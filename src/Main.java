import views.Menu;
import views.Uifunctions;

public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.

        String message = "Welcome to Green Pulse!";
        Uifunctions.printWelcomeScreen(message);

        Menu menu = new Menu();
        menu.displayMainMenu();

    }
}