package services;

import entities.CarbonConsumption;
import entities.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Scanner;

public class UserManager {
    private Map<UUID, User> userMap;
    private Scanner scanner;

    public UserManager() {
        this.userMap = new HashMap<>();
    }

    public void addUser(User user) {
        userMap.put(user.getUserID(), user);
    }
    public void removeUser(UUID userID) {
        userMap.remove(userID);
    }
    public User getUser(UUID userID) {
        return userMap.get(userID);
    }

    public void addConsumptionToUser(UUID userID, CarbonConsumption consumption) {
        User user = getUser(userID);
        if (user != null) {
            user.addConsumption(consumption);
        }
    }

    public void listAllUsers() {
        if (userMap.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("--- List of Users ---");
        int idWidth = 36;
        int nameWidth = 20;
        int ageWidth = 3;
        // Print header
        System.out.println(new String(new char[idWidth + nameWidth + ageWidth + 6]).replace('\0', '-'));
        System.out.printf("%-" + idWidth + "s | %-" + nameWidth + "s | %-" + ageWidth + "s%n", "User ID", "Name", "Age");
        System.out.println(new String(new char[idWidth + nameWidth + ageWidth + 6]).replace('\0', '-'));

        // Print user details
        for (User user : userMap.values()) {
            System.out.printf("%-" + idWidth + "s | %-" + nameWidth + "s | %-" + ageWidth + "d%n",
                    user.getUserID(),
                    user.getName(),
                    user.getAge());
            System.out.println(new String(new char[idWidth + nameWidth + ageWidth + 6]).replace('\0', '-'));
        }
    }

    public Map<UUID, User> getAllUsers() {
        return userMap;
    }

    public void deleteUser() {
        System.out.print("Enter the User ID to delete: ");
        String userIdString = scanner.nextLine();

        try {
            UUID userId = UUID.fromString(userIdString);
            if (userMap.containsKey(userId)) {
                userMap.remove(userId); // This also removes the user's consumptions if they are stored in the User class

                System.out.println("User and their carbon consumption records have been deleted successfully.");
            } else {
                System.out.println("User not found with the given ID.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format. Please try again.");
        }
    }

}
