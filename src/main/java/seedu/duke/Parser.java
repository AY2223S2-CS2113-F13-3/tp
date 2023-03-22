package seedu.duke;

import java.util.Arrays;

public class Parser {

    private static final int OFFSET = 1;
    private final Ui ui;

    public Parser() {
        ui = new Ui();
    }

    public static void parseCommand(String userInput, EventList eventList) {
        try {
            userInput = userInput.trim();
            String command = "";
            String remainder = "";
            if (!userInput.contains(" ")) {
                command = userInput;
            } else {
                command = userInput.substring(0, userInput.indexOf(" "));
                remainder = userInput.substring(userInput.indexOf(" ") + 1);
            }

            switch (command.toLowerCase()) {
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
                parseEditCommand(remainder, eventList);
                break;
            default:
                Ui.addErrorMsg();
                break;
            }
        } catch (Exception e) {
            Ui.printErrorMsg(e.getMessage());
        }
    }

    private static void parseListCommand(EventList eventList) {
        Ui.listTask(eventList.getFullList());
    }

    private static void parseDeleteCommand(String remainder, EventList eventList) throws NPExceptions {
        String[] details = remainder.split("-");

        if (details.length <= 1) {
            throw new NPExceptions("need a flag to specify your action!");
        }

        String information = details[1].substring(0, 1).trim();
        if (information.equals("s")) {
            int index = Integer.parseInt(details[1].substring(1).trim()) - OFFSET;
            String deletedTask = eventList.getDetails(index);
            eventList.deleteThisTask(index);
            // TODO: Show successful add on UI. (For all cases)
            Ui.deleteSuccessMsg(deletedTask);
        } else if (details[1].substring(0, 3).trim().equals("all")) {
            eventList.deleteAll();
            Ui.deleteAllSuccess();
        } else {
            throw new NPExceptions("please input a valid flag!");
        }

    }

    private static void parseAddCommand(String remainder, EventList eventList) throws NPExceptions {
        // Method is still broken, someone will have to fix it fully later on when handling exceptions
        // Note no "-" anywhere else.
        String[] details = remainder.split("-");

        if (details.length <= 1) {
            throw new NPExceptions("Event description and start day of your event are strictly required!");
        }
        boolean[] duplicity = new boolean[5]; // to detect duplicate flags in command
        Arrays.fill(duplicity, false);
        String[] information = new String[5];
        Arrays.fill(information, "");
        for (int i = 1; i < details.length; i++) {
            String field = details[i].substring(0, 2).trim();
            String change = details[i].substring(2).trim();
            switch (field) {
            case ("e"):
                if (!duplicity[0]) {
                    information[0] = change;
                    duplicity[0] = true;
                } else {
                    throw new NPExceptions("Cannot have duplicate flags a command!");
                }
                break;
            case ("st"):
                if (!duplicity[1]) {
                    information[1] = change;
                    duplicity[1] = true;
                } else {
                    throw new NPExceptions("Cannot have duplicate flags a command!");
                }
                break;
            case ("sd"):
                if (!duplicity[2]) {
                    information[2] = change;
                    duplicity[2] = true;
                } else {
                    throw new NPExceptions("Cannot have duplicate flags a command!");
                }
                break;
            case ("et"):
                if (!duplicity[3]) {
                    information[3] = change;
                    duplicity[3] = true;
                } else {
                    throw new NPExceptions("Cannot have duplicate flags a command!");
                }
                break;
            case ("ed"):
                if (!duplicity[4]) {
                    information[4] = change;
                    duplicity[4] = true;
                } else {
                    throw new NPExceptions("Cannot have duplicate flags a command!");
                }
                break;
            default:
                break;
            }
        }
        addFormatChecker(information);

        String eventName = information[0];
        String startTime = information[1];
        String startDate = information[2];

        if (!information[4].equals("")) {
            String endTime = information[3];
            String endDate = information[4];
            eventList.addEvent(eventName, startTime, startDate, endTime, endDate);
        } else {
            eventList.addEvent(eventName, startTime, startDate);
        }

        Ui.addSuccessMsg(eventList.getLastTaskDescription());
    }

    private static void parseEditCommand(String remainder, EventList eventList) throws NPExceptions {
        String[] details = remainder.split("-");
        String[] information = new String[5];

        if (!(details[1].substring(0, 2).trim().equalsIgnoreCase("i")
                || details[1].substring(0, 2).trim().equalsIgnoreCase("e"))) {
            throw new NPExceptions("undefined flag!");
        }

        Arrays.fill(information, "");
        for (int i = 1; i < details.length; i++) {
            String field = details[i].substring(0, 2).trim();
            String change = details[i].substring(2).trim();
            switch (field) {
            case ("i"):
                information[0] = change;
                break;
            case ("st"):
                information[1] = change;
                break;
            case ("sd"):
                information[2] = change;
                break;
            case ("et"):
                information[3] = change;
                break;
            case ("ed"):
                information[4] = change;
                break;
            default:
                break;
            }
        }

        if (information[2].equals("")) { // Starting date field MUST NOT be empty.
            throw new NPExceptions("Empty starting date detected! Please add starting date.");
        }

        if (information[0].equals("")) {
            information[0] = details[1].substring(2).trim();
            reviseTimeInfoUsingName(information, eventList);
        } else {
            reviseTimeInfoUsingIndex(information, eventList);
        }
    }

    private static void addFormatChecker(String[] information) throws NPExceptions {
        boolean isValidFormat = !information[0].equalsIgnoreCase("") && !information[2].equalsIgnoreCase("");

        if (!isValidFormat) {
            throw new NPExceptions("Please use correct command format!");
        }
    }

    private static void reviseTimeInfoUsingIndex(String[] information, EventList eventList)
            throws NPExceptions {
        int eventIndex = -1;
        try {
            eventIndex = Integer.parseInt(information[0]);
            eventIndex--;
        } catch (NumberFormatException e) {
            throw new NPExceptions("Event index is invalid!");
        }

        if (eventIndex < 0 || eventIndex >= eventList.getSize()) {
            throw new NPExceptions("Event index out of bound!");
        }

        if (!information[4].equals("")) {
            eventList.reviseTimeInfo(eventIndex, information[1], information[2], information[3],
                    information[4]);
        } else {
            eventList.reviseTimeInfo(eventIndex, information[1], information[2]);
        }

        Ui.editSuccessMsg(eventList.getDescription(eventIndex), eventList.getTime(eventIndex));
    }

    private static void reviseTimeInfoUsingName(String[] information, EventList eventList)
            throws NPExceptions {
        if (!information[4].equals("")) {
            eventList.reviseTimeInfo(information[0], information[1], information[2], information[3],
                    information[4]);
        } else {
            eventList.reviseTimeInfo(information[0], information[1], information[2]);
        }

        int eventIndex = eventList.searchTaskIndex(information[0]);
        Ui.editSuccessMsg(eventList.getDescription(eventIndex), eventList.getTime(eventIndex));
    }
}
