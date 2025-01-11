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

    private final DataService dataService = new DataService();
    private final Grid<Book> grid = new Grid<>(Book.class, false);

    public MainView() {
        setSizeFull();
        setSpacing(true);

        configureGrid();  // Configura la tabla para mostrar los libros
        loadBooks();      // Carga los libros desde el backend

        // Formulario para añadir un nuevo libro
        TextField titleField = new TextField("Title");
        TextField authorField = new TextField("Author");
        TextField yearField = new TextField("Year");
        TextField genreField = new TextField("Genre");
        TextField isbnField = new TextField("ISBN");
        Button addButton = new Button("Add Book", click -> {
            try {
                // Validación simple de campos vacíos
                if (titleField.isEmpty() || authorField.isEmpty() || yearField.isEmpty() || genreField.isEmpty() || isbnField.isEmpty()) {
                    Notification.show("All fields must be filled!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                // Crea un nuevo objeto Book y lo envía al backend
                Book book = new Book(
                    titleField.getValue(),
                    authorField.getValue(),
                    Integer.parseInt(yearField.getValue()),
                    genreField.getValue(),
                    isbnField.getValue()
                );


                String response = dataService.addBook(book);
                Notification.show(response, 3000, Notification.Position.MIDDLE);

                titleField.clear();
                authorField.clear();
                yearField.clear();
                genreField.clear();
                isbnField.clear();

                loadBooks(); // Recarga la lista de libros

            } catch (Exception e) {
                Notification.show("Error adding book: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });

        // Añade el formulario y la tabla al diseño
        HorizontalLayout formLayout = new HorizontalLayout(titleField, authorField, yearField, genreField, isbnField, addButton);
        formLayout.setWidthFull();
        formLayout.setSpacing(true);
        add(formLayout, grid);
    }

    private void configureGrid() {
        grid.addColumn(Book::getTitle).setHeader("Title");
        grid.addColumn(Book::getAuthor).setHeader("Author");
        grid.addColumn(Book::getYear).setHeader("Year");
        grid.addColumn(Book::getGenre).setHeader("Genre");
        grid.addColumn(Book::getISBN).setHeader("ISBN");

        // Añadir botón de "Generate PDF"
        grid.addComponentColumn(book -> {
            Button pdfButton = new Button("Generate PDF");
            pdfButton.addClickListener(click -> {
                try {
                    String response = dataService.generatePdf(book.getTitle());
                    Notification.show(response, 3000, Notification.Position.MIDDLE);
                } catch (Exception e) {
                    Notification.show("Error generating PDF: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
                }
            });
            return pdfButton;
        }).setHeader("Actions");

        grid.setSizeFull();
        add(grid);
    }

    private void loadBooks() {
        try {
            List<Book> books = dataService.getBooks(); // Llama al backend para obtener los libros
            grid.setItems(books); // Muestra los libros en el grid
        } catch (Exception e) {
            Notification.show("Error loading books: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }
}
