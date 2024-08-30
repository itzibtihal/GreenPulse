package entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CarbonConsumption {

    private LocalDate startDate;
    private LocalDate endDate;
    private double amount;

    public CarbonConsumption(LocalDate startDate, LocalDate endDate, double amount) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
    }


    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    // calculate duration
    public long getDurationInDays() {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }


    public double getAmountForPeriod(LocalDate startDate, LocalDate endDate) {
        LocalDate effectiveStart = startDate.isAfter(this.startDate) ? startDate : this.startDate;
        LocalDate effectiveEnd = endDate.isBefore(this.endDate) ? endDate : this.endDate;
        if (!effectiveStart.isAfter(effectiveEnd)) {
            long daysInPeriod = ChronoUnit.DAYS.between(effectiveStart, effectiveEnd) + 1;
            long totalDays = getDurationInDays();
            return (amount / totalDays) * daysInPeriod;
        }
        return 0;
    }


    @Override
    public String toString() {
        return "CarbonConsumption : \n" +
                "\nstartDate=" + startDate +
                "\nendDate=" + endDate +
                "\namount=" + amount +
                "\nduration=" + getDurationInDays() + " days" ;
    }
}
