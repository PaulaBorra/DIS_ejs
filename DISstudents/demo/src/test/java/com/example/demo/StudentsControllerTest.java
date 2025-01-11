package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentsControllerTest {

    private StudentsController studentsController;

    @BeforeEach
    public void setUp() {
        // Inicializa el controlador antes de cada prueba
        studentsController = new StudentsController();
    }

    @Test
    public void testAddStudent() {
        // Crear un nuevo estudiante
        Students student = new Students("John", "Doe", 20000101, "Male");

        // Añadir estudiante al controlador
        Students addedStudent = studentsController.addStudent(student);

        // Verificar que el estudiante tiene un UUID generado
        assertNotNull(addedStudent.getUuid());

        // Verificar que el estudiante se ha añadido a la lista
        List<Students> studentsList = studentsController.getAllStudents();
        assertTrue(studentsList.contains(addedStudent));
    }

    @Test
    public void testGetAllStudents() {
        // Crear y añadir estudiantes
        studentsController.addStudent(new Students("John", "Doe", 20000101, "Male"));
        studentsController.addStudent(new Students("Jane", "Smith", 19991231, "Female"));

        // Recuperar la lista de estudiantes
        List<Students> studentsList = studentsController.getAllStudents();

        // Verificar que la lista contiene los estudiantes añadidos
        assertEquals(2, studentsList.size());
    }

    @Test
    public void testDeleteStudent() {
        // Crear y añadir un estudiante
        Students student = studentsController.addStudent(new Students("John", "Doe", 20000101, "Male"));
        String uuid = student.getUuid();

        // Eliminar el estudiante
        String response = studentsController.deleteStudent(uuid);

        // Verificar respuesta
        assertEquals("Estudiante con UUID " + uuid + " eliminado correctamente.", response);

        // Verificar que el estudiante ya no está en la lista
        List<Students> studentsList = studentsController.getAllStudents();
        assertFalse(studentsList.stream().anyMatch(s -> s.getUuid().equals(uuid)));
    }

    @Test
    public void testUpdateStudent() {
        // Crear y añadir un estudiante
        Students student = studentsController.addStudent(new Students("John", "Doe", 20000101, "Male"));
        String uuid = student.getUuid();

        // Crear un estudiante actualizado
        Students updatedStudent = new Students("Johnny", "Doe", 19991231, "Male");

        // Actualizar estudiante
        String response = studentsController.updateStudent(uuid, updatedStudent);

        // Verificar respuesta
        assertEquals("Estudiante actualizado correctamente.", response);

        // Verificar que los datos se actualizaron
        Students updated = studentsController.getAllStudents().stream()
                .filter(s -> s.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);

        assertNotNull(updated);
        assertEquals("Johnny", updated.getFirstName());
        assertEquals(19991231, updated.getDateOfBirth());
    }
}
