package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final String CSV_FILE = "employees.csv";
    private final String JSON_FILE = "employees.json";
    private List<Employee> employeeList = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EmployeeController() {
        //si queremos que se pase de csv a json sin hacer el get /csv-to-json, ponemos aqui:
        loadEmployeesFromCsv();
        saveEmployeesToJson();
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeList;
    }

    @PostMapping
    public String addEmployee(@RequestBody Employee newEmployee) {
        employeeList.add(newEmployee);
        saveEmployeesToJson();
        return "Employee added successfully!";
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable int id) {
        boolean removed = employeeList.removeIf(employee -> employee.getId() == id);
        if (removed) {
            saveEmployeesToJson();
            return "Employee with ID " + id + " deleted.";
        } else {
            return "Employee with ID " + id + " not found.";
        }
    }

    @GetMapping("/export-csv")
    public String exportToCsv() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) {
            writer.write("id,firstName,lastName,salary,position");
            writer.newLine();
            for (Employee employee : employeeList) {
                writer.write(String.format("%d,%s,%s,%.2f,%s",
                        employee.getId(),
                        employee.getName(),
                        employee.getDepartment(),
                        employee.getSalary(),
                        employee.getHireDate()));
                writer.newLine();
            }
            return "CSV exported successfully to: " + CSV_FILE;
        } catch (IOException e) {
            System.err.println("Error exporting to CSV: " + e.getMessage());
            return "Error exporting to CSV.";
        }
    }

    @GetMapping("/csv-to-json")
    public String convertCsvToJson() {
        try {
            // Cargar empleados desde el archivo CSV
            loadEmployeesFromCsv();
            // Guardar empleados en el archivo JSON
            saveEmployeesToJson();

            return "CSV successfully converted to JSON: " + JSON_FILE;
        } catch (Exception e) {
            return "Error converting CSV to JSON: " + e.getMessage();
        }
    }

    private void loadEmployeesFromCsv() {
        // Ruta relativa al archivo CSV desde la raíz del proyecto
        String filePath = "./employees.csv"; 
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("CSV file not found: " + file.getAbsolutePath());
            return;
        }
    
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Saltar encabezados
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Employee employee = new Employee(
                        Integer.parseInt(values[0]), // ID
                        values[1],                   // Nombre
                        values[2],                   // Departamento
                        Double.parseDouble(values[3]), // Salario
                        values[4]                    // Fecha de contratación
                );
                employeeList.add(employee); // Agregar empleado a la lista
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }    

    private void saveEmployeesToJson() {
        try {
            objectMapper.writeValue(new File(JSON_FILE), employeeList);
        } catch (IOException e) {
            System.err.println("Error saving to JSON file: " + e.getMessage());
        }
    }
}