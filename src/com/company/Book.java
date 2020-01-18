package com.company;

import java.io.Serializable;

public class Book implements Serializable, Comparable< Book >{

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
        return "Författare: " + author + ", " + "Boktitel: " + title;
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

    @Override
    public int compareTo(Book o) {
        if (this.getAuthor().compareTo(o.getAuthor()) == 0){
            return this.getTitle().compareTo(o.getTitle());
        } else return this.getAuthor().compareTo(o.getAuthor());
    }
}
