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

    private static final String BASE_URL = "http://localhost:8087/books"; // Cambia el endpoint según tu backend

    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    // Obtener lista de libros
    public List<Book> getBooks() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), new TypeToken<List<Book>>() {}.getType());
        } catch (IOException | InterruptedException e) {
            logger.error("Error fetching books from backend", e);
            throw new RuntimeException("Error fetching books from backend", e);
        }
    }

    // Generar PDF
    public String generatePdf(String bookTitle) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/pdf"))
                    .POST(HttpRequest.BodyPublishers.ofString("\"" + bookTitle + "\""))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            logger.error("Error generating PDF for {}", bookTitle, e);
            throw new RuntimeException("Error generating PDF for " + bookTitle, e);
        }
    }

    // Agregar nuevo libro
    public String addBook(Book book) {
        try {
            String bookJson = gson.toJson(book);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/add"))
                    .POST(HttpRequest.BodyPublishers.ofString(bookJson))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            logger.error("Error adding book: {}", book, e);
            throw new RuntimeException("Error adding book", e);
        }
    }
}
