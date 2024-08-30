package entities;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.ArrayList;


public class User {
    private UUID userID;
    private String name;
    private int age;
    private List<CarbonConsumption> consumptions;


    public User( String name, int age) {
        this.userID = UUID.randomUUID();
        this.name = name;
        this.age = age;
        this.consumptions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setId(UUID id) {
        this.userID = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public UUID getUserID() {
        return userID;
    }

    public int getAge() {
        return age;
    }



    public List<CarbonConsumption> getConsumptions() {
        return consumptions;
    }

    public void addConsumption(CarbonConsumption consumption) {
        this.consumptions.add(consumption);
    }

    public void removeConsumption(CarbonConsumption consumption) {
        this.consumptions.remove(consumption);
    }

    public double calculateTotalConsumption() {
        return consumptions.stream()
                .mapToDouble(CarbonConsumption::getAmount)
                .sum();
    }

//    public double calculateTotalConsumption() {
//        return consumptions.stream()
//                .mapToDouble(CarbonConsumption::getAmount)
//                .sum();
//    }

    public double calculateDailyConsumption(LocalDate reportDate) {
        return getConsumptions().stream()
                .filter(consumption -> !consumption.getStartDate().isAfter(reportDate) && !consumption.getEndDate().isBefore(reportDate))
                .mapToDouble(consumption -> {
                    long durationInDays = ChronoUnit.DAYS.between(consumption.getStartDate(), consumption.getEndDate()) + 1;
                    return consumption.getAmount() / durationInDays;
                })
                .sum();
    }

    public double calculateWeeklyConsumption(LocalDate reportDate) {
        LocalDate weekStart = reportDate.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        LocalDate weekEnd = weekStart.plusDays(6);
        return getConsumptions().stream()
                .filter(consumption -> !consumption.getStartDate().isAfter(weekEnd) && !consumption.getEndDate().isBefore(weekStart))
                .mapToDouble(consumption -> {
                    long overlapDays = calculateOverlapDays(consumption, weekStart, weekEnd);
                    long totalDays = ChronoUnit.DAYS.between(consumption.getStartDate(), consumption.getEndDate()) + 1;
                    return (consumption.getAmount() / totalDays) * overlapDays;
                })
                .sum();
    }

    public double calculateMonthlyConsumption(LocalDate reportDate) {
        YearMonth month = YearMonth.from(reportDate);
        LocalDate monthStart = month.atDay(1);
        LocalDate monthEnd = month.atEndOfMonth();
        return getConsumptions().stream()
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



    @Override
    public String toString() {
        return "User:" +
                "\n userID=" + userID +
                "\n name='" + name + '\'' +
                "\n age=" + age;
    }
}
