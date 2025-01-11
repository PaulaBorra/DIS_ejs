package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SalaryControllerTest {

    private SalaryController salaryController;

    @BeforeEach
    void setUp() {
        // Inicializa el controlador antes de cada prueba
        salaryController = new SalaryController();
    }

    @Test
    void testPreTaxSalaryForManager() {
        // Crea una instancia de SalaryCalculation para un Manager
        SalaryCalculation manager = new SalaryCalculation("manager", 10, 1200);

        // Llama al método preTax
        double preTaxSalary = salaryController.preTax(manager);

        // Verifica que el salario pre-impuestos es correcto
        assertEquals(10000.0 + (10 * 50.0) + 100.0, preTaxSalary);
    }

    @Test
    void testPreTaxSalaryForSeller() {
        // Crea una instancia de SalaryCalculation para un Seller
        SalaryCalculation seller = new SalaryCalculation("seller", 15, 1600);

        // Llama al método preTax
        double preTaxSalary = salaryController.preTax(seller);

        // Verifica que el salario pre-impuestos es correcto
        assertEquals(5000.0 + (15 * 20.0) + 200.0, preTaxSalary);
    }

    @Test
    void testPostTaxSalaryForLowIncome() {
        // Crear empleado tipo "seller" sin horas extra ni productos vendidos
        SalaryCalculation employee = new SalaryCalculation("seller", 0, 0);

        // Salario pre-tax: baseSalary = 5000.0
        // No hay bonus por productos vendidos ni por horas extra.
        double expectedPreTaxSalary = 5000.0;

        // Salario post-tax: <= 1000 (no aplica impuestos)
        double expectedPostTaxSalary = expectedPreTaxSalary;

        // Calcular salario post-tax
        double postTaxSalary = salaryController.postTax(employee);

        // Verificar salario
        assertEquals(expectedPostTaxSalary, postTaxSalary);
    }

    @Test
    void testPostTaxSalaryForMediumIncome() {
        // Crear empleado tipo "seller" con 10 horas extra y sin productos vendidos
        SalaryCalculation employee = new SalaryCalculation("seller", 10, 0);

        // Salario pre-tax:
        // baseSalary = 5000.0
        // extraHoursBonus = 10 * 20.0 = 200.0
        double expectedPreTaxSalary = 5000.0 + 200.0;

        // Salario post-tax: se aplica 16% de impuestos (rango <= 1500)
        double expectedPostTaxSalary = expectedPreTaxSalary * 0.84;

        // Calcular salario post-tax
        double postTaxSalary = salaryController.postTax(employee);

        // Verificar salario
        assertEquals(expectedPostTaxSalary, postTaxSalary);
    }

    @Test
    void testPostTaxSalaryForHighIncome() {
        // Crear empleado tipo "manager" con 20 horas extra y 1600 productos vendidos
        SalaryCalculation employee = new SalaryCalculation("manager", 20, 1600);

        // Salario pre-tax:
        // baseSalary = 10000.0
        // productBonus = 200.0 (por más de 1500 productos)
        // extraHoursBonus = 20 * 50.0 = 1000.0
        double expectedPreTaxSalary = 10000.0 + 200.0 + 1000.0;

        // Salario post-tax: se aplica 18% de impuestos (rango > 1500)
        double expectedPostTaxSalary = expectedPreTaxSalary * 0.82;

        // Calcular salario post-tax
        double postTaxSalary = salaryController.postTax(employee);

        // Verificar salario
        assertEquals(expectedPostTaxSalary, postTaxSalary);
    }

    @Test
    void testInvalidEmployeeType() {
        // Crea una instancia de SalaryCalculation con un tipo de empleado inválido
        SalaryCalculation invalidEmployee = new SalaryCalculation("invalid", 0, 0);

        // Verifica que se lanza una excepción al calcular el salario pre-impuestos
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            salaryController.preTax(invalidEmployee);
        });

        // Verifica el mensaje de la excepción
        assertEquals("Invalid employee type: invalid", exception.getMessage());
    }
}
