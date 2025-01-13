package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VehiclesControllerTest {

    private VehiclesController vehiclesController;

    @BeforeEach
    public void setUp() {
        // Inicializa el controlador antes de cada prueba
        vehiclesController = new VehiclesController();
    }

    @Test
    public void testAddVehicle() {
        // Crear un nuevo vehículo
        Vehicles vehicle = new Vehicles("Toyota", "Camry", 2020, "Sedan", "ABC-1234");

        // Añadir vehículo al controlador
        Vehicles addedVehicle = vehiclesController.addVehicle(vehicle);

        // Verificar que el vehículo tiene un UUID generado
        assertNotNull(addedVehicle.getUuid());

        // Verificar que el vehículo se ha añadido a la lista
        List<Vehicles> vehiclesList = vehiclesController.getAllVehicles();
        assertTrue(vehiclesList.contains(addedVehicle));
    }

    @Test
    public void testGetAllVehicles() {
        // Crear y añadir vehículos
        vehiclesController.addVehicle(new Vehicles("Toyota", "Camry", 2020, "Sedan", "ABC-1234"));
        vehiclesController.addVehicle(new Vehicles("Ford", "F-150", 2018, "Truck", "DEF-5678"));

        // Recuperar la lista de vehículos
        List<Vehicles> vehiclesList = vehiclesController.getAllVehicles();

        // Verificar que la lista contiene los vehículos añadidos
        assertEquals(2, vehiclesList.size());
    }

    @Test
    public void testExportToCsv() {
        // Crear y añadir vehículos
        vehiclesController.addVehicle(new Vehicles("Toyota", "Camry", 2020, "Sedan", "ABC-1234"));
        vehiclesController.addVehicle(new Vehicles("Ford", "F-150", 2018, "Truck", "DEF-5678"));

        // Exportar a CSV
        String response = vehiclesController.exportToCsv();

        // Verificar respuesta
        assertEquals("CSV exportado correctamente", response);

        // No se verifica el archivo CSV directamente en este test unitario, 
        // porque requiere pruebas de integración para validar la salida del archivo.
    }
}
