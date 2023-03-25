package seedu.duke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Ui {
    private static final List<String> PERMITTED_SEMESTER_VALUES = Arrays.asList("1", "2", "3", "4");

    /**
     * Prints a line of dashes for better readability
     */
    public static void printDash() {
        System.out.println("____________________________________________________________");
    }


    /**
     * Obtains user input and interprets what needs to be performed by certain keywords.
     * @param eventList List of events that ui interacts with
     */
    public static void getUserCommand(EventList eventList) {
        Scanner in = new Scanner(System.in);

        String cmd;
        cmd = in.nextLine();

        while (!(cmd.equals("bye"))) {
            Parser.parseCommand(cmd, eventList);
            cmd = in.nextLine();
        }

        in.close();
    }

    /**
     * Prints a welcome message for users when application is launched
     */
    public static void showWelcome() {
        String logo = ("888b      88  88        88   ad88888ba   88888888ba   88\n"
                + "8888b     88  88        88  d8\"     \"8b  88      \"8b  88\n"
                + "88 `8b    88  88        88  Y8,          88      ,8P  88\n"
                + "88  `8b   88  88        88  `Y8aaaaa,    88aaaaaa8P'  88  ,"
                + "adPPYYba,  8b,dPPYba,   8b,dPPYba,    ,adPPYba,  8b,dPPYba,  \n"
                + "88   `8b  88  88        88    `\"\"\"\"\"8b,  88\"\"\"\"\"\"'    "
                + "88  \"\"     `Y8  88P'   `\"8a  88P'   `\"8a  a8P_____88  88P'   \"Y8  \n"
                + "88    `8b 88  88        88          `8b  88           88  ,adPPPPP"
                + "88  88       88  88       88  8PP\"\"\"\"\"\"\"  88          \n"
                + "88     `8888  Y8a.    .a8P  Y8a     a8P  88           88  88,"
                + "    ,88  88       88  88       88  \"8b,   ,aa  88          \n"
                + "88      `888   `\"Y8888Y\"'    \"Y88888P\"   88           88 "
                + " `\"8bbdP\"Y8  88       88  88       88   `\"Ybbd8\"'  88          \n" + "\n");

        System.out.println(logo);
        printDash();
    }

    /**
     * Prints success message for users when event is added
     * @param taskDetail String containing information that was added to eventList
     */
    public static void addSuccessMsg(String taskDetail) {
        printDash();
        System.out.println("Event successfully added: " + System.lineSeparator());
        System.out.println("    > " + taskDetail);
        printDash();
    }

    /**
     * Prints success message for users when event is edited
     */
    public static void addSuccessEditMsg() {
        printDash();
        System.out.println("Event successfully edited!");
        printDash();
    }

    /**
     * Prints error message for users when there is unrecognised command Prints error message for users when there is
     * unrecognised command
     */
    public static void addErrorMsg() {
        printDash();
        System.out.println("Sorry, I don't understand you!");
        printDash();
    }

    /*
     * Prints success message for users when event is deleted
     */
    public static void deleteSuccessMsg(String taskDetail) {
        printDash();
        System.out.println("This event is deleted: " + System.lineSeparator());
        System.out.println("    > " + taskDetail);
        printDash();
    }

    /**
     * Prints list of events
     * @param eventList Array List containing events to be printed to user
     */
    public static void listTask(ArrayList<Event> eventList) {
        printDash();
        if (eventList.size() == 0) {
            System.out.println("There are no events!");
            printDash();
            return;
        }
        for (int i = 0; i < eventList.size(); i++) {
            System.out.println("   > " + Integer.toString(i + 1) + "." + eventList.get(i).toString());
        }
        printDash();
    }

    /**
     * Prints an exit message when user intends to exit Duke
     */
    public static void printExit() {
        printDash();
        System.out.println("Bye, see ya soon!");
        printDash();
    }

    public static void printErrorMsg(String errorMessage) {
        printDash();
        System.out.println(errorMessage);
        printDash();
    }

    public static void editSuccessMsg(String description, String time) {
        printDash();
        System.out.println("Time of event: " + description + " is changed to: ");
        System.out.println("    > " + time);
        printDash();
    }

    public static void deleteAllSuccess() {
        printDash();
        System.out.println("    > all events are deleted!");
        printDash();
    }

    public static void getSemester() {
        System.out.println("Before getting started, Please enter the semester you are in according to the below menu.");

        System.out.println("Type \"1\" for Semester 1");
        System.out.println("Type \"2\" for Semester 2");
        System.out.println("Type \"3\" for Special Term 1");
        System.out.println("Type \"4\" for Special Term 2");

        Scanner in = new Scanner(System.in);

        String cmd = in.nextLine();

        while (!PERMITTED_SEMESTER_VALUES.contains(cmd)) {
            System.out.println("Not a valid semester, please provide a valid semester.");
            System.out.println("Type \"1\" for Semester 1");
            System.out.println("Type \"2\" for Semester 2");
            System.out.println("Type \"3\" for Special Term 1");
            System.out.println("Type \"4\" for Special Term 2");
            cmd = in.nextLine();
        }

        User user = UserUtility.getUser();
        user.setSemester(Integer.parseInt(cmd));

        System.out.println("Semester saved!");
        printDash();
        System.out.println("Hello there! What can we do for you today?");
    }
}
