package com.company;

import java.io.Serializable;

public class Book implements Serializable {

    private String title;
    private String author;
    private boolean available = true;
    private User currentReader;

    public Book(String title, String author){
        this.title = title;
        this.author = author;
    }

    @Override
    public String toString() {
        return "Author: " + author + ", " + "Title: " + title;
    }

    public User getCurrentReader(){
        return currentReader;
    }

    public void setCurrentReader(User reader){
        this.currentReader = reader;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
