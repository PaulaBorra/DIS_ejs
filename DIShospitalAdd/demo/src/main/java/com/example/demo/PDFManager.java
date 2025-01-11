package com.example.demo;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

public class PDFManager {

    public static void generateHospitalPdf(Hospital hospital, String outputPath) throws Exception {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();
            document.add(new Paragraph("Hospital Information"));
            document.add(new Paragraph("Name: " + hospital.getHospitalName()));
            document.add(new Paragraph("Location: " + hospital.getLocation()));
            document.add(new Paragraph("Registration Number: " + hospital.getRegistrationNumber()));
            document.add(new Paragraph("Capacity: " + hospital.getCapacity()));
            document.add(new Paragraph("Departments: " + hospital.getDepartments()));
            document.add(new Paragraph("Contact Number: " + hospital.getContactNumber()));
            document.add(new Paragraph("Emergency Services: " + hospital.getEmergencyServices()));
            document.add(new Paragraph("Hospital Type: " + hospital.getHospitalType()));
            document.add(new Paragraph("Number of Doctors: " + (hospital.getDoctors() != null ? hospital.getDoctors().size() : 0)));
            document.add(new Paragraph("Number of Patients: " + (hospital.getPatients() != null ? hospital.getPatients().size() : 0)));
            document.close();
        } catch (Exception e) {
            System.err.println("Error generating PDF: " + e.getMessage());
            throw e;
        }
    }
}
