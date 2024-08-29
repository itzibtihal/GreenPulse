package entities;

import java.util.List;
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



    @Override
    public String toString() {
        return "User:" +
                "\n userID=" + userID +
                "\n name='" + name + '\'' +
                "\n age=" + age;
    }
}
