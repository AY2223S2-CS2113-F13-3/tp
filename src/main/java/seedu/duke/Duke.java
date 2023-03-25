package seedu.duke;

// import java.util.Scanner;
//import seedu.duke.ui.Ui;
//import seedu.duke.EventList;
//import seedu.duke.parser.Parser;


import seedu.duke.storage.Storage;

public class Duke {
    /**
     * Main entry-point for the java.duke.Duke application.
     */
    private final EventList eventTracker;
    private final Ui ui;
    private final Parser parser;
    private final Storage storage;

    public Duke (){
        ui = new Ui();
        storage = new Storage();
        eventTracker = new EventList(storage.loadEvents());
        parser = new Parser();
    }
    public void run(){
        ui.showWelcome();
        // Method to fetch semester
        ui.getSemester();
        ui.getUserCommand(eventTracker);
        storage.saveToFile(eventTracker); //Will only save to file on exit
    }

    public static void main(String[] args) {
        Duke nusPlanner = new Duke();
        nusPlanner.run();
    }
}
