package com.company;

public class Book {

    private String title;
    private String author;
    private boolean available;
    private User currentReader;

    public Book(String title, String author, boolean available){
        this.title = title;
        this.author = author;
        this.available = available;
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
