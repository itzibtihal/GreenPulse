package entities;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

public class User {
    private UUID userID;
    private String name;
    private int age;
    private List<CarbonConsumption> consumptions;

    public User(String name, int age) {
        this.userID = UUID.randomUUID();
        this.name = name;
        this.age = age;
        this.consumptions = new ArrayList<>();
    }

    public User() {
    }

    public UUID getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<CarbonConsumption> getConsumptions() {
        return consumptions;
    }

    @Override
    public String toString() {
        return "User:" +
                "\n userID=" + userID +
                "\n name='" + name + '\'' +
                "\n age=" + age;
    }
}
