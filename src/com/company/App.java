package com.company;

import java.util.ArrayList;
import java.util.List;

public class App {

    private ArrayList<Book> booksInLibrary = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();

    public void start(){
        getBooksFromFile();
        getUsersFromFile();

        
    }

    public void createUser(String userName, String password) {
        assert users != null;
        for (User user : users) {
            if (user.getName().equals(userName)) {
                System.out.println("Användarnamnet är inte tillgängligt!");
                return;
            } else {
                users.add(new Customer(userName, password, "Kund"));
                FileUtils.saveObject("users.ser", users);
            }
        }
    }

    public void getUsersFromFile(){
        users = (ArrayList<User>)FileUtils.loadObject("users.ser");
    }

    public void getBooksFromFile(){
        booksInLibrary = (ArrayList<Book>)FileUtils.loadObject("bookList.ser");
    }
}
