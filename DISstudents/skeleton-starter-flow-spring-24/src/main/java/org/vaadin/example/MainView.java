package org.vaadin.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route
public class MainView extends VerticalLayout {

    private final Grid<Students> studentGrid = new Grid<>(Students.class, false);

    public MainView() {
        setSpacing(true); //espacio entre los componentes verticales
        setMargin(true); //margenes del layout

        // Crea componentes del formulario para cada campo
        TextField firstNameField = new TextField("First Name");
        TextField lastNameField = new TextField("Last Name");
        DatePicker dobField = new DatePicker("Date of Birth");

        //boton para seleccionar el genero
        RadioButtonGroup<String> genderField = new RadioButtonGroup<>();
        genderField.setLabel("Gender");
        genderField.setItems("Male", "Female");

        //boton para agregar un nuevo estudiante
        Button saveButton = new Button("Add Student");

        // Layout del formulario
        HorizontalLayout formLayout = new HorizontalLayout(
            firstNameField, lastNameField, dobField, genderField, saveButton
        );

        // Configura el Grid para mostrar los estudiantes
        studentGrid.addColumn(Students::getFirstName).setHeader("First Name");
        studentGrid.addColumn(Students::getLastName).setHeader("Last Name");
        studentGrid.addColumn(Students::getDateOfBirth).setHeader("Date of Birth");
        studentGrid.addColumn(Students::getGender).setHeader("Gender");
        studentGrid.addColumn(Students::getUuid).setHeader("UUID");

        // Obtener los datos iniciales desde el backend
        refreshGrid();

        // Acción para el botón de guardar
        saveButton.addClickListener(e -> {
            try {
                // Verifica que todos los campos estén llenos
                if (firstNameField.isEmpty() || lastNameField.isEmpty() || dobField.isEmpty() || genderField.isEmpty()) {
                    Notification.show("Please fill in all the fields!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                // Crea un nuevo estudiante con los datos del formulario
                Students newStudent = new Students(
                    firstNameField.getValue(),
                    lastNameField.getValue(),
                    dobField.getValue() != null ? Integer.parseInt(dobField.getValue().toString().replaceAll("-", "")) : 0, //convierte la fecha a un entero
                    genderField.getValue()
                );

                // Envia el nuevo estudiante al backend
                DataService.addStudent(newStudent);

                // Actualiza la lista desde el backend
                refreshGrid();

                // Notificación de éxito
                Notification.show("Student added successfully!", 3000, Notification.Position.MIDDLE);

                // Limpia los campos del formulario de los diferentes campos
                firstNameField.clear();
                lastNameField.clear();
                dobField.clear();
                genderField.clear();

            } catch (Exception ex) {
                Notification.show("Error adding student: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });

        // Botón para exportar a CSV
        Button exportButton = new Button("Export to CSV");
        exportButton.addClickListener(event -> { //que pasa al hacer click
            try {
                // Llama al servicio para exportar a CSV
                String resultMessage = DataService.exportToCsv();

                // Muestrs el resultado de la exportación
                Notification.show(resultMessage, 3000, Notification.Position.MIDDLE);
            } catch (Exception e) {
                Notification.show("Error exporting to CSV: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });

        // Agrega el formulario, la tabla y el boton al layout principal
        add(formLayout, studentGrid, exportButton);
    }

    // Refresca el Grid obteniendo la lista actualizada de estudiantes desde el backend.
    private void refreshGrid() {
        try {
            List<Students> studentsFromBackend = DataService.getStudents(); //llama al backend para obtener la lista actualizada 
            studentGrid.setItems(studentsFromBackend); //Muestra los datos obtenidos en el Grid.
        } catch (Exception e) {
            Notification.show("Error fetching students: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }
}
