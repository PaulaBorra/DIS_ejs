package com.example.demo;

import java.util.List;

public class Movies {
    private String id;
    private String title;
    private String director;
    private String year;
    private String genre;
    private List<String> cast;
    private String description;

    
    public Movies(String id, String title, String director, String year, String genre, List<String> cast,
            String description) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.year = year;
        this.genre = genre;
        this.cast = cast;
        this.description = description;
    }

    public Movies() {
        
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDirector() {
        return director;
    }
    public void setDirector(String director) {
        this.director = director;
    }
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public List<String> getCast() {
        return cast;
    }
    public void setCast(List<String> cast) {
        this.cast = cast;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}