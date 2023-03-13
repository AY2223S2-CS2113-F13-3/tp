package seedu.duke;

public class Parser {

    public static void parseCommand(String userInput, EventList eventList) {
        userInput = userInput.trim();
        String command = userInput.substring(0, userInput.indexOf(" "));
        String remainder = userInput.substring(userInput.indexOf(" ") + 1);
        switch (command) {
        case "add":
            parseAddCommand(remainder, eventList);
            break;
        case "delete":
            parseDeleteCommand(remainder, eventList);
            break;
        case "list":
            parseListCommand(eventList);
            break;
        case "edit":
            // parseEditCommand();
        default:
            Ui.addErrorMsg();
            break;
        }
    }

    private static void parseListCommand(EventList eventList) {
        Ui.listTask(eventList.fullList());
    }

    private static void parseDeleteCommand(String remainder, EventList eventList) {
        eventList.deleteThisTask(Integer.parseInt(remainder));

        //TODO: Show successful add on UI. (For all cases)
        Ui.deleteSuccessMsg();
    }

    private static void parseAddCommand(String remainder, EventList eventList) {
        // Note no "-" anywhere else.
        String[] details = remainder.split("-");
        String eventName = details[0];
        String startTime = details[1];
        String startDate = details[2];
        String endTime = details[3];

        if (details.length == 5) {
            String endDate = details[4];
            eventList.addEvent(eventName, startTime, startDate, endTime, endDate);
        } else {
            eventList.addEvent(eventName, startTime, startDate, endTime);
        }

        //TODO: Show successful add on UI. (For all cases)
        Ui.addSuccessMsg();
    }
}