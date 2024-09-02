package views;

import entities.CarbonConsumption;
import entities.User;
import services.UserManager;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
            System.out.print("\n Enter your choice: ");

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
                        listAllUsers();
                        break;
                    case 5:
                        generateReport();
                        break;
                    case 6:
                        modifyUser();
                        break;
                    case 7:
                        deleteUser();
                        break;
                    case 8:
                        String message = "Exiting the application...";
                        Uifunctions.printWelcomeScreen(message);
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

        int age = -1;
        boolean validAge = false;

        while (!validAge) {
            System.out.print("Enter user age: ");
            try {
                age = scanner.nextInt();
                scanner.nextLine();
                if (age < 0) {
                    System.out.println("Age cannot be negative. Please enter a valid age.");
                } else {
                    validAge = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
        User user = new User(name, age);
        userManager.addUser(user);
        System.out.println("User created successfully!");
    }

    private void listAllUsers() {
        userManager.listAllUsers();
    }

    private void addConsumption() {
        listAllUsers();

        UUID userID = null;
        boolean validID = false;

        while (!validID) {
            System.out.print("Enter user ID to add consumption (UUID format): ");
            try {
                userID = UUID.fromString(scanner.nextLine());
                validID = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID format. Please try again.");
            }
        }

        User user = userManager.getUser(userID);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        LocalDate startDate = null;
        boolean validStartDate = false;

        while (!validStartDate) {
            System.out.print("Enter start date (YYYY-MM-DD): ");
            try {
                startDate = LocalDate.parse(scanner.nextLine());
                validStartDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            }
        }

        LocalDate endDate = null;
        boolean validEndDate = false;

        while (!validEndDate) {
            System.out.print("Enter end date (YYYY-MM-DD): ");
            try {
                endDate = LocalDate.parse(scanner.nextLine());
                validEndDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            }
        }

        double amount = -1;
        boolean validAmount = false;

        while (!validAmount) {
            System.out.print("Enter carbon amount: ");
            try {
                amount = scanner.nextDouble();
                scanner.nextLine();
                if (amount < 0) {
                    System.out.println("Carbon amount cannot be negative. Please enter a valid amount.");
                } else {
                    validAmount = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }

        CarbonConsumption consumption = new CarbonConsumption(startDate, endDate, amount);
        userManager.addConsumptionToUser(userID, consumption);
        System.out.println("Carbon consumption added: " + consumption);
    }

    private void displayUserDetails() {
        listAllUsers();
        UUID userID = null;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Enter user ID to display details (UUID format): ");
            try {
                userID = UUID.fromString(scanner.nextLine());
                validInput = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID format. Please try again.");
            }
        }
        User user = userManager.getUser(userID);
        if (user == null) {
            System.out.println("User not found. Please try again.");
            return;
        }
        System.out.println("User Details:");
        System.out.println("ID: " + user.getUserID());
        System.out.println("Name: " + user.getName());
        System.out.println("Age: " + user.getAge());
        double totalConsumption = userManager.calculateTotalConsumption(userID);
        System.out.printf("Total Carbon Consumption: %.2f units%n", totalConsumption);
    }

    private void deleteUser() {
        while (true) {
            listAllUsers();
            System.out.print("Enter the User ID to delete (UUID format): ");
            String userIdString = scanner.nextLine();

            try {
                UUID userId = UUID.fromString(userIdString);

                if (userManager.getUser(userId) != null) {
                    userManager.removeUser(userId);
                    System.out.println("User and their carbon consumption records have been deleted successfully.");
                    break;
                } else {
                    System.out.println("User not found with the given ID. Please try again.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID format. Please try again.");
            }
        }
    }

    private void modifyUser() {
        listAllUsers();

        UUID userID = null;
        boolean validID = false;

        while (!validID) {
            System.out.print("Enter the User ID to modify (UUID format): ");
            try {
                userID = UUID.fromString(scanner.nextLine());
                validID = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID format. Please try again.");
            }
        }

        User user = userManager.getUser(userID);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("Current user details: " + user);
        boolean validChoice = false;

        while (!validChoice) {
            System.out.println("What would you like to modify?");
            System.out.println("1. Name");
            System.out.println("2. Age");
            System.out.println("3. Cancel");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    user.setName(newName);
                    System.out.println("Name updated successfully.");
                    validChoice = true;
                    break;
                case 2:
                    System.out.print("Enter new age: ");
                    int newAge;
                    try {
                        newAge = scanner.nextInt();
                        scanner.nextLine();
                        user.setAge(newAge);
                        System.out.println("Age updated successfully.");
                        validChoice = true;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a number.");
                        scanner.nextLine();
                    }
                    break;
                case 3:
                    System.out.println("Modification canceled.");
                    validChoice = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void generateReport() {
        userManager.listAllUsers();
        UUID userID;
        while (true) {
            System.out.print("Enter user ID to generate report (UUID format): ");
            try {
                userID = UUID.fromString(scanner.nextLine());
                User user = userManager.getUser(userID);
                if (user == null) {
                    System.out.println("User not found. Please enter a valid user ID.");
                } else {
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID format. Please enter a valid UUID.");
            }
        }

        String reportType;
        LocalDate reportDate;

        while (true) {
            System.out.print("Enter report type (daily, weekly, monthly): ");
            reportType = scanner.nextLine().toLowerCase();
            if (reportType.equals("daily") || reportType.equals("weekly") || reportType.equals("monthly")) {
                break;
            } else {
                System.out.println("Invalid report type. Please enter 'daily', 'weekly', or 'monthly'.");
            }
        }

        while (true) {
            System.out.print("Enter the date for the report (YYYY-MM-DD): ");
            try {
                reportDate = LocalDate.parse(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
            }
        }

        userManager.generateReport(userID, reportType, reportDate);
    }

}
