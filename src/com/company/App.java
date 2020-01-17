package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

    private ArrayList<Book> booksInLibrary = new ArrayList<>();
    private ArrayList<Customer> users = new ArrayList<>();
    private ArrayList<Librarian> librarians = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    public void start(){

        getBooksFromFile();
        getUsersFromFile();

        System.out.println("Välkommen till BiblioteksAppen!\n");
        System.out.println("Logga in eller skapa ett konto för att gå vidare.\n");
        System.out.println("Ange ett alternativ (0-2):");
        System.out.println("1) Logga in");
        System.out.println("2) Skapa konto\n");
        System.out.println("0) Avsluta appen");

        int selection = 99;

        try{
            selection = Integer.parseInt(scanner.nextLine());
        } catch(Exception e){
            System.out.println("Vänligen ange ett giltigt alternativ!");
        }

        switch (selection){
            case 1:
                login();
                break;
            case 2:
                createUser();
                break;
            case 0:
                System.exit(0);
                break;
            default:
                System.out.println("Vänligen ange ett giltigt alternativ!");
        }

        System.out.println("Välj ett alternativ (0-?):");
        System.out.println("");
        System.out.println("Välkommen till BiblioteksAppen!");
        System.out.println("Välkommen till BiblioteksAppen!");
        System.out.println("Välkommen till BiblioteksAppen!");
    }

    public void login(){
        String userName;
        String password;
        User currentUser = null;
        boolean keepLooping = true;
        do {
            System.out.print("Ange användarnamn:");
            userName = scanner.nextLine();
            if(users.size() > 0) {
                for (User user : users) {
                    if (user.getName().equals(userName)) {
                        currentUser = user;
                        keepLooping = false;
                        break;
                    }
                }
            }
            if (keepLooping) {
                System.out.println("\nAnvändaren finns inte i systemet!");
                System.out.println("Vänligen se över dina uppgifter eller skapa ett konto för att logga in.\n");
            }
        } while (keepLooping);

        do {
            System.out.print("Ange lösenord:");
            password = scanner.nextLine();
            if (currentUser.getPassword().equals(password)) {
                System.out.println("\nDu är nu inloggad!\n");
                break;
            }
                System.out.println("\nDu har angivit fel lösenord!");
        } while (true);
    }

    public void createUser() {

        String userName;
        String password;
        boolean keepLooping = false;

        do {
            System.out.print("Välj ditt användarnamn: ");
            userName = scanner.nextLine();
            if(users.size() > 0) {
                for (User user : users) {
                    if (user.getName().equals(userName)) {
                        System.out.println("Användarnamnet är inte tillgängligt!");
                        keepLooping = true;
                        break;
                    } else keepLooping = false;
                }
            } else keepLooping = false;
        } while (keepLooping);

        System.out.print("Välj ditt lösenord (minst 4 tecken): ");
        do {
            password = scanner.nextLine();
            if (password.length() > 4){
                break;
            } else {
                System.out.println("Ditt lösenord måste vara minst 4 tecken långt!");
            }
        } while (true);

        do {
            System.out.print("Upprepa ditt lösenord: ");
            if (password.equals(scanner.nextLine())) {
                break;
            } else {
                System.out.println("Du måste upprepa lösenordet du angav i förra steget!");
            }
        } while (true);

        users.add(new Customer(userName, password, "Kund"));
        FileUtils.saveObject("users.ser", users);
    }

    public void getUsersFromFile(){
        users = (ArrayList<Customer>)FileUtils.loadObject("users.ser");
        librarians = (ArrayList<Librarian>)FileUtils.loadObject("librarians.ser");
    }

    public void getBooksFromFile(){
        booksInLibrary = (ArrayList<Book>)FileUtils.loadObject("bookList.ser");
    }
}
