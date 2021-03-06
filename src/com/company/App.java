package com.company;

import java.io.File;
import java.lang.reflect.Array;
import java.util.*;

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
            System.out.println("\nVälkommen till BiblioteksAppen!\n");
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
                    System.out.println("Vänligen ange ett giltigt alternativ!\n");
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
                    System.out.println("Vänligen ange ett giltigt alternativ!\n");
            }

            if (currentUser.getUserRole().equals("Kund")) {
                loggedInAsCustomer(currentUser);
            } else loggedInAsLibrarian(currentUser);

        }while (true);
    }

    public void loggedInAsCustomer(User user){
        int selection = 99;

        do {
            System.out.println("\nVälj ett alternativ (0-4):\n");
            System.out.println("1) Visa tillgängliga böcker");
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
                    showAvailableBooks(user);
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

    public void loggedInAsLibrarian(User user) {
        int selection = 99;

        do {
            System.out.println("\nVälj ett alternativ (0-6):\n");
            System.out.println("1) Visa alla böcker samt tillgänglighet");
            System.out.println("2) Visa alla utlånade böcker");
            System.out.println("3) Lägg till böcker");
            System.out.println("4) Ta bort böcker");
            System.out.println("5) Visa alla användare");
            System.out.println("6) Sök efter användare\n");
            System.out.println("0) Logga ut");

            try {
                selection = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("\nVänligen ange ett giltigt alternativ!\n");
            }

            switch (selection) {
                case 1:
                    showAllBooksAndStatus();
                    break;
                case 2:
                    showAllBorrowedBooks();
                    break;
                case 3:
                    addBooks();
                    break;
                case 4:
                    removeBooks();
                    break;
                case 5:
                    showAllUsers();
                    break;
                case 6:
                    searchForUser();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("\nVänligen ange ett giltigt alternativ!\n");
            }
        } while (true);
    }

    public void showAllBorrowedBooks(){

        ArrayList<Book> borrowedBooks = new ArrayList<>();

        for (Book book : booksInLibrary){
            if (!book.isAvailable()){
                borrowedBooks.add(book);
            }
        }

        if (borrowedBooks.size() < 1){
            System.out.println("\nIngen har lånat några böcker!\n");
            return;
        }

        for (int i = 0; i < borrowedBooks.size(); i++){
            System.out.println((i + 1) + ". " + borrowedBooks.get(i) + ", Utlånad till: " + borrowedBooks.get(i).getCurrentReader().getName());
        }
    }

    public void addBooks(){
        System.out.println("\nAnge författarens namn: ");
        String author = scanner.nextLine();
        System.out.println("Ange boktiteln: ");
        String title = scanner.nextLine();

        booksInLibrary.add(new Book(title, author));

        FileUtils.saveObject("bookList.ser", booksInLibrary);

        System.out.println("\nDu har lagt till " + title + " av " + author + " i biblioteket!");
    }

    public void removeBooks(){
        showAllBooksAndStatus();

        System.out.println("\nAnge '0' för att återgå till huvudmenyn");
        System.out.println("Ange siffran till vänster om en bok för att ta bort den ur biblioteket!");

        int selection = 99;

        do{
            try{
                selection = Integer.parseInt(scanner.nextLine());
                if (selection < 0 || selection > booksInLibrary.size() + 1){
                    throw new IndexOutOfBoundsException();
                }
                break;
            }catch(Exception e){
                System.out.println("\nVänligen ange ett giltigt alternativ!\n");
            }
        }while (true);

        if (selection == 0) return;

        for (Customer customer : customers){
            for (Book book : customer.getBorrowedBooks()){
                if (book.getTitle().equals(booksInLibrary.get(selection-1).getTitle())){
                    customer.returnBook(book);
                    break;
                }
            }
        }

        System.out.println("\nDu tog bort " + booksInLibrary.get(selection-1).getTitle() + " av " + booksInLibrary.get(selection-1).getAuthor() + " ur biblioteket!");
        booksInLibrary.remove(booksInLibrary.get(selection - 1));

        FileUtils.saveObject("bookList.ser", booksInLibrary);
    }

    public void showAllUsers() {

        int selection;

        do {
            for (int i = 0; i < customers.size(); i++) {
                System.out.println((i + 1) + ". " + customers.get(i).getName());
            }

            System.out.println("\nAnge '0' för att återgå till huvudmenyn");
            System.out.print("Ange siffran till vänster om en användare för att se deras lånade böcker: ");
            do {
                try {
                    selection = Integer.parseInt(scanner.nextLine());
                    if (selection < 0 || selection > customers.size() + 1) {
                        throw new IndexOutOfBoundsException();
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("\nVänligen ange ett giltigt alternativ!\n");
                }
            } while (true);

            if (selection == 0) return;

            for (int i = 0; i < customers.size(); i++) {
                if (selection == (i + 1)) {
                    if (customers.get(i).getBorrowedBooks().size() < 1) {
                        System.out.println("\nAnvändaren har inte lånat några böcker!\n");
                        break;
                    }
                    System.out.println();
                    for (Book book : customers.get(i).getBorrowedBooks()){
                        System.out.println(book);
                    }
                    System.out.println();
                }
            }

        } while (true);
    }

    public void searchForUser() {
        System.out.println("\nAnge '0' för att återgå till huvudmenyn");
        System.out.print("Ange ett användarnamn att söka efter: ");

        String search = scanner.nextLine();

        if (search.equals("0")) return;

        ArrayList<Customer> searchResult = new ArrayList<>();

        for (Customer customer : customers) {
            if (customer.getName().toLowerCase().contains(search.toLowerCase())) {
                searchResult.add(customer);
            }
        }

        int selection;

        do {
            for (int i = 0; i < searchResult.size(); i++) {
                System.out.println((i + 1) + ". " + searchResult.get(i).getName());
            }

            System.out.println("\nAnge '0' för att återgå till huvudmenyn");
            System.out.print("Ange siffran till vänster om en användare för att se deras lånade böcker: ");
            do {
                try {
                    selection = Integer.parseInt(scanner.nextLine());
                    if (selection < 0 || selection > searchResult.size() + 1) {
                        throw new IndexOutOfBoundsException();
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("\nVänligen ange ett giltigt alternativ!\n");
                }
            } while (true);

            if (selection == 0) return;

            for (int i = 0; i < searchResult.size(); i++) {
                if (selection == (i + 1)) {
                    if (searchResult.get(i).getBorrowedBooks().size() < 1) {
                        System.out.println("\nAnvändaren har inte lånat några böcker!\n");
                        break;
                    }
                    System.out.println();
                    for (Book book : searchResult.get(i).getBorrowedBooks()){
                        System.out.println(book);
                    }
                    System.out.println();
                }
            }

        } while (true);
    }

    public void showAllBooksAndStatus(){

        Collections.sort(booksInLibrary, Book::compareTo);

        for (int i = 0; i < booksInLibrary.size(); i++){
            if (booksInLibrary.get(i).isAvailable()) {
                System.out.println((i + 1) + ". " + booksInLibrary.get(i) + ", " + "Tillgänglig");
            } else {
                System.out.println((i + 1) + ". " + booksInLibrary.get(i) + ", " + "Utlånad till: " + booksInLibrary.get(i).getCurrentReader().getName());
            }
        }
    }

    public void showBorrowedBooks(User user){
        int selection;

        if (user.getBorrowedBooks().size() > 0) {

            for (Book book : user.getBorrowedBooks()) {
                System.out.println(book);
            }
        } else {
            System.out.println("\nDu har inte lånat några böcker!");
            return;
        }

        System.out.println("\nVälj ett alternativ (0-1)\n");

        System.out.println("1) Lämna tillbaka böcker\n");
        System.out.println("0) Återgå till huvudmenyn");

        do{
            try{
                selection = Integer.parseInt(scanner.nextLine());
                if (selection > 1 || selection < 0){
                    throw new IndexOutOfBoundsException();
                }
                break;
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("Vänligen ange ett giltigt alternativ!\n");
            }
        }while (true);

        if (selection == 1) {
            returnBooks(user);
        }
    }

    public void returnBooks(User user){
        int selection;

        do {
            if (user.getBorrowedBooks().size() > 0) {

                for (int i = 0; i < user.getBorrowedBooks().size(); i++){
                    System.out.println(i + 1 + ". " + user.getBorrowedBooks().get(i));
                }
                System.out.println("\nAnge '0' för att återgå till huvudmenyn");
                System.out.print("Ange siffran till vänster om den bok du vill lämna tillbaka: ");

            } else {
                System.out.println("\nDu har inte lånat några böcker!");
                break;
            }

                do {
                    try {
                        selection = Integer.parseInt(scanner.nextLine());
                        break;
                    } catch (Exception e) {
                        System.out.println("Vänligen ange ett giltigt alternativ!\n");
                    }
                } while (true);

                if (selection == 0){
                    return;
                }

                for (int i = 0; i < user.getBorrowedBooks().size(); i++) {
                    if (selection == (i + 1)) {
                        for (Book book : booksInLibrary) {
                            if (book.getTitle().equals(user.getBorrowedBooks().get(i).getTitle())) {
                                book.setCurrentReader(null);
                                book.setAvailable(true);
                                user.returnBook(user.getBorrowedBooks().get(i));
                                System.out.println("\nDu har nu lämnat tillbaka " + book.getTitle() + " av " + book.getAuthor() + "!\n");
                                break;
                            }
                        }
                    }
                }
                FileUtils.saveObject("bookList.ser", booksInLibrary);
                FileUtils.saveObject("users.ser", customers);
        }while(true);
    }

    public void searchForBooks(User user) {
        String search;
        ArrayList<Book> searchResults = new ArrayList<>();
        int selection;

        do {
            System.out.println("\nAnge '0' för att återgå till huvudmenyn");
            System.out.print("Sök efter en boktitel/författare: ");

            do {
                search = scanner.nextLine();
                if (search.equals("0")) return;
                for (Book book : booksInLibrary) {
                    if (book.getAuthor().toLowerCase().contains(search.toLowerCase()) || book.getTitle().toLowerCase().contains(search.toLowerCase())) {
                        if (book.isAvailable()) {
                            searchResults.add(book);
                        }
                    }
                }
                if (searchResults.size() == 0) {
                    System.out.println("\nDin sökning gav inget resultat!\n");
                }
            } while (searchResults.size() < 1);

            Collections.sort(searchResults, Book::compareTo);

            for (int i = 0; i < searchResults.size(); i++) {
                System.out.println((i + 1) + ". " + searchResults.get(i));
            }
            System.out.println("\nAnge '0' för att återgå till huvudmenyn");
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
                    for (Book book : booksInLibrary){
                        if (book.getTitle().equals(searchResults.get(i).getTitle())){
                            user.setBorrowedBooks(book);
                            book.setCurrentReader(user);
                            book.setAvailable(false);
                            System.out.println("\nDu lånade " + book.getTitle() + " av " + book.getAuthor() + "!");
                        }
                    }
                }
            }
            FileUtils.saveObject("bookList.ser", booksInLibrary);
            FileUtils.saveObject("users.ser", customers);
        }while (true);
    }

    public void showAvailableBooks(User user) {
        ArrayList<Book> availableBooks = new ArrayList<>();

        for (Book book : booksInLibrary){
            if (book.isAvailable()){
                availableBooks.add(book);
            }
        }

        Collections.sort(availableBooks, Book::compareTo);

        for (int i = 0; i < availableBooks.size(); i++) {
            System.out.println((i + 1) + ". " + availableBooks.get(i));
        }

        int selection;

        System.out.println("\nAnge '0' för att återgå till huvudmenyn");
        System.out.print("\nFör att låna en bok, ange siffran till vänster om boken: ");

        do {
            try {
                selection = Integer.parseInt(scanner.nextLine());
                if (selection < 0 || selection > availableBooks.size() + 1) {
                    throw new IndexOutOfBoundsException();
                }
                break;
            } catch (Exception e) {
                System.out.println("\nVänligen ange ett giltigt alternativ!\n");
            }
        } while (true);

        if (selection == 0) return;

        for (int i = 0; i < availableBooks.size(); i++) {
            if (selection == (i + 1)) {
                for (Book book : booksInLibrary){
                    if (book.getTitle().equals(availableBooks.get(i).getTitle())){
                        user.setBorrowedBooks(book);
                        book.setCurrentReader(user);
                        book.setAvailable(false);
                        System.out.println("\nDu lånade " + book.getTitle() + " av " + book.getAuthor() + "!");
                    }
                }
            }
        }
        FileUtils.saveObject("bookList.ser", booksInLibrary);
        FileUtils.saveObject("users.ser", customers);
    }

    public User login() {
        String userName;
        String password;
        User currentUser = null;
        boolean keepLooping = true;

        do {
            System.out.print("\nAnge användarnamn:");
            userName = scanner.nextLine();
            for (Customer customer : customers) {
                if (customer.getName().equals(userName)) {
                    currentUser = customer;
                    keepLooping = false;
                    break;
                }
            }

            for (Librarian librarian : librarians) {
                if (librarian.getName().equals(userName)) {
                    currentUser = librarian;
                    keepLooping = false;
                    break;
                }
            }

            if (keepLooping) {
                System.out.println("\nAnvändaren finns inte i systemet!");
                System.out.println("Vänligen se över dina uppgifter eller skapa ett konto för att logga in.");
            }
        } while (keepLooping);

        do {
            System.out.print("Ange lösenord:");
            password = scanner.nextLine();
            if (currentUser.getPassword().equals(password)) {
                System.out.println("\nDu är nu inloggad!");
                break;
            }
            System.out.println("\nDu har angivit fel lösenord!\n");
        } while (true);

        return currentUser;
    }

    public Customer createUser() {

        String userName;
        String password;
        boolean keepLooping = false;
        Customer newUser = null;

        do {
            System.out.print("\nVälj ditt användarnamn: ");
            userName = scanner.nextLine();
            if(customers.size() > 0) {
                for (Customer customer : customers) {
                    if (customer.getName().equals(userName)) {
                        System.out.println("Användarnamnet är inte tillgängligt!\n");
                        keepLooping = true;
                        break;
                    } else keepLooping = false;
                }
            } else keepLooping = false;
        } while (keepLooping);

        System.out.print("\nVälj ditt lösenord (minst 4 tecken): ");
        do {
            password = scanner.nextLine();
            if (password.length() > 4){
                break;
            } else {
                System.out.println("Ditt lösenord måste vara minst 4 tecken långt!\n");
            }
        } while (true);

        do {
            System.out.print("Upprepa ditt lösenord: ");
            if (password.equals(scanner.nextLine())) {
                break;
            } else {
                System.out.println("Du måste upprepa lösenordet du angav i förra steget!\n");
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

        /*ArrayList<String> bookString = new ArrayList<>();
        bookString = (ArrayList<String>)FileUtils.loadText("books.txt");

        for (String book : bookString){
            String[] boooook = book.split(" by ");
            booksInLibrary.add(new Book(boooook[0], boooook[1]));
        }

        FileUtils.saveObject("bookList.ser", booksInLibrary);*/

        booksInLibrary = (ArrayList<Book>)FileUtils.loadObject("bookList.ser");
    }
}
