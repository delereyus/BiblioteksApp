package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

    private ArrayList<Book> booksInLibrary = new ArrayList<>();
    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<Librarian> librarians = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    public void start(){

        getBooksFromFile();
        getUsersFromFile();

        User currentUser = null;

        do {
            System.out.println("Välkommen till BiblioteksAppen!\n");
            System.out.println("Logga in eller skapa ett konto för att gå vidare.\n");
            System.out.println("Ange ett alternativ (0-2):");
            System.out.println("1) Logga in");
            System.out.println("2) Skapa konto\n");
            System.out.println("0) Avsluta appen");

            int selection = 99;

            do {
                try {
                    selection = Integer.parseInt(scanner.nextLine());
                    break;
                } catch (Exception e) {
                    System.out.println("Vänligen ange ett giltigt alternativ!");
                }
            }while (true);

            switch (selection) {
                case 1:
                    currentUser = login();
                    break;
                case 2:
                    currentUser = createUser();
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Vänligen ange ett giltigt alternativ!");
            }

            if (currentUser.getUserRole().equals("Kund")) {
                loggedInAsCustomer(currentUser);
            } else loggedInAsLibrarian(currentUser);

        }while (true);
    }

    public void loggedInAsLibrarian(User user){
        do {
            System.out.println("Välj ett alternativ (0-?):\n");
            System.out.println("1) Se tillgängliga böcker");
            System.out.println("2) ");
            System.out.println("Välkommen till BiblioteksAppen!");
            System.out.println("Välkommen till BiblioteksAppen!");
        } while (true);
    }

    public void loggedInAsCustomer(User user){
        int selection = 99;

        do {
            System.out.println("Välj ett alternativ (0-?):\n");
            System.out.println("1) Se tillgängliga böcker");
            System.out.println("2) Sök på boktitel/författare");
            System.out.println("3) Visa mina lånade böcker");
            System.out.println("4) Lämna tillbaka böcker\n");
            System.out.println("0) Logga ut");

            try {
                selection = Integer.parseInt(scanner.nextLine());
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("\nVänligen ange ett giltigt alternativ!\n");
            }

            switch(selection){
                case 1:
                    showAvailableBooks();
                    break;
                case 2:
                    searchForBooks(user);
                    break;
                case 3:
                    showBorrowedBooks(user);
                    break;
                case 4:
                    returnBooks(user);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("\nVänligen ange ett giltigt alternativ!\n");
            }
        } while (true);
    }

    public void showBorrowedBooks(User user){

    }

    public void returnBooks(User user){

    }

    public void searchForBooks(User user) {
        String search;
        ArrayList<Book> searchResults = new ArrayList<>();
        int selection;

        do {
            System.out.println("Ange '0' för att återgå till huvudmenyn");
            System.out.print("Sök efter en boktitel/författare: ");

            do {
                search = scanner.nextLine();
                if (search.equals("0")) return;
                for (Book book : booksInLibrary) {
                    if (book.getAuthor().contains(search) || book.getTitle().contains(search)) {
                        searchResults.add(book);
                    }
                }
                if (searchResults.size() == 0) {
                    System.out.println("\nDin sökning gav inget resultat!\n");
                }
            } while (searchResults.size() < 1);

            for (int i = 0; i < searchResults.size(); i++) {
                System.out.println((i + 1) + ". " + searchResults.get(i));
            }
            System.out.println("Ange '0' för att återgå till huvudmenyn");
            System.out.print("\nFör att låna en bok, ange siffran till vänster om boken: ");

            do {
                try {
                    selection = Integer.parseInt(scanner.nextLine());
                    if (selection < 0 || selection > searchResults.size() + 1) {
                        throw new IndexOutOfBoundsException();
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("\nVänligen ange ett giltigt alternativ!\n");
                }
            } while (true);

            if (selection == 0) return;

            for (int i = 0; i < searchResults.size(); i++) {
                if (selection == (i + 1)) {
                    user.setBorrowedBooks(searchResults.get(i));
                    for (Book book : booksInLibrary){
                        if (book.getTitle().equals(searchResults.get(i).getTitle())){
                            book.setCurrentReader(user);
                            book.setAvailable(false);
                        }
                    }
                }
            }
            FileUtils.saveObject("books", booksInLibrary);
        }while (true);
    }

    public void showAvailableBooks(){
        for (Book book : booksInLibrary){
            if (book.isAvailable()) {
                System.out.println(book);
            }
        }
    }

    public User login(){
        String userName;
        String password;
        User currentUser = null;
        boolean keepLooping = true;

        ArrayList<User> users = new ArrayList<User>();

        for (Customer customer : customers){
            users.add(customer);
        }
        for (Librarian librarian : librarians){
            users.add(librarian);
        }

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

        return currentUser;
    }

    public Customer createUser() {

        String userName;
        String password;
        boolean keepLooping = false;
        Customer newUser = null;

        do {
            System.out.print("Välj ditt användarnamn: ");
            userName = scanner.nextLine();
            if(customers.size() > 0) {
                for (Customer customer : customers) {
                    if (customer.getName().equals(userName)) {
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

        newUser = new Customer(userName, password, "Kund");
        customers.add(newUser);
        FileUtils.saveObject("users.ser", customers);

        return newUser;
    }

    public void getUsersFromFile(){
        customers = (ArrayList<Customer>)FileUtils.loadObject("users.ser");
        librarians = (ArrayList<Librarian>)FileUtils.loadObject("librarians.ser");
    }

    public void getBooksFromFile(){
        booksInLibrary = (ArrayList<Book>)FileUtils.loadObject("bookList.ser");
    }
}
