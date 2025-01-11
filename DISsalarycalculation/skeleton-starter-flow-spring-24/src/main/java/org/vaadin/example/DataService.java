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

    private static final String BASE_URL = "http://localhost:8084/";

    // Obtener los tipos de empleados desde el backend
    public static List<String> getEmployeeTypes() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "employeeTypes"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Gson gson = new Gson();

        // Convertir la respuesta JSON en una lista de strings
        java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<List<String>>() {}.getType();
        return gson.fromJson(response.body(), listType);
    }

    // Calcular salario bruto (pre-tax) llamando al backend
    public static Double calculatePreTaxSalary(SalaryCalculation salaryCalc) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        String body = gson.toJson(salaryCalc);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "preTax"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return Double.valueOf(response.body());
    }

    // Calcular salario neto (post-tax) llamando al backend
    public static Double calculatePostTaxSalary(SalaryCalculation salaryCalc) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        String body = gson.toJson(salaryCalc);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "postTax"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return Double.valueOf(response.body());
    }
}
