package com.example.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {

    private final String FILE_PATH = "./books.json"; // Ruta completa al archivo JSON
    private final String EXPORT_FOLDER = "exports"; // Carpeta de exportación para PDF
    private List<Book> bookList = new ArrayList<>(); // Lista de libros
    private final ObjectMapper objectMapper = new ObjectMapper(); // Mapper de JSON

    // Constructor: Carga los libros al inicio
    public BookController() {
        loadBooksFromFile(); // Carga los libros desde el archivo JSON al iniciar
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookList; // Devuelve los libros en memoria
    }

    @PostMapping("/add")
    public String addBook(@RequestBody Book newBook) {
        // Verifica si ya existe un libro con el mismo título o ISBN
        boolean exists = bookList.stream()
                .anyMatch(b -> b.getTitle().trim().equalsIgnoreCase(newBook.getTitle().trim()) ||
                               b.getISBN().trim().equalsIgnoreCase(newBook.getISBN().trim()));
        if (exists) {
            return "A book with the same title or ISBN already exists.";
        }

        // Generar ID automáticamente para el libro
        newBook.setId(UUID.randomUUID().toString());

        // Agregar el nuevo libro a la lista
        bookList.add(newBook);

        // Guarda los libros actualizados en el archivo JSON
        saveBooksToFile();
        return "Book added successfully: " + newBook.getTitle();
    }

    @PostMapping("/pdf")
    public String generatePdf(@RequestBody String bookTitle) {
        System.out.println("Received book title: " + bookTitle.trim());

        // Busca el libro (normalizando nombres para evitar problemas)
        Book book = bookList.stream()
                .filter(b -> b.getTitle().trim().equalsIgnoreCase(bookTitle.trim()))
                .findFirst()
                .orElse(null);

        if (book == null) {
            System.out.println("Book not found! Available books:");
            bookList.forEach(b -> System.out.println("Available: [" + b.getTitle() + "]"));
            return "Book not found!";
        }

        try {
            // Verifica y crea la carpeta de exportación si no existe
            File folder = new File(EXPORT_FOLDER);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Genera el archivo PDF
            String pdfPath = EXPORT_FOLDER + "/" + bookTitle.trim() + ".pdf";
            PDFManager.generateBookPdf(book, pdfPath);

            return "PDF generated for book: " + bookTitle.trim();
        } catch (Exception e) {
            System.err.println("Error generating PDF: " + e.getMessage());
            return "Error generating PDF for book: " + bookTitle.trim();
        }
    }

    private void loadBooksFromFile() {
        try {
            File file = new File(FILE_PATH);

            // Si el archivo no existe, crea un JSON vacío
            if (!file.exists()) {
                System.out.println("JSON file not found. Creating a new one...");
                saveBooksToFile(); // Crea el archivo JSON vacío
            } else {
                // Carga los datos del archivo JSON
                bookList = objectMapper.readValue(file, new TypeReference<List<Book>>() {});
                System.out.println("Loaded books: " + bookList.size());
                bookList.forEach(b -> System.out.println("Loaded book: [" + b.getTitle() + "]"));
            }
        } catch (IOException e) {
            System.err.println("Error loading books from JSON file: " + e.getMessage());
        }
    }

    private void saveBooksToFile() {
        try {
            // Guarda la lista de libros en el archivo JSON
            objectMapper.writeValue(new File(FILE_PATH), bookList);
            System.out.println("Books saved to JSON.");
        } catch (IOException e) {
            System.err.println("Error saving books to JSON file: " + e.getMessage());
        }
    }
}