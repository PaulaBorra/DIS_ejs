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

    private final Grid<Player> playerGrid = new Grid<>(Player.class, false);
    private TextField nameField = new TextField("Name");
    private TextField numberField = new TextField("Number");
    private TextField valueField = new TextField("Player Value");
    private TextField positionField = new TextField("Position");
    private TextField matchesField = new TextField("Matches Played This Season");
    private TextField previousTeamsField = new TextField("Previous Teams (comma separated)");
    private Button saveButton = new Button("Add Player");
    private Button exportPdfButton = new Button("Export All to PDF");
    private String editingPlayerId = null;

    public MainView() {
        setSpacing(true);
        setMargin(true);

        // Formulario
        HorizontalLayout formLayout = new HorizontalLayout(
                nameField, numberField, valueField, positionField, matchesField, previousTeamsField, saveButton
        );

        // Configurar Grid
        playerGrid.addColumn(Player::getName).setHeader("Name");
        playerGrid.addColumn(Player::getNumber).setHeader("Number");
        playerGrid.addColumn(Player::getPlayerValue).setHeader("Player Value");
        playerGrid.addColumn(Player::getPosition).setHeader("Position");
        playerGrid.addColumn(Player::getMatchesPlayedThisSeason).setHeader("Matches Played");
        playerGrid.addColumn(Player::getPreviousTeams).setHeader("Previous Teams");

        // Botón de editar
        playerGrid.addComponentColumn(player -> {
            Button editButton = new Button("Edit", e -> {
                editingPlayerId = player.getId();
                nameField.setValue(player.getName());
                numberField.setValue(String.valueOf(player.getNumber()));
                valueField.setValue(String.valueOf(player.getPlayerValue()));
                positionField.setValue(player.getPosition());
                matchesField.setValue(String.valueOf(player.getMatchesPlayedThisSeason()));
                previousTeamsField.setValue(String.join(", ", player.getPreviousTeams()));
                saveButton.setText("Update Player");
            });
            return editButton;
        }).setHeader("Edit");

        // Botón de eliminar
        playerGrid.addComponentColumn(player -> {
            Button deleteButton = new Button("Delete", e -> {
                try {
                    DataService.deletePlayer(player.getId());
                    refreshGrid();
                    Notification.show("Player deleted successfully.", 3000, Notification.Position.MIDDLE);
                } catch (Exception ex) {
                    Notification.show("Error deleting player: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                }
            });
            return deleteButton;
        }).setHeader("Delete");

        // Botón de exportar a CSV para cada jugador
        playerGrid.addComponentColumn(player -> {
            Button csvButton = new Button("Export to CSV", e -> {
                try {
                    DataService.exportToCsvByIds(List.of(player.getId()));
                    Notification.show("Player exported to CSV successfully.", 3000, Notification.Position.MIDDLE);
                } catch (Exception ex) {
                    Notification.show("Error exporting player to CSV: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                }
            });
            return csvButton;
        }).setHeader("Export to CSV");

        // Acción del botón de guardar
        saveButton.addClickListener(e -> {
            try {
                if (nameField.isEmpty() || numberField.isEmpty() || valueField.isEmpty() || positionField.isEmpty() || matchesField.isEmpty()) {
                    Notification.show("Please fill in all the fields!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                Player player = new Player(
                        nameField.getValue(),
                        Integer.parseInt(numberField.getValue()),
                        Double.parseDouble(valueField.getValue()),
                        positionField.getValue(),
                        Integer.parseInt(matchesField.getValue()),
                        List.of(previousTeamsField.getValue().split(","))
                );

                if (editingPlayerId == null) {
                    DataService.addPlayer(player);
                    Notification.show("Player added successfully!", 3000, Notification.Position.MIDDLE);
                } else {
                    player.setId(editingPlayerId);
                    DataService.updatePlayer(player);
                    Notification.show("Player updated successfully!", 3000, Notification.Position.MIDDLE);
                    saveButton.setText("Add Player");
                    editingPlayerId = null;
                }

                refreshGrid();
                clearForm();

            } catch (Exception ex) {
                Notification.show("Error saving player: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });

        // Acción del botón de exportar todos a PDF
        /*exportPdfButton.addClickListener(e -> {
            try {
                String resultMessage = DataService.exportAllToPdf();
                Notification.show(resultMessage, 3000, Notification.Position.MIDDLE);
            } catch (Exception ex) {
                Notification.show("Error exporting all to PDF: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });*/
        
        exportPdfButton.addClickListener(e -> {
            try {
                String resultMessage = DataService.exportAllToPdf();
                Notification.show(resultMessage, 3000, Notification.Position.MIDDLE);
            } catch (Exception ex) {
                Notification.show("Error exporting all to PDF: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });
        

        // Añadir componentes al layout principal
        add(formLayout, playerGrid, exportPdfButton);

        // Refrescar datos iniciales
        refreshGrid();
    }

    private void refreshGrid() {
        try {
            List<Player> playersFromBackend = DataService.getAllPlayers();
            playerGrid.setItems(playersFromBackend);
        } catch (Exception e) {
            Notification.show("Error fetching players: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void clearForm() {
        nameField.clear();
        numberField.clear();
        valueField.clear();
        positionField.clear();
        matchesField.clear();
        previousTeamsField.clear();
    }
}