import Interfaces.Menu;
import Interfaces.Uifunctions;

public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        String message = "Welcome to Green Pulse!";
        Uifunctions.printWelcomeScreen(message);

        Menu menu = new Menu();
        menu.displayMainMenu();



//        entities.CarbonConsumption consumption = new entities.CarbonConsumption(
//                LocalDate.of(2024, 8, 1),
//                LocalDate.of(2024, 8, 31),
//                150.0
//        );
//        System.out.println(consumption);
//        System.out.println("Duration: " + consumption.getDurationInDays() + " days");
//
//
//       // List<entities.User> users = new ArrayList<>();
//        List<entities.User> users = new LinkedList<>();
//
//        entities.User user1 = new entities.User("Ibtihal", 20);
//        entities.User user2 = new entities.User("Samir", 30);
//        entities.User user3 = new entities.User("ziad", 20);
//
//        // Add entities.User instances to the list
//        users.add(user1);
//        users.add(user2);
//        users.add(user3);
//
//        for (entities.User user : users) {
//            System.out.println(user);
//        }



//        entities.User user1 = new entities.User("Ibtihal", 25);
//        System.out.println(user1);
//        String welcomemessage = "Welcome, " + user1.getName() + "!";
//        System.out.println(welcomemessage);

    }
}