package org.vaadin.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route
public class MainView extends VerticalLayout {

    public MainView() {
        setSpacing(true);
        setMargin(true);

        // Crear los campos para el formulario
        ComboBox<String> employeeTypeField = new ComboBox<>("Type of Employee");
        NumberField productsSoldField = new NumberField("number of Monthly Sales");
        NumberField extraHoursField = new NumberField("Extra Hours Worked");

        // Crear los botones de cálculo
        Button calculatePreTaxButton = new Button("Calculate Pre-Tax Salary");
        Button calculatePostTaxButton = new Button("Calculate Post-Tax Salary");

        // Campos para mostrar el resultado
        VerticalLayout resultLayout = new VerticalLayout();

        // Obtener los tipos de empleados desde el backend
        try {
            List<String> employeeTypes = DataService.getEmployeeTypes();
            employeeTypeField.setItems(employeeTypes); // Asignar los valores al combo box.
        } catch (Exception e) {
            Notification.show("Error fetching employee types: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }

        // Acción para el botón de cálculo de salario bruto (pre-tax)
        calculatePreTaxButton.addClickListener(e -> {
            try {
                // Validar campos obligatorios
                if (employeeTypeField.isEmpty() || productsSoldField.isEmpty() || extraHoursField.isEmpty()) {
                    Notification.show("Please fill in all the fields!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                // Crear la entidad SalaryCalculation
                SalaryCalculation salaryCalc = new SalaryCalculation(
                        employeeTypeField.getValue(),
                        extraHoursField.getValue().intValue(),
                        productsSoldField.getValue().intValue()
                );

                // Calcular el salario bruto desde el backend
                Double preTaxSalary = DataService.calculatePreTaxSalary(salaryCalc);

                // Mostrar el resultado
                resultLayout.removeAll(); // Limpiar resultados previos
                resultLayout.add("Pre-tax salary: " + preTaxSalary);

            } catch (Exception ex) {
                Notification.show("Error calculating pre-tax salary: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });

        // Acción para el botón de cálculo de salario neto (post-tax)
        calculatePostTaxButton.addClickListener(e -> {
            try {
                // Validar campos obligatorios
                if (employeeTypeField.isEmpty() || productsSoldField.isEmpty() || extraHoursField.isEmpty()) {
                    Notification.show("Please fill in all the fields!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                // Crear la entidad SalaryCalculation
                SalaryCalculation salaryCalc = new SalaryCalculation(
                        employeeTypeField.getValue(),
                        extraHoursField.getValue().intValue(),
                        productsSoldField.getValue().intValue()
                );

                // Calcular el salario neto desde el backend
                Double postTaxSalary = DataService.calculatePostTaxSalary(salaryCalc);

                // Mostrar el resultado
                resultLayout.removeAll(); // Limpiar resultados previos
                resultLayout.add("Post-tax salary: " + postTaxSalary);

            } catch (Exception ex) {
                Notification.show("Error calculating post-tax salary: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });

        // Organizar los botones en un layout horizontal
        HorizontalLayout buttonLayout = new HorizontalLayout(calculatePreTaxButton, calculatePostTaxButton);

        // Agregar todos los componentes al layout principal
        add(employeeTypeField, productsSoldField, extraHoursField, buttonLayout, resultLayout);
    }
}
