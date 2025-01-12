package org.vaadin.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route
public class MainView extends VerticalLayout {

    private final Grid<Movies> movieGrid = new Grid<>(Movies.class, false);

    public MainView() {
        setSpacing(true); // Espacio entre componentes
        setMargin(true); // Márgenes del layout

        // Crear campos del formulario
        TextField titleField = new TextField("Title");
        TextField directorField = new TextField("Director");
        TextField yearField = new TextField("Year");
        TextField genreField = new TextField("Genre");
        TextField castField = new TextField("Cast (comma-separated)");
        TextField descriptionField = new TextField("Description");

        // Botones
        Button saveButton = new Button("Add Movie");
        Button deleteAllButton = new Button("Delete All Movies");
        Button resetButton = new Button("Reset Database");

        // Layout del formulario
        HorizontalLayout formLayout = new HorizontalLayout(
                titleField, directorField, yearField, genreField, castField, descriptionField, saveButton
        );

        // Configurar el Grid para mostrar las películas
        movieGrid.addColumn(Movies::getTitle).setHeader("Title");
        movieGrid.addColumn(Movies::getDirector).setHeader("Director");
        movieGrid.addColumn(Movies::getYear).setHeader("Year");
        movieGrid.addColumn(Movies::getGenre).setHeader("Genre");
        movieGrid.addColumn(Movies::getCast).setHeader("Cast");
        movieGrid.addColumn(Movies::getDescription).setHeader("Description");
        movieGrid.addComponentColumn(movie -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(click -> {
                deleteMovie(movie.getId());
            });
            return deleteButton;
        }).setHeader("Actions");

        // Obtener los datos iniciales desde el backend
        refreshGrid();

        // Acción para agregar una nueva película
        saveButton.addClickListener(e -> {
            try {
                if (titleField.isEmpty() || directorField.isEmpty() || yearField.isEmpty() ||
                        genreField.isEmpty() || castField.isEmpty() || descriptionField.isEmpty()) {
                    Notification.show("Please fill in all the fields!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                Movies newMovie = new Movies(
                        null, // ID lo generará el backend
                        titleField.getValue(),
                        directorField.getValue(),
                        yearField.getValue(),
                        genreField.getValue(),
                        List.of(castField.getValue().split(",")), // Convertir la lista separada por comas
                        descriptionField.getValue()
                );

                DataService.addMovie(newMovie);
                refreshGrid();

                Notification.show("Movie added successfully!", 3000, Notification.Position.MIDDLE);
                clearForm(titleField, directorField, yearField, genreField, castField, descriptionField);

            } catch (Exception ex) {
                Notification.show("Error adding movie: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });

        // Acción para eliminar todas las películas
        deleteAllButton.addClickListener(event -> {
            try {
                DataService.deleteAllMovies();
                refreshGrid();
                Notification.show("All movies deleted successfully!", 3000, Notification.Position.MIDDLE);
            } catch (Exception e) {
                Notification.show("Error deleting movies: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });

        // Acción para resetear la base de datos
        resetButton.addClickListener(event -> {
            try {
                String resultMessage = DataService.resetDatabase();
                Notification.show(resultMessage, 3000, Notification.Position.MIDDLE);
                refreshGrid();
            } catch (Exception e) {
                Notification.show("Error resetting database: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });

        // Agregar los componentes al layout principal
        add(formLayout, movieGrid, deleteAllButton, resetButton);
    }

    // Refresca el Grid obteniendo la lista actualizada desde el backend
    private void refreshGrid() {
        try {
            List<Movies> moviesFromBackend = DataService.getMovies();
            movieGrid.setItems(moviesFromBackend);
        } catch (Exception e) {
            Notification.show("Error fetching movies: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    // Eliminar una película por ID
    private void deleteMovie(String id) {
        try {
            DataService.deleteMovie(id);
            refreshGrid();
            Notification.show("Movie deleted successfully!", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Error deleting movie: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    // Limpia los campos del formulario
    private void clearForm(TextField titleField, TextField directorField, TextField yearField,
                           TextField genreField, TextField castField, TextField descriptionField) {
        titleField.clear();
        directorField.clear();
        yearField.clear();
        genreField.clear();
        castField.clear();
        descriptionField.clear();
    }
}