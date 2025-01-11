package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookControllerTest {

    private BookController bookController;

    @BeforeEach
    public void setUp() {
        // Inicializa el controlador antes de cada prueba
        bookController = new BookController();
    }

    @Test
    public void testAddBook() {
        // Crear un nuevo libro
        Book book = new Book("1984", "George Orwell", 1949, "Dystopian", "9780451524935");

        // Añadir libro al controlador
        String response = bookController.addBook(book);

        // Verificar la respuesta
        assertEquals("Book added successfully: 1984", response);

        // Verificar que el libro se ha añadido a la lista
        List<Book> bookList = bookController.getAllBooks();
        assertTrue(bookList.stream().anyMatch(b -> b.getTitle().equals("1984")));
    }

    @Test
    public void testAddDuplicateBook() {
        // Crear y añadir un libro
        Book book1 = new Book("1984", "George Orwell", 1949, "Dystopian", "9780451524935");
        bookController.addBook(book1);

        // Intentar añadir un libro con el mismo título o ISBN
        Book duplicateBook = new Book("1984", "George Orwell", 1949, "Dystopian", "9780451524935");
        String response = bookController.addBook(duplicateBook);

        // Verificar que no se permite duplicados
        assertEquals("A book with the same title or ISBN already exists.", response);
    }

    @Test
    public void testGetAllBooks() {
        // Crear y añadir libros
        bookController.addBook(new Book("1984", "George Orwell", 1949, "Dystopian", "9780451524935"));
        bookController.addBook(new Book("To Kill a Mockingbird", "Harper Lee", 1960, "Classic", "9780061120084"));

        // Recuperar la lista de libros
        List<Book> bookList = bookController.getAllBooks();

        // Verificar que la lista contiene los libros añadidos
        assertEquals(2, bookList.size());
        assertTrue(bookList.stream().anyMatch(b -> b.getTitle().equals("1984")));
        assertTrue(bookList.stream().anyMatch(b -> b.getTitle().equals("To Kill a Mockingbird")));
    }

    @Test
    public void testGeneratePdf() {
        // Crear y añadir un libro
        Book book = new Book("1984", "George Orwell", 1949, "Dystopian", "9780451524935");
        bookController.addBook(book);

        // Generar PDF para el libro
        String response = bookController.generatePdf("1984");

        // Verificar la respuesta
        assertEquals("PDF generated for book: 1984", response);
    }

    @Test
    public void testGeneratePdfForNonExistentBook() {
        // Intentar generar un PDF para un libro inexistente
        String response = bookController.generatePdf("Nonexistent Book");

        // Verificar la respuesta
        assertEquals("Book not found!", response);
    }
}
