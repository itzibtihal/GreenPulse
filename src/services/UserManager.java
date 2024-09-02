package services;

import entities.CarbonConsumption;
import entities.User;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class UserManager {
    private Map<UUID, User> userMap;

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
            user.getConsumptions().add(consumption);
        }
    }

    public double calculateTotalConsumption(UUID userID) {
        User user = getUser(userID);
        return user.getConsumptions().stream()
                .mapToDouble(CarbonConsumption::getAmount)
                .sum();
    }

    public double calculateDailyConsumption(UUID userID, LocalDate reportDate) {
        User user = getUser(userID);
        return user.getConsumptions().stream()
                .filter(consumption -> !consumption.getStartDate().isAfter(reportDate) && !consumption.getEndDate().isBefore(reportDate))
                .mapToDouble(consumption -> {
                    long durationInDays = ChronoUnit.DAYS.between(consumption.getStartDate(), consumption.getEndDate()) + 1;
                    return consumption.getAmount() / durationInDays;
                })
                .sum();
    }

    public double calculateWeeklyConsumption(UUID userID, LocalDate reportDate) {
        User user = getUser(userID);
        LocalDate weekStart = reportDate.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        LocalDate weekEnd = weekStart.plusDays(6);
        return user.getConsumptions().stream()
                .filter(consumption -> !consumption.getStartDate().isAfter(weekEnd) && !consumption.getEndDate().isBefore(weekStart))
                .mapToDouble(consumption -> {
                    long overlapDays = calculateOverlapDays(consumption, weekStart, weekEnd);
                    long totalDays = ChronoUnit.DAYS.between(consumption.getStartDate(), consumption.getEndDate()) + 1;
                    return (consumption.getAmount() / totalDays) * overlapDays;
                })
                .sum();
    }

    public double calculateMonthlyConsumption(UUID userID, LocalDate reportDate) {
        User user = getUser(userID);
        YearMonth month = YearMonth.from(reportDate);
        LocalDate monthStart = month.atDay(1);
        LocalDate monthEnd = month.atEndOfMonth();
        return user.getConsumptions().stream()
                .filter(consumption -> !consumption.getStartDate().isAfter(monthEnd) && !consumption.getEndDate().isBefore(monthStart))
                .mapToDouble(consumption -> {
                    long overlapDays = calculateOverlapDays(consumption, monthStart, monthEnd);
                    long totalDays = ChronoUnit.DAYS.between(consumption.getStartDate(), consumption.getEndDate()) + 1;
                    return (consumption.getAmount() / totalDays) * overlapDays;
                })
                .sum();
    }

    private long calculateOverlapDays(CarbonConsumption consumption, LocalDate start, LocalDate end) {
        LocalDate overlapStart = consumption.getStartDate().isAfter(start) ? consumption.getStartDate() : start;
        LocalDate overlapEnd = consumption.getEndDate().isBefore(end) ? consumption.getEndDate() : end;
        return ChronoUnit.DAYS.between(overlapStart, overlapEnd) + 1;
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
        System.out.println(new String(new char[idWidth + nameWidth + ageWidth + 6]).replace('\0', '-'));
        System.out.printf("%-" + idWidth + "s | %-" + nameWidth + "s | %-" + ageWidth + "s%n", "User ID", "Name", "Age");
        System.out.println(new String(new char[idWidth + nameWidth + ageWidth + 6]).replace('\0', '-'));

        for (User user : userMap.values()) {
            System.out.printf("%-" + idWidth + "s | %-" + nameWidth + "s | %-" + ageWidth + "d%n",
                    user.getUserID(),
                    user.getName(),
                    user.getAge());
            System.out.println(new String(new char[idWidth + nameWidth + ageWidth + 6]).replace('\0', '-'));
        }
    }

    public void generateReport(UUID userID, String reportType, LocalDate reportDate) {
        switch (reportType.toLowerCase()) {
            case "daily":
                double dailyConsumption = calculateDailyConsumption(userID, reportDate);
                System.out.printf("Daily carbon consumption report for %s for user %s: %.2f units%n",
                        reportDate,
                        getUser(userID).getName(),
                        dailyConsumption);
                break;

            case "weekly":
                double weeklyConsumption = calculateWeeklyConsumption(userID, reportDate);
                System.out.printf("Weekly carbon consumption report for the week starting %s for user %s: %.2f units%n",
                        reportDate,
                        getUser(userID).getName(),
                        weeklyConsumption);
                break;

            case "monthly":
                double monthlyConsumption = calculateMonthlyConsumption(userID, reportDate);
                System.out.printf("Monthly carbon consumption report for %s %d for user %s: %.2f units%n",
                        reportDate.getMonth(),
                        reportDate.getYear(),
                        getUser(userID).getName(),
                        monthlyConsumption);
                break;
            default:
                System.out.println("Invalid report type.");
        }
    }
}
