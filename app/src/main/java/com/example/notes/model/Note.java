package com.example.notes.model;

public class Note {
    private String title;
    private String description;
    private int id;
    private String email;
    public Note(String title, String description, String email){
        this.title = title;
        this.description = description;
        this.email = email;
    }
    public Note(int id,String title, String description, String email){
        this.id = id;
        this.title = title;
        this.description = description;
        this.email = email;
    }
    public Note(int id,String title, String description){
        this.id = id;
        this.title = title;
        this.description = description;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public int getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}