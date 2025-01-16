package es.ufv.dis.final2025.PBC;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final String FILE_PATH = "PBC/src/main/resources/datos.json";
    private final String EXPORT_FOLDER = "PBC/src/main/resources/exports";
    private List<Player> playerList = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public Player addPlayer(@RequestBody Player player) {
        player.setId(UUID.randomUUID().toString());
        loadPlayersFromFile();
        playerList.add(player);
        savePlayersToFile();
        return player;
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        loadPlayersFromFile();
        return playerList;
    }

    @GetMapping("/team-value")
    public double getTotalTeamValue() {
        loadPlayersFromFile();
        return playerList.stream()
                .mapToDouble(Player::getPlayerValue)
                .sum();
    }

    @DeleteMapping("/{id}")
    public String deletePlayer(@PathVariable String id) {
        loadPlayersFromFile();
        boolean removed = playerList.removeIf(player -> player.getId().equals(id));

        if (removed) {
            savePlayersToFile();
            return "Jugador con ID " + id + " eliminado correctamente.";
        } else {
            return "Jugador con ID " + id + " no encontrado.";
        }
    }

    @PutMapping("/{id}")
    public String updatePlayer(@PathVariable String id, @RequestBody Player updatedPlayer) {
        loadPlayersFromFile();
        for (Player player : playerList) {
            if (player.getId().equals(id)) {
                player.setName(updatedPlayer.getName());
                player.setNumber(updatedPlayer.getNumber());
                player.setPlayerValue(updatedPlayer.getPlayerValue());
                player.setPosition(updatedPlayer.getPosition());
                player.setMatchesPlayedThisSeason(updatedPlayer.getMatchesPlayedThisSeason());
                player.setPreviousTeams(updatedPlayer.getPreviousTeams());
                savePlayersToFile();
                return "Jugador con ID " + id + " actualizado correctamente.";
            }
        }
        return "Jugador con ID " + id + " no encontrado.";
    }

    private void ensureExportFolderExists() {
        Path path = Paths.get(EXPORT_FOLDER);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                System.err.println("Error al crear la carpeta de exportación ");
            }
        }
    }

    @GetMapping("/export")
    public String exportToCsv() {
        ensureExportFolderExists();

        String csvFilePath = EXPORT_FOLDER + "/players.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            writer.write("ID,Name,Number,PlayerValue,Position,MatchesPlayedThisSeason,PreviousTeams");
            writer.newLine();

            for (Player player : playerList) {
                writer.write(String.format("%s,%s,%d,%.2f,%s,%d,%s",
                        player.getId(),
                        player.getName(),
                        player.getNumber(),
                        player.getPlayerValue(),
                        player.getPosition(),
                        player.getMatchesPlayedThisSeason(),
                        String.join("|", player.getPreviousTeams())));
                writer.newLine();
            }

            return "CSV exportado correctamente";

        } catch (IOException e) {
            System.err.println("Error al exportar a CSV");
            return "Error al exportar a CSV.";
        }
    }

    @PostMapping("/generate-pdf")
    public String generatePdf(@RequestBody String playerName) {
        String cleanedName = playerName.trim().replace("\"", "");
        loadPlayersFromFile();

        try {
            // Buscar el jugador
            Player selectedPlayer = playerList.stream()
                    .filter(p -> p.getName().equalsIgnoreCase(cleanedName))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));

            // Crear el archivo PDF utilizando PDFManager
            File pdfFile = new File(EXPORT_FOLDER + "/" + cleanedName + ".pdf");
            PDFManager.generatePlayerPdf(selectedPlayer, pdfFile.getAbsolutePath());

            return "PDF generado correctamente ";

        } catch (RuntimeException e) {
            return e.getMessage();
        } catch (Exception e) {
            System.err.println("Error al generar el PDF: " + e.getMessage());
            return "Error al generar el PDF.";
        }
    }
    
    private void loadPlayersFromFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try {
                playerList = objectMapper.readValue(file, new TypeReference<List<Player>>() {});
            } catch (IOException e) {
                System.err.println("Error al cargar el archivo JSON");
            }
        }
    }

    private void savePlayersToFile() {
        try {
            objectMapper.writeValue(new File(FILE_PATH), playerList);
        } catch (IOException e) {
            System.err.println("Error al guardar en el archivo JSON ");
        }
    }
}