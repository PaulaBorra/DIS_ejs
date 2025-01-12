package com.example.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/movies")
public class MoviesController {

    private final String FILE_PATH = "demo/src/main/resources/movies.json"; // Ruta del archivo JSON
    private final String INITIAL_FILE_PATH = "demo/src/main/resources/movies_initial.json"; // Ruta del archivo JSON inicial
    private List<Movies> moviesList = new ArrayList<>(); // Lista de películas
    private final ObjectMapper objectMapper = new ObjectMapper(); // Para manejar JSON

    // GET: Devuelve toda la lista de películas
    @GetMapping
    public List<Movies> getAllMovies() {
        loadMoviesFromFile(); // Cargar la lista desde el archivo JSON
        return moviesList; // Devolver la lista
    }

    // GET: Devuelve una película por ID
    @GetMapping("/{id}")
    public Object getMovieById(@PathVariable String id) {
        loadMoviesFromFile(); // Asegurar que los datos estén actualizados
        for (Movies movie : moviesList) {
            if (movie.getId().equals(id)) {
                return movie; // Si encuentra la película, la devuelve
            }
        }
        return "Película con ID " + id + " no encontrada."; // Mensaje si no encuentra la película
    }

    // POST: Agregar una nueva película
    @PostMapping
    public Movies addMovie(@RequestBody Movies newMovie) {
        loadMoviesFromFile(); // Cargar la lista actualizada
        newMovie.setId(UUID.randomUUID().toString()); // Generar un nuevo ID único
        moviesList.add(newMovie); // Agregar la película a la lista
        saveMoviesToFile(); // Guardar la lista actualizada en el archivo JSON
        return newMovie; // Devolver la película creada
    }

    // PUT: Actualizar una película por ID
    @PutMapping("/{id}")
    public String updateMovie(@PathVariable String id, @RequestBody Movies updatedMovie) {
        loadMoviesFromFile(); // Cargar la lista actualizada
        for (Movies movie : moviesList) {
            if (movie.getId().equals(id)) {
                movie.setTitle(updatedMovie.getTitle());
                movie.setDirector(updatedMovie.getDirector());
                movie.setYear(updatedMovie.getYear());
                movie.setGenre(updatedMovie.getGenre());
                movie.setCast(updatedMovie.getCast());
                movie.setDescription(updatedMovie.getDescription());
                saveMoviesToFile(); // Guardar los cambios en el archivo JSON
                return "Película con ID " + id + " actualizada correctamente.";
            }
        }
        return "Película con ID " + id + " no encontrada.";
    }

    // DELETE: Borrar todas las películas
    @DeleteMapping
    public String deleteAllMovies() {
        moviesList.clear(); // Vaciar la lista de películas
        saveMoviesToFile(); // Guardar una lista vacía en el archivo JSON
        return "Todas las películas han sido eliminadas.";
    }

    // DELETE: Resetear la base de datos
    @DeleteMapping("/reset")
    public String resetDatabase() {
        File initialFile = new File(INITIAL_FILE_PATH);

        if (initialFile.exists()) {
            try {
                // Sobrescribir el archivo principal con el contenido del archivo inicial
                moviesList = objectMapper.readValue(initialFile, new TypeReference<List<Movies>>() {});
                saveMoviesToFile(); // Guardar los datos iniciales en el archivo principal
                return "La base de datos ha sido reseteada correctamente.";
            } catch (IOException e) {
                return "Error al resetear la base de datos: " + e.getMessage();
            }
        } else {
            return "El archivo inicial no existe. No se puede resetear la base de datos.";
        }
    }

    // DELETE: Borrar una película por ID
    @DeleteMapping("/{id}")
    public String deleteMovie(@PathVariable String id) {
        loadMoviesFromFile(); // Cargar la lista actualizada
        boolean removed = moviesList.removeIf(movie -> movie.getId().equals(id));
        if (removed) {
            saveMoviesToFile(); // Guardar la lista actualizada en el archivo JSON
            return "Película con ID " + id + " eliminada correctamente.";
        } else {
            return "Película con ID " + id + " no encontrada.";
        }
    }

    // Métodos auxiliares para manejar el archivo JSON
    private void loadMoviesFromFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try {
                moviesList = objectMapper.readValue(file, new TypeReference<List<Movies>>() {});
            } catch (IOException e) {
                System.err.println("Error al cargar el archivo JSON: " + e.getMessage());
            }
        } else {
            moviesList = new ArrayList<>(); // Si no existe el archivo, inicializar una lista vacía
        }
    }

    private void saveMoviesToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), moviesList);
        } catch (IOException e) {
            System.err.println("Error al guardar en el archivo JSON: " + e.getMessage());
        }
    }
}
