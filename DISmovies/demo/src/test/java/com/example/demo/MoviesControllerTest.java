package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MoviesControllerTest {

    private MoviesController moviesController;

    @BeforeEach
    public void setUp() {
        // Inicializa el controlador antes de cada prueba
        moviesController = new MoviesController();
    }

    @Test
    public void testGetAllMovies() {
        // Cargar películas iniciales desde movies.json
        List<Movies> moviesList = moviesController.getAllMovies();

        // Verificar que la lista no está vacía
        assertNotNull(moviesList);
        assertTrue(moviesList.size() > 0, "La lista de películas debería contener elementos.");
    }

    @Test
    public void testGetMovieById() {
        // Cargar películas iniciales y obtener el primer ID
        List<Movies> moviesList = moviesController.getAllMovies();
        String id = moviesList.get(0).getId();

        // Obtener la película por ID
        Object movie = moviesController.getMovieById(id);

        // Verificar que la película existe
        assertNotNull(movie, "La película no debería ser nula.");
        assertTrue(movie instanceof Movies, "El objeto debería ser de tipo Movies.");
        assertEquals(id, ((Movies) movie).getId(), "El ID de la película no coincide.");
    }

    @Test
    public void testAddMovie() {
        // Crear una nueva película
        String tempId = UUID.randomUUID().toString();
        Movies newMovie = new Movies(
                tempId,
                "Inception",
                "Christopher Nolan",
                "2010",
                "Ciencia Ficción",
                List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Ellen Page"),
                "Un ladrón que roba secretos corporativos mediante tecnología de sueños compartidos."
        );

        // Añadir la película al controlador
        Movies addedMovie = moviesController.addMovie(newMovie);

        // Verificar que la película tiene un ID generado
        assertNotNull(addedMovie.getId(), "El ID de la película no debería ser nulo.");

        // Verificar que la película se ha añadido a la lista
        List<Movies> moviesList = moviesController.getAllMovies();
        assertTrue(moviesList.contains(addedMovie), "La película debería estar en la lista.");
    }

    @Test
    public void testUpdateMovie() {
        // Obtener la primera película de la lista
        List<Movies> moviesList = moviesController.getAllMovies();
        Movies originalMovie = moviesList.get(0);
        String id = originalMovie.getId();

        // Crear una película actualizada
        Movies updatedMovie = new Movies(
                id,
                "Interstellar",
                "Christopher Nolan",
                "2014",
                "Ciencia Ficción",
                List.of("Matthew McConaughey", "Anne Hathaway", "Jessica Chastain"),
                "Un grupo de astronautas viaja a través de un agujero de gusano en busca de un nuevo hogar para la humanidad."
        );

        // Actualizar la película
        String response = moviesController.updateMovie(id, updatedMovie);

        // Verificar respuesta
        assertEquals("Película con ID " + id + " actualizada correctamente.", response);

        // Verificar que los datos se actualizaron
        Object movie = moviesController.getMovieById(id);
        assertTrue(movie instanceof Movies);
        Movies updated = (Movies) movie;

        assertEquals("Interstellar", updated.getTitle());
        assertEquals("Christopher Nolan", updated.getDirector());
        assertEquals("2014", updated.getYear());
    }

    @Test
    public void testDeleteMovie() {
        // Obtener la primera película de la lista
        List<Movies> moviesList = moviesController.getAllMovies();
        Movies movieToDelete = moviesList.get(0);
        String id = movieToDelete.getId();

        // Eliminar la película
        String response = moviesController.deleteMovie(id);

        // Verificar respuesta
        assertEquals("Película con ID " + id + " eliminada correctamente.", response);

        // Verificar que la película ya no está en la lista
        List<Movies> updatedMoviesList = moviesController.getAllMovies();
        assertFalse(updatedMoviesList.stream().anyMatch(m -> m.getId().equals(id)), "La película debería haber sido eliminada.");
    }

    @Test
    public void testDeleteAllMovies() {
        // Eliminar todas las películas
        String response = moviesController.deleteAllMovies();

        // Verificar respuesta
        assertEquals("Todas las películas han sido eliminadas.", response);

        // Verificar que la lista está vacía
        List<Movies> moviesList = moviesController.getAllMovies();
        assertTrue(moviesList.isEmpty(), "La lista de películas debería estar vacía.");
    }

    @Test
    public void testResetDatabase() {
        // Resetear la base de datos
        String response = moviesController.resetDatabase();

        // Verificar respuesta
        assertEquals("La base de datos ha sido reseteada correctamente.", response);

        // Verificar que las películas iniciales están presentes
        List<Movies> moviesList = moviesController.getAllMovies();
        assertNotNull(moviesList);
        assertTrue(moviesList.size() > 0, "La lista de películas debería haberse restaurado.");
    }
}
