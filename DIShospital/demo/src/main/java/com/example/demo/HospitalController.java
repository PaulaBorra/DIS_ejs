package com.example.demo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@RestController
@RequestMapping("/api")
public class HospitalController {

    private static final Logger logger = LoggerFactory.getLogger(HospitalController.class);
    private static final String JSON_PATH = new File("demo/src/main/resources/datos.json").getAbsolutePath();
    private static final String EXPORTS_FOLDER = new File("demo/src/main/resources/exports/").getAbsolutePath();
    private static final String HOSPITALS_FOLDER = new File("demo/src/main/resources/hospitals/").getAbsolutePath();
    private static final String SUMMARY_FILE = HOSPITALS_FOLDER + "/summary.json";

    // Usamos Hashmap porque queremos almacenar el nombre del hospital y la cantidad de veces que se ha generado un PDF para ese hospital. Es clave : valor.
    private Map<String, Integer> summaryMap = new HashMap<>();

    public HospitalController() {
        // Crea la carpeta de exportaciones si no existe.
        new File(EXPORTS_FOLDER).mkdirs();
        // Crea la carpeta de hospitales si no existe.
        new File(HOSPITALS_FOLDER).mkdirs();

        File summaryFile = new File(SUMMARY_FILE);
        if (!summaryFile.exists()) {
            // Si el archivo summary.json no existe, se inicializa como un archivo vacío.
            try (Writer writer = new FileWriter(SUMMARY_FILE)) {
                new Gson().toJson(summaryMap, writer);
            } catch (IOException e) {
                // Maneja errores al inicializar summary.json.
                logger.error("Error initializing summary.json", e);
                throw new RuntimeException("Error initializing summary.json", e);
            }
        } else {
            try (Reader reader = new FileReader(SUMMARY_FILE)) {
                // Si el archivo summary.json existe, carga su contenido en summaryMap.
                summaryMap = new Gson().fromJson(reader, new TypeToken<Map<String, Integer>>() {}.getType());
            } catch (IOException e) {
                // Maneja errores al leer summary.json.
                logger.error("Error reading summary.json", e);
                throw new RuntimeException("Error reading summary.json", e);
            }
        }
    }

    @GetMapping("/hospital")
    public List<Hospital> getAllHospitals() {
        try (FileReader reader = new FileReader(JSON_PATH)) {
            return new Gson().fromJson(reader, new TypeToken<List<Hospital>>() {}.getType());
        } catch (IOException e) {
            logger.error("Error reading JSON file", e);
            throw new RuntimeException("Error reading JSON file", e);
        }
    }

    @PostMapping("/generate-pdf")
    public String generatePdf(@RequestBody String hospitalName) {
        String cleanedName = hospitalName.trim().replace("\"", "");
        logger.info("Received request to generate PDF for '{}'", cleanedName);

        try (FileReader reader = new FileReader(JSON_PATH)) {
            List<Hospital> hospitals = new Gson().fromJson(reader, new TypeToken<List<Hospital>>() {}.getType());
            logger.info("Loaded hospitals: {}", hospitals);

            // Buscar el hospital
            Hospital selectedHospital = hospitals.stream()
                    .filter(h -> h.getHospitalName().trim().equalsIgnoreCase(cleanedName))
                    .findFirst()
                    .orElseThrow(() -> {
                        logger.warn("Hospital not found: '{}'", cleanedName);
                        return new RuntimeException("Hospital not found");
                    });

            // Generar el PDF usando PDFManager
            File pdfFile = new File(EXPORTS_FOLDER + "/" + cleanedName + ".pdf");
            try {
                PDFManager.generateHospitalPdf(selectedHospital, pdfFile.getAbsolutePath());
                logger.info("PDF saved to {}", pdfFile.getAbsolutePath());
            } catch (Exception e) {
                logger.error("Error generating PDF for {}", cleanedName, e);
                return "Error generating PDF for " + cleanedName;
            }

            // Actualizar resumen
            summaryMap.put(cleanedName, summaryMap.getOrDefault(cleanedName, 0) + 1);
            try (Writer writer = new FileWriter(SUMMARY_FILE)) {
                new Gson().toJson(summaryMap, writer);
                logger.info("Updated summary.json with {}", summaryMap);
            } catch (IOException e) {
                logger.error("Error updating summary.json", e);
                return "Error updating summary.json";
            }

            return "PDF generated successfully for " + cleanedName;
        } catch (Exception e) {
            logger.error("Error generating PDF for {}", hospitalName.trim(), e);
            return "Error generating PDF for " + hospitalName.trim();
        }
    }
}