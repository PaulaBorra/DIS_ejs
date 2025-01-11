package com.example.demo;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

public class PDFManager {

    public static void generateBookPdf(Book book, String filePath) {
        try {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));
            doc.open();
            doc.add(new Paragraph("Book Details:"));
            doc.add(new Paragraph("Title: " + book.getTitle()));
            doc.add(new Paragraph("Author: " + book.getAuthor()));
            doc.add(new Paragraph("Year: " + book.getYear()));
            doc.add(new Paragraph("Genre: " + book.getGenre()));
            doc.add(new Paragraph("ISBN: " + book.getISBN()));
            doc.close();
        } catch (Exception e) {
            System.err.println("Error creating PDF: " + e.getMessage());
        }
    }    
    
}