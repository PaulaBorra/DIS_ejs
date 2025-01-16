package es.ufv.dis.final2025.PBC;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerControllerTest {

    private PlayerController playerController;

    @BeforeEach
    public void setUp() {
        // Inicializa el controlador antes de cada prueba
        playerController = new PlayerController();
    }

    @Test
    public void testAddPlayer() {
        // Crear un nuevo jugador
        Player player = new Player("John Doe", 10, 500000.0, "Forward", 20, List.of("Team A", "Team B"));

        // Añadir jugador al controlador
        Player addedPlayer = playerController.addPlayer(player);

        // Verificar que el jugador tiene un ID generado
        assertNotNull(addedPlayer.getId());

        // Verificar que el jugador se ha añadido a la lista
        List<Player> playerList = playerController.getAllPlayers();
        assertTrue(playerList.contains(addedPlayer));
    }

    @Test
    public void testGetAllPlayers() {
        // Crear y añadir jugadores
        playerController.addPlayer(new Player("John Doe", 10, 500000.0, "Forward", 20, List.of("Team A")));
        playerController.addPlayer(new Player("Jane Smith", 11, 600000.0, "Midfielder", 25, List.of("Team C")));

        // Recuperar la lista de jugadores
        List<Player> playerList = playerController.getAllPlayers();

        // Verificar que la lista contiene los jugadores añadidos
        assertEquals(2, playerList.size());
    }

    @Test
    public void testDeletePlayer() {
        // Crear y añadir un jugador
        Player player = playerController.addPlayer(new Player("John Doe", 10, 500000.0, "Forward", 20, List.of("Team A")));
        String id = player.getId();

        // Eliminar el jugador
        String response = playerController.deletePlayer(id);

        // Verificar respuesta
        assertEquals("Jugador con ID " + id + " eliminado correctamente.", response);

        // Verificar que el jugador ya no está en la lista
        List<Player> playerList = playerController.getAllPlayers();
        assertFalse(playerList.stream().anyMatch(p -> p.getId().equals(id)));
    }

    @Test
    public void testUpdatePlayer() {
        // Crear y añadir un jugador
        Player player = playerController.addPlayer(new Player("John Doe", 10, 500000.0, "Forward", 20, List.of("Team A")));
        String id = player.getId();

        // Crear un jugador actualizado
        Player updatedPlayer = new Player("Johnny Doe", 9, 700000.0, "Striker", 30, List.of("Team B"));

        // Actualizar jugador
        String response = playerController.updatePlayer(id, updatedPlayer);

        // Verificar respuesta
        assertEquals("Jugador con ID " + id + " actualizado correctamente.", response);

        // Verificar que los datos se actualizaron
        Player updated = playerController.getAllPlayers().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);

        assertNotNull(updated);
        assertEquals("Johnny Doe", updated.getName());
        assertEquals(700000.0, updated.getPlayerValue());
    }

    @Test
    public void testExportToCsv() {
        // Crear y añadir jugadores
        playerController.addPlayer(new Player("Paula Garcia", 8, 450000.0, "Defender", 18, List.of("Team X")));
        playerController.addPlayer(new Player("Maria Gomez", 7, 300000.0, "Goalkeeper", 15, List.of("Team Y")));

        // Exportar a CSV
        String response = playerController.exportToCsv();

        // Verificar respuesta
        assertEquals("CSV exportado correctamente", response);
    }

    @Test
    public void testGetTotalTeamValue() {
        // Crear y añadir jugadores
        playerController.addPlayer(new Player("Player 1", 10, 100000.0, "Forward", 20, List.of("Team A")));
        playerController.addPlayer(new Player("Player 2", 11, 200000.0, "Midfielder", 25, List.of("Team B")));

        // Calcular el valor total del equipo
        double totalValue = playerController.getTotalTeamValue();

        // Verificar el valor total
        assertEquals(300000.0, totalValue);
    }

    @Test
    public void testExportToPdf() {
        // Crear y añadir un jugador
        playerController.addPlayer(new Player("Paula Garcia", 8, 450000.0, "Defender", 18, List.of("Team X")));

        // Exportar a PDF
        String response = playerController.generatePdf("Paula Garcia");

        // Verificar respuesta
        assertEquals("PDF generado correctamente ", response);
    }
}