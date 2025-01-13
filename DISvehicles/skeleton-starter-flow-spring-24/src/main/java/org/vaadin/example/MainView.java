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

    private final Grid<Vehicles> vehicleGrid = new Grid<>(Vehicles.class, false);

    public MainView() {
        setSpacing(true); // Espaciado entre los componentes verticales
        setMargin(true); // Márgenes del layout

        // Crear componentes del formulario para cada campo
        TextField makeField = new TextField("Make");
        TextField modelField = new TextField("Model");
        TextField yearField = new TextField("Year of Manufacture");
        TextField typeField = new TextField("Type");
        TextField licensePlateField = new TextField("License Plate Number");

        // Botón para agregar un nuevo vehículo
        Button saveButton = new Button("Add Vehicle");

        // Layout del formulario
        HorizontalLayout formLayout = new HorizontalLayout(
            makeField, modelField, yearField, typeField, licensePlateField, saveButton
        );

        // Configurar el Grid para mostrar los vehículos
        vehicleGrid.addColumn(Vehicles::getMake).setHeader("Make");
        vehicleGrid.addColumn(Vehicles::getModel).setHeader("Model");
        vehicleGrid.addColumn(Vehicles::getYearOfManufacture).setHeader("Year of Manufacture");
        vehicleGrid.addColumn(Vehicles::getType).setHeader("Type");
        vehicleGrid.addColumn(Vehicles::getLicensePlateNumber).setHeader("License Plate Number");
        vehicleGrid.addColumn(Vehicles::getUuid).setHeader("UUID");

        // Obtener los datos iniciales desde el backend
        refreshGrid();

        // Acción para el botón de guardar
        saveButton.addClickListener(e -> {
            try {
                // Verificar que todos los campos estén llenos
                if (makeField.isEmpty() || modelField.isEmpty() || yearField.isEmpty() || typeField.isEmpty() || licensePlateField.isEmpty()) {
                    Notification.show("Please fill in all the fields!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                // Crear un nuevo vehículo con los datos del formulario
                Vehicles newVehicle = new Vehicles(
                    makeField.getValue(),
                    modelField.getValue(),
                    Integer.parseInt(yearField.getValue()),
                    typeField.getValue(),
                    licensePlateField.getValue()
                );

                // Enviar el nuevo vehículo al backend
                DataService.addVehicle(newVehicle);

                // Actualizar la lista desde el backend
                refreshGrid();

                // Notificación de éxito
                Notification.show("Vehicle added successfully!", 3000, Notification.Position.MIDDLE);

                // Limpiar los campos del formulario
                makeField.clear();
                modelField.clear();
                yearField.clear();
                typeField.clear();
                licensePlateField.clear();

            } catch (Exception ex) {
                Notification.show("Error adding vehicle: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });

        // Botón para exportar a CSV
        Button exportButton = new Button("Export to CSV");
        exportButton.addClickListener(event -> {
            try {
                // Llamar al servicio para exportar a CSV
                String resultMessage = DataService.exportToCsv();

                // Mostrar el resultado de la exportación
                Notification.show(resultMessage, 3000, Notification.Position.MIDDLE);
            } catch (Exception e) {
                Notification.show("Error exporting to CSV: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });

        // Agregar el formulario, la tabla y el botón al layout principal
        add(formLayout, vehicleGrid, exportButton);
    }

    // Refrescar el Grid obteniendo la lista actualizada de vehículos desde el backend
    private void refreshGrid() {
        try {
            List<Vehicles> vehiclesFromBackend = DataService.getVehicles(); // Llamar al backend para obtener la lista actualizada
            vehicleGrid.setItems(vehiclesFromBackend); // Mostrar los datos obtenidos en el Grid
        } catch (Exception e) {
            Notification.show("Error fetching vehicles: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }
}
