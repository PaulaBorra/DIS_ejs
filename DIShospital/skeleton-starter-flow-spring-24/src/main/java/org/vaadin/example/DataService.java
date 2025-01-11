package org.vaadin.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DataService {

    private static final Logger logger = LoggerFactory.getLogger(DataService.class);

    private static final String BASE_URL = "http://localhost:8082/api";

    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<Hospital> fetchHospitals() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/hospital"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), new TypeToken<List<Hospital>>() {}.getType());
        } catch (IOException | InterruptedException e) {
            logger.error("Error fetching hospitals from backend", e);
            throw new RuntimeException("Error fetching hospitals from backend", e);
        }
    }

    public String generatePdf(String hospitalName) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/generate-pdf"))
                    .POST(HttpRequest.BodyPublishers.ofString("\"" + hospitalName + "\""))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            logger.error("Error generating PDF for {}", hospitalName, e);
            throw new RuntimeException("Error generating PDF for " + hospitalName, e);
        }
    }
}
