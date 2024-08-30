package services;

import entities.CarbonConsumption;
import entities.User;

import java.time.LocalDate;
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
                userMap.remove(userId);
                System.out.println("User and their carbon consumption records have been deleted successfully.");
            } else {
                System.out.println("User not found with the given ID.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format. Please try again.");
        }
    }

    public void generateReport(UUID userID, String reportType, LocalDate reportDate) {
        User user = getUser(userID);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        double totalConsumption = 0;
        switch (reportType.toLowerCase()) {
            case "daily":
                totalConsumption = user.getConsumptions().stream()
                        .filter(consumption -> !consumption.getStartDate().isAfter(reportDate) && !consumption.getEndDate().isBefore(reportDate))
                        .mapToDouble(consumption -> consumption.getAmount() / consumption.getDurationInDays())
                        .sum();
                System.out.println("Daily carbon consumption report for " + reportDate + " for user " + user.getName() + ": " + totalConsumption + " units");
                break;

            case "weekly":
                LocalDate weekStart = reportDate.minusDays(reportDate.getDayOfWeek().getValue() % 7);
                LocalDate weekEnd = weekStart.plusDays(6);
                totalConsumption = user.getConsumptions().stream()
                        .filter(consumption -> !consumption.getEndDate().isBefore(weekStart) && !consumption.getStartDate().isAfter(weekEnd))
                        .mapToDouble(CarbonConsumption::getAmount)
                        .sum();
                System.out.println("Weekly carbon consumption report from " + weekStart + " to " + weekEnd + " for user " + user.getName() + ": " + totalConsumption + " units");
                break;

            case "monthly":
                LocalDate monthStart = reportDate.withDayOfMonth(1);
                LocalDate monthEnd = reportDate.withDayOfMonth(reportDate.lengthOfMonth());
                totalConsumption = user.getConsumptions().stream()
                        .filter(consumption -> !consumption.getEndDate().isBefore(monthStart) && !consumption.getStartDate().isAfter(monthEnd))
                        .mapToDouble(CarbonConsumption::getAmount)
                        .sum();
                System.out.println("Monthly carbon consumption report for " + reportDate.getMonth() + " " + reportDate.getYear() + " for user " + user.getName() + ": " + totalConsumption + " units");
                break;

            default:
                System.out.println("Invalid report type.");
        }
    }

}
