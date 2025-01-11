package org.vaadin.example;


import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {

    private final DataService dataService = new DataService();
    private final Grid<Hospital> grid = new Grid<>(Hospital.class);

    public MainView() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        addClassName("main-view");

        // Configurar la tabla
        configureGrid();

        // Cargar los datos
        loadHospitals();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("hospitalName", "location", "registrationNumber", "capacity", "departments");
        grid.addComponentColumn(hospital -> createGeneratePdfButton(hospital))
                .setHeader("Actions")
                .setAutoWidth(true);

        add(grid);
    }

    private void loadHospitals() {
        try {
            List<Hospital> hospitals = dataService.fetchHospitals();
            grid.setItems(hospitals);
        } catch (Exception e) {
            Notification.show("Error loading hospitals: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private Button createGeneratePdfButton(Hospital hospital) {
        return new Button("Generate PDF", click -> {
            try {
                String response = dataService.generatePdf(hospital.getHospitalName());
                Notification.show(response, 3000, Notification.Position.MIDDLE);
            } catch (Exception e) {
                Notification.show("Error generating PDF: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });
    }
}