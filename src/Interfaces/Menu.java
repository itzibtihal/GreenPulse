package Interfaces;

import entities.CarbonConsumption;
import entities.User;
import services.UserManager;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

public class Menu {
    private UserManager userManager;
    private Scanner scanner;

    public Menu() {
        this.userManager = new UserManager();
        this.scanner = new Scanner(System.in);
    }

    public void displayMainMenu() {
        int choice;
        do {
            System.out.println("\n--- Carbon Footprint Tracker Interfaces.Menu ---");
            System.out.println("1. Create New User");
            System.out.println("2. Add Carbon Consumption to User");
            System.out.println("3. Display User Details");
            System.out.println("4. List all users");
            System.out.println("5. Consultation of reports");
            System.out.println("6. Modify User");
            System.out.println("7. Delete User");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        createUser();
                        break;
                    case 2:
                        addConsumption();
                        break;
                    case 3:
                        displayUserDetails();
                        break;
                    case 4:
                        deleteUser();
                        break;
                    case 5:
                        System.out.println("reports");
                        break;
                    case 6:
                        System.out.println("Exiting the application..6.");
                        break;
                    case 7:
                        System.out.println("Exiting the application.7..");
                        break;
                    case 8:
                        System.out.println("Exiting the application...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                choice = 0;
            }
        } while (choice != 8);
    }

    private void createUser() {
        System.out.print("Enter user name: ");
        String name = scanner.nextLine();
        System.out.print("Enter user age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        User user = new User(name, age);
        userManager.addUser(user);
        System.out.println("entities.User created successfully !");
    }

    private void listAllUsers() {
        userManager.listAllUsers();
    }

    private void addConsumption() {
        listAllUsers();
        System.out.print("Enter user ID to add consumption (UUID format): ");
        UUID userID;
        try {
            userID = UUID.fromString(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format.");
            return;
        }

        User user = userManager.getUser(userID);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.print("Enter start date (YYYY-MM-DD): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter end date (YYYY-MM-DD): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter carbon amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        CarbonConsumption consumption = new CarbonConsumption(startDate, endDate, amount);
        userManager.addConsumptionToUser(userID, consumption);
        System.out.println("Carbon consumption added: " + consumption);
    }

    private void displayUserDetails() {
        listAllUsers();
        System.out.print("Enter user ID to display details (UUID format): ");
        UUID userID;
        try {
            userID = UUID.fromString(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format.");
            return;
        }

        User user = userManager.getUser(userID);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println(user);
        System.out.println("Carbon Consumption Records:");
            System.out.println(user.calculateTotalConsumption());
    }

    private void deleteUser() {
        listAllUsers();
        System.out.print("Enter the User ID to delete (UUID format): ");
        String userIdString = scanner.nextLine();

        try {
            UUID userId = UUID.fromString(userIdString);
            if (userManager.getUser(userId) != null) {
                userManager.removeUser(userId);
                System.out.println("User and their carbon consumption records have been deleted successfully.");
            } else {
                System.out.println("User not found with the given ID.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format. Please try again.");
        }
    }



}
