package Interfaces;

public class Uifunctions {

    public static void printWelcomeScreen(String message) {
        int width = 40;  // Width of the welcome screen

        printLine(width);
        printEmptyLine(width);
        printCenteredMessageLine(message, width);
        printEmptyLine(width);
        printLine(width);
    }

    private static void printLine(int width) {
        for (int i = 0; i < width; i++) {
            System.out.print("*");
        }
        System.out.println();
    }

    private static void printEmptyLine(int width) {
        System.out.print("*");
        for (int i = 0; i < width - 2; i++) {
            System.out.print(" ");
        }
        System.out.println("*");
    }

    private static void printCenteredMessageLine(String message, int width) {
        int padding = (width - message.length() - 2) / 2;  // Calculate padding for centering
        System.out.print("*");
        for (int i = 0; i < padding; i++) {
            System.out.print(" ");
        }
        System.out.print(message);
        for (int i = 0; i < width - padding - message.length() - 2; i++) {
            System.out.print(" ");
        }
        System.out.println("*");
    }
}
