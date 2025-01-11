package com.example.demo;

import com.fasterxml.jackson.core.type.TypeReference;
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

    @GetMapping
    public String getEmployeesAsCsv() {
        StringBuilder csvContent = new StringBuilder();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CSV_FILE);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                csvContent.append(line).append("\n");
            }
        } catch (IOException | NullPointerException e) {
            return "Error reading CSV file: " + e.getMessage();
        }
        return csvContent.toString();
    }

    @GetMapping("/employees/json")
    public List<Employee> getOrConvertEmployeesFromCsv() {
        File jsonFile = new File(JSON_FILE);
        if (!jsonFile.exists()) {
            // Si el archivo JSON no existe, convertir el CSV a JSON
            employeeList.clear();
            loadEmployeesFromCsv();
            saveEmployeesToJson();
        } else {
            // Si el archivo JSON existe, cargarlo
            loadEmployeesFromJson();
        }
        return employeeList;
    }
    
    //devuelve el json de los empleados una vez ejecutas lo anterior para pasar el csv a json
    @GetMapping("/employees/json")
    public List<Employee> getEmployeesFromJson() {
        loadEmployeesFromJson();
        return employeeList;
    }

    private void loadEmployeesFromCsv() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CSV_FILE);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Employee employee = new Employee(
                        Integer.parseInt(values[0]),
                        values[1],
                        values[2],
                        Double.parseDouble(values[3]),
                        values[4]
                );
                employeeList.add(employee);
            }
        } catch (IOException | NullPointerException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    private void loadEmployeesFromJson() {
        File file = new File(JSON_FILE);
        if (file.exists()) {
            try {
                employeeList = objectMapper.readValue(file, new TypeReference<List<Employee>>() {});
            } catch (IOException e) {
                System.err.println("Error reading JSON file: " + e.getMessage());
            }
        } else {
            System.err.println("JSON file not found: " + JSON_FILE);
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