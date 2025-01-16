package org.vaadin.example;

import java.util.List;
import java.util.UUID;

public class Player {
    private String id;
    private String name;
    private int number;
    private double playerValue;
    private String position;
    private int matchesPlayedThisSeason;
    private List<String> previousTeams;


    public Player(String name, int number, double playerValue, String position, int matchesPlayedThisSeason,
            List<String> previousTeams) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.number = number;
        this.playerValue = playerValue;
        this.position = position;
        this.matchesPlayedThisSeason = matchesPlayedThisSeason;
        this.previousTeams = previousTeams;
    }

    public Player() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getPlayerValue() {
        return playerValue;
    }

    public void setPlayerValue(double playerValue) {
        this.playerValue = playerValue;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getMatchesPlayedThisSeason() {
        return matchesPlayedThisSeason;
    }

    public void setMatchesPlayedThisSeason(int matchesPlayedThisSeason) {
        this.matchesPlayedThisSeason = matchesPlayedThisSeason;
    }

    public List<String> getPreviousTeams() {
        return previousTeams;
    }

    public void setPreviousTeams(List<String> previousTeams) {
        this.previousTeams = previousTeams;
    }

}