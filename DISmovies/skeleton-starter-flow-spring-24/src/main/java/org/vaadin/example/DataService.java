package org.vaadin.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class DataService {

    private static final String BASE_URL = "http://localhost:8083/api/movies";

    // Obtener la lista de películas desde el backend
    public static List<Movies> getMovies() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        Gson gson = new Gson();
        java.lang.reflect.Type movieListType = new TypeToken<List<Movies>>() {}.getType();
        return gson.fromJson(responseBody, movieListType);
    }

    // Agregar una nueva película al backend
    public static void addMovie(Movies movie) throws IOException, InterruptedException {
        Gson gson = new Gson();
        String body = gson.toJson(movie);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body()); // Imprimir respuesta para depuración
    }

    // Editar una película por ID
    public static void updateMovie(String id, Movies movie) throws IOException, InterruptedException {
        Gson gson = new Gson();
        String body = gson.toJson(movie);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body()); // Imprimir respuesta para depuración
    }

    // Eliminar una película por ID
    public static void deleteMovie(String id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body()); // Imprimir respuesta para depuración
    }

    // Eliminar todas las películas
    public static void deleteAllMovies() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body()); // Imprimir respuesta para depuración
    }

    // Resetear la base de datos
    public static String resetDatabase() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/reset"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body(); // Devuelve el mensaje del backend
    }
}
