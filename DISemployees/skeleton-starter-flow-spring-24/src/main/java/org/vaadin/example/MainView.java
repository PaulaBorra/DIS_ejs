package org.vaadin.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route
public class MainView extends VerticalLayout {

    private final Grid<Employee> employeeGrid = new Grid<>(Employee.class, false);

    public MainView() {
        setSpacing(true);
        setMargin(true);

        // Crear campos de entrada para agregar empleados
        TextField idField = new TextField("ID");
        TextField nameField = new TextField("Name");
        TextField departmentField = new TextField("Department");
        NumberField salaryField = new NumberField("Salary");
        TextField hireDateField = new TextField("Hire Date (YYYY-MM-DD)");

        Button addButton = new Button("Add Employee");
        Button exportButton = new Button("Export to CSV"); // Nuevo botón para exportar a CSV

        // Layout del formulario
        HorizontalLayout formLayout = new HorizontalLayout(
                idField, nameField, departmentField, salaryField, hireDateField, addButton
        );

        // Configurar la tabla (Grid)
        employeeGrid.addColumn(Employee::getId).setHeader("ID");
        employeeGrid.addColumn(Employee::getName).setHeader("Name");
        employeeGrid.addColumn(Employee::getDepartment).setHeader("Department");
        employeeGrid.addColumn(Employee::getSalary).setHeader("Salary");
        employeeGrid.addColumn(Employee::getHireDate).setHeader("Hire Date");

        // Agregar columna con botón para eliminar
        employeeGrid.addComponentColumn(employee -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(event -> {
                try {
                    String result = DataService.deleteEmployee(employee.getId());
                    Notification.show(result, 3000, Notification.Position.MIDDLE);
                    refreshGrid();
                } catch (Exception e) {
                    Notification.show("Error deleting employee: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
                }
            });
            return deleteButton;
        }).setHeader("Actions");

        // Acción del botón "Add Employee"
        addButton.addClickListener(event -> {
            try {
                if (idField.isEmpty() || nameField.isEmpty() || departmentField.isEmpty() || salaryField.isEmpty() || hireDateField.isEmpty()) {
                    Notification.show("Please fill in all fields!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                Employee newEmployee = new Employee(
                        Integer.parseInt(idField.getValue()),
                        nameField.getValue(),
                        departmentField.getValue(),
                        salaryField.getValue(),
                        hireDateField.getValue()
                );

                String result = DataService.addEmployee(newEmployee);
                Notification.show(result, 3000, Notification.Position.MIDDLE);
                refreshGrid();

                // Limpiar campos
                idField.clear();
                nameField.clear();
                departmentField.clear();
                salaryField.clear();
                hireDateField.clear();
            } catch (Exception e) {
                Notification.show("Error adding employee: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });

        // Acción del botón "Export to CSV"
        exportButton.addClickListener(event -> {
            try {
                String result = DataService.exportCsv();
                Notification.show(result, 3000, Notification.Position.MIDDLE);
            } catch (Exception e) {
                Notification.show("Error exporting to CSV: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });

        // Agregar componentes al layout principal
        add(formLayout, employeeGrid, exportButton);

        // Cargar datos iniciales
        refreshGrid();
    }

    // Actualizar los datos del Grid
    private void refreshGrid() {
        try {
            List<Employee> employees = DataService.getEmployees();
            employeeGrid.setItems(employees);
        } catch (Exception e) {
            Notification.show("Error loading employees: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }
}