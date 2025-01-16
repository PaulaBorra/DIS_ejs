package es.ufv.dis.final2025.PBC;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.util.List;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.FileOutputStream;

//para generar un pdf por jugador
/*public class PDFManager {

    public static void generatePlayerPdf(List<Player> players, String outputPath) throws Exception {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();
    
            document.add(new Paragraph("All Players Information", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK)));
            document.add(new Paragraph(" "));
    
            for (Player player : players) {
                document.add(new Paragraph("Name: " + player.getName()));
                document.add(new Paragraph("Number: " + player.getNumber()));
                document.add(new Paragraph("Player Value: " + player.getPlayerValue()));
                document.add(new Paragraph("Position: " + player.getPosition()));
                document.add(new Paragraph("Matches Played This Season: " + player.getMatchesPlayedThisSeason()));
                document.add(new Paragraph("Previous Teams: " + String.join(", ", player.getPreviousTeams())));
                document.add(new Paragraph(" ")); // Espaciado entre jugadores
            }
    
            document.close();
        } catch (Exception e) {
            System.err.println("Error generating PDF for all players: " + e.getMessage());
            throw e;
        }
    }
    
}*/
//para que se genere un pdf que contenga a todos los jugadores
public class PDFManager {

    /**
     * Genera un único PDF con la información de todos los jugadores.
     *
     * @param players    Lista de jugadores.
     * @param outputPath Ruta donde se guardará el archivo PDF.
     * @throws Exception Si ocurre algún error durante la generación del PDF.
     */
    public static void generateAllPlayersPdf(List<Player> players, String outputPath) throws Exception {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            // Título principal
            document.add(new Paragraph("All Players Information", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK)));
            document.add(new Paragraph(" "));

            // Añadir cada jugador
            for (Player player : players) {
                document.add(new Paragraph("Name: " + player.getName(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
                document.add(new Paragraph("Number: " + player.getNumber(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
                document.add(new Paragraph("Player Value: " + player.getPlayerValue(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
                document.add(new Paragraph("Position: " + player.getPosition(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
                document.add(new Paragraph("Matches Played This Season: " + player.getMatchesPlayedThisSeason(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
                document.add(new Paragraph("Previous Teams: " + String.join(", ", player.getPreviousTeams()), FontFactory.getFont(FontFactory.HELVETICA, 12)));
                document.add(new Paragraph(" ")); // Espaciado entre jugadores
                document.add(new LineSeparator()); // Línea de separación
                document.add(new Paragraph(" "));
            }

            document.close();
        } catch (Exception e) {
            System.err.println("Error al generar el PDF: " + e.getMessage());
            throw e;
        }
    }
}

