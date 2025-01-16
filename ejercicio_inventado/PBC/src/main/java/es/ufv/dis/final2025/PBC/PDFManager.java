package es.ufv.dis.final2025.PBC;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class PDFManager {

    public static void generatePlayerPdf(Player player, String outputPath) throws Exception {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            document.add(new Paragraph("Player Information", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK)));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Name: " + player.getName()));
            document.add(new Paragraph("Number: " + player.getNumber()));
            document.add(new Paragraph("Player Value: " + player.getPlayerValue()));
            document.add(new Paragraph("Position: " + player.getPosition()));
            document.add(new Paragraph("Matches Played This Season: " + player.getMatchesPlayedThisSeason()));
            document.add(new Paragraph("Previous Teams: " + String.join(", ", player.getPreviousTeams())));

            document.close();
        } catch (Exception e) {
            System.err.println("Error generating PDF: " + e.getMessage());
            throw e;
        }
    }
}
