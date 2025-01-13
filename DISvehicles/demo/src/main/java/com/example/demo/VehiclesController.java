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
@RequestMapping
public class VehiclesController {

    private final String FILE_PATH = "vehicles.json";
    private final String EXPORT_FOLDER = "exports";
    private List<Vehicles> vehiclesList = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/vehicles")
    public Vehicles addVehicle(@RequestBody Vehicles vehicle) {
        vehicle.setUuid(UUID.randomUUID().toString()); // Se autogenera el id 
        vehiclesList.add(vehicle);
        saveVehiclesToFile(); // Se guarda en un json
        return vehicle;
    }

    @GetMapping("/vehicles")
    public List<Vehicles> getAllVehicles() {
        loadVehiclesFromFile();
        return vehiclesList;
    }

    @PostMapping("/export")
    public String exportToCsv() {
        ensureExportFolderExists();

        String csvFilePath = EXPORT_FOLDER + "/vehicles.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            writer.write("UUID,Make,Model,YearOfManufacture,Type,LicensePlateNumber");
            writer.newLine();

            for (Vehicles vehicle : vehiclesList) {
                writer.write(String.format("%s,%s,%s,%d,%s,%s",
                        vehicle.getUuid(),
                        vehicle.getMake(),
                        vehicle.getModel(),
                        vehicle.getYearOfManufacture(),
                        vehicle.getType(),
                        vehicle.getLicensePlateNumber()));
                writer.newLine();
            }

            return "CSV exportado correctamente";

        } catch (IOException e) {
            System.err.println("Error al exportar a CSV");
            return "Error al exportar a CSV";
        }
    }

    private void loadVehiclesFromFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try {
                vehiclesList = objectMapper.readValue(file, new TypeReference<List<Vehicles>>() {});
            } catch (IOException e) {
                System.err.println("Error al cargar el archivo JSON");
            }
        }
    }

    private void saveVehiclesToFile() {
        try {
            objectMapper.writeValue(new File(FILE_PATH), vehiclesList);
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
}