package com.example.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/students")
public class StudentsController {

    private final String FILE_PATH = "students.json";
    private final String EXPORT_FOLDER = "exports";
    private List<Students> studentsList = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public Students addStudent(@RequestBody Students student) {
        student.setUuid(UUID.randomUUID().toString()); 
        studentsList.add(student);
        saveStudentsToFile(); 
        return student;
    }

    @GetMapping
    public List<Students> getAllStudents() {
        loadStudentsFromFile();
        return studentsList;
    }

    @PostMapping("/export")
    public String exportToCsv() {
        ensureExportFolderExists();

        String csvFilePath = EXPORT_FOLDER + "/students.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            writer.write("UUID,FirstName,LastName,DateOfBirth,Gender");
            writer.newLine();

            for (Students student : studentsList) {
                writer.write(String.format("%s,%s,%s,%d,%s",
                        student.getUuid(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getDateOfBirth(),
                        student.getGender()));
                writer.newLine();
            }

            return "CSV exportado correctamente";

        } catch (IOException e) {
            System.err.println("Error al exportar a CSV");
            return "Error al exportar a CSV";
        }
    }

    private void loadStudentsFromFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try {
                studentsList = objectMapper.readValue(file, new TypeReference<List<Students>>() {});
            } catch (IOException e) {
                System.err.println("Error al cargar el archivo JSON");
            }
        }
    }

    private void saveStudentsToFile() {
        try {
            objectMapper.writeValue(new File(FILE_PATH), studentsList);
        } catch (IOException e) {
            System.err.println("Error al guardar en el archivo JSON");
        }
    }

    private void ensureExportFolderExists() {
        Path path = Paths.get(EXPORT_FOLDER);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                System.err.println("Error al crear la carpeta de exportación: " + e.getMessage());
            }
        }
    }

    @DeleteMapping("/{uuid}")
    public String deleteStudent(@PathVariable String uuid) {
        boolean removed = studentsList.removeIf(student -> student.getUuid().equals(uuid));
        
        if (removed) {
            saveStudentsToFile();
            return "Estudiante con UUID " + uuid + " eliminado correctamente.";
        } else {
            return "Estudiante con UUID " + uuid + " no encontrado.";
        }
    }

    @PutMapping("/{uuid}")
    public String updateStudent(@PathVariable String uuid, @RequestBody Students updatedStudent) {
        for (Students student : studentsList) {
            if (student.getUuid().equals(uuid)) {
                student.setFirstName(updatedStudent.getFirstName());
                student.setLastName(updatedStudent.getLastName());
                student.setDateOfBirth(updatedStudent.getDateOfBirth());
                student.setGender(updatedStudent.getGender());
                saveStudentsToFile(); 
                return "Estudiante actualizado correctamente.";
            }
        }
        return "Estudiante con UUID " + uuid + " no encontrado.";
    }
}