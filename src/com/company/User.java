package com.company;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private String name;
    private String password;
    private String userRole;
    private ArrayList<Book> borrowedBooks = new ArrayList<>();

    public User(String name, String password, String userRole){
        this.name = name;
        this.password = password;
        this.userRole = userRole;
    }

    public ArrayList<Book> getBorrowedBooks(){
        return borrowedBooks;
    }

    public void setBorrowedBooks(Book book){
        borrowedBooks.add(book);
    }

    public void returnBook(Book book){
        borrowedBooks.remove(book);
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getUserRole() {
        return userRole;
    }

    public String toString(){
        return name + ", " + userRole;
    }

}
