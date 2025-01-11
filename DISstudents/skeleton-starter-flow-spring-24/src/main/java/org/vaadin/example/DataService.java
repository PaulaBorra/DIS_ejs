package org.vaadin.example;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class DataService {

    // Obtener la lista de estudiantes desde el backend
    public static List<Students> getStudents() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI("http://localhost:8082/students"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        Gson gson = new Gson();
        java.lang.reflect.Type studentListType = new com.google.gson.reflect.TypeToken<List<Students>>(){}.getType();
        return gson.fromJson(responseBody, studentListType);
    }

    // Agregar un nuevo estudiante al backend
    public static void addStudent(Students student) throws IOException, InterruptedException {
        Gson gson = new Gson();
        String body = gson.toJson(student);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8082/students")) // Ruta del backend
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Opcional: imprimir la respuesta del backend
        System.out.println(response.body());
    }

    public static String exportToCsv() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8082/students/export")) // Endpoint del backend
            .POST(HttpRequest.BodyPublishers.noBody()) // No necesita cuerpo
            .build();
    
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    
        // Imprimir la respuesta del backend
        System.out.println(response.body());
    
        return response.body(); // Devuelve el mensaje de éxito o error
    }
    
}
