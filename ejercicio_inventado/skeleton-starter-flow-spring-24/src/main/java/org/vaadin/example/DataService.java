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

    // Obtener la lista de jugadores desde el backend
    public static List<Player> getAllPlayers() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI("http://localhost:8084/players"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        Gson gson = new Gson();
        java.lang.reflect.Type playerListType = new com.google.gson.reflect.TypeToken<List<Player>>(){}.getType();
        return gson.fromJson(responseBody, playerListType);
    }

    // Agregar un nuevo jugador al backend
    public static void addPlayer(Player player) throws IOException, InterruptedException {
        Gson gson = new Gson();
        String body = gson.toJson(player);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8084/players")) // Ruta del backend
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Opcional: imprimir la respuesta del backend
        System.out.println(response.body());
    }

    // Eliminar un jugador por su ID
    public static void deletePlayer(String playerId) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8084/players/" + playerId)) // Endpoint del backend con ID del jugador
            .DELETE()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Opcional: imprimir la respuesta del backend
        System.out.println(response.body());
    }

    // Exportar un jugador a PDF
    public static void exportPlayerToPdf(String playerName) throws IOException, InterruptedException {
        Gson gson = new Gson();
        String body = gson.toJson(playerName);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8084/players/generate-pdf"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Opcional: imprimir la respuesta del backend
        System.out.println(response.body());
    }

    // Exportar todos los jugadores a CSV
    public static String exportToCsv() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8084/players/export"))
            .GET() 
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Imprimir la respuesta del backend
        System.out.println(response.body());

        return response.body(); // Devuelve el mensaje de éxito o error
    }

    public static void updatePlayer(Player player) throws IOException, InterruptedException {
        Gson gson = new Gson();
        String body = gson.toJson(player);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8084/players/" + player.getId())) // Ruta del backend
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(body))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Opcional: imprimir la respuesta del backend
        System.out.println(response.body());
    }
}