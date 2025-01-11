package org.vaadin.example;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
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

        // Formulario para agregar nuevos hospitales
        add(createAddHospitalForm());

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

    private HorizontalLayout createAddHospitalForm() {
        TextField nameField = new TextField("Hospital Name");
        TextField locationField = new TextField("Location");
        TextField regNumberField = new TextField("Registration Number");
        TextField capacityField = new TextField("Capacity"); 
        TextField departmentsField = new TextField("Departments (comma-separated)"); 
    
        Button addButton = new Button("Add Hospital", click -> {
            try {
                // Crear un nuevo objeto Hospital
                Hospital hospital = new Hospital();
                hospital.setHospitalName(nameField.getValue().trim());
                hospital.setLocation(locationField.getValue().trim());
                hospital.setRegistrationNumber(regNumberField.getValue().trim());
                hospital.setCapacity(capacityField.getValue().trim()); // Se asigna como String
                hospital.setDepartments(departmentsField.getValue().trim()); // Se asigna como String
    
                // Enviar al backend
                String response = dataService.addHospital(hospital);
    
                // Mostrar notificación
                Notification.show(response, 3000, Notification.Position.MIDDLE);
    
                // Recargar la tabla
                loadHospitals();
            } catch (Exception e) {
                Notification.show("Error adding hospital: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });
    
        HorizontalLayout formLayout = new HorizontalLayout(nameField, locationField, regNumberField, capacityField, departmentsField, addButton);
        formLayout.setWidthFull();
        formLayout.setAlignItems(Alignment.BASELINE);
        return formLayout;
    }
    
}