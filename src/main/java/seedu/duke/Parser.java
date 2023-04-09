package seedu.duke;

import seedu.duke.storage.JsonNusModuleLoader;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Parser {
    public static final Map<Integer, LocalDate> SEMESTER_START_DATES = Map.of(1, LocalDate.of(2022, 8, 8), 2,
            LocalDate.of(2023, 1, 9), 3, LocalDate.of(2023, 5, 8), 4, LocalDate.of(2023, 6, 19));

    public static final Map<Integer, LocalDate> SEMESTER_END_DATES = Map.of(1, LocalDate.of(2022, 12, 3), 2,
            LocalDate.of(2023, 5, 6), 3, LocalDate.of(2023, 6, 17), 4, LocalDate.of(2023, 7, 29));

    private static final int OFFSET = 1;
    private static final JsonNusModuleLoader converter = new JsonNusModuleLoader();
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
                parseListCommand(remainder, eventList);
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

    private static void parseListCommand(String remainder, EventList eventList) {
        String[] details = remainder.split("-");

        if (details.length <= 1) {
            Ui.listTask(eventList.getFullList());
            return;
        }

        String information = details[1].substring(2).trim();
        int weekNumber = Integer.parseInt(information);
        Ui.printScheduleTable(eventList.getFullList(), weekNumber);

    }

    private static void parseDeleteCommand(String remainder, EventList eventList) throws NPExceptions {
        String[] details = remainder.split("-");

        if (details.length <= 1) {
            throw new NPExceptions("need a flag to specify your action!");
        }
        if (details.length > 2){
            throw new NPExceptions("Too many flags, or negative index entered");
        }

        String information = details[1].substring(0, 1).trim();
        if (information.equals("s")) {
            int index = Integer.parseInt(details[1].substring(1).trim()) - OFFSET;
            String deletedTask = eventList.getDetails(index);
            eventList.deleteThisTask(index);
            // TODO: Show successful add on UI. (For all cases)
            Duke.LOGGER.log(Level.INFO, "User deleted event in event list.");
            Ui.deleteSuccessMsg(deletedTask);
        } else if (details[1].length()<3){
            throw new NPExceptions("please input a valid flag");
        }else if (details[1].substring(0, 3).trim().equals("all")) {
            eventList.deleteAll();
            Duke.LOGGER.log(Level.INFO, "User deleted all events in event list.");
        } else {
            throw new NPExceptions("please input a valid flag!");
        }

    }

    private static void parseAddCommand(String remainder, EventList eventList) throws NPExceptions {
        // Method is still broken, someone will have to fix it fully later on when handling exceptions
        // Note no "-" anywhere else.
        String[] details = remainder.split("-");

        boolean isModuleFlag = false;

        if (details.length <= 1) {
            throw new NPExceptions("Event description and start day of your event are strictly required!");
        }
        boolean[] duplicity = new boolean[7]; // to detect duplicate flags in command
        Arrays.fill(duplicity, false);
        String[] information = new String[7];
        Arrays.fill(information, "");

        Map<String, Object> map =  UserUtility.extractFields(duplicity, information, details, isModuleFlag);
        information = (String[]) map.get("information");
        isModuleFlag = (boolean) map.get("isModuleFlag");

        addFormatChecker(information);

        // if body executed when user adds a module. Code inside "else" is same as before.
        if (isModuleFlag) {
            eventList.addModule(information, converter);
        } else {
            String eventName = information[0];
            String startTime = information[1];
            String startDate = information[2];

            if (!information[3].equals("")) {

                String endTime = information[3];
                String endDate = information[4].equals("") ? startDate : information[4];
                if (information[5].equals("")) {
                    eventList.addEvent(eventName, startTime, startDate, endTime, endDate);
                } else {
                    eventList.addEvent(eventName, startTime, startDate, endTime, endDate, information[5]);
                }

            } else {
                if (!information[4].equals("")) {
                    Ui.printEDOmitted();
                }
                if (information[5].equals("")) {
                    eventList.addEvent(eventName, startTime, startDate);
                } else {
                    eventList.addEvent(eventName, startTime, startDate, information[5]);
                }
            }

            // add location(venue)
            int eventNum = eventList.getSize() - 1;
            if (duplicity[6]) {
                eventList.reviseLocation(eventNum, information[6]);
            }
            Ui.addSuccessMsg(eventList.getLastTaskDescription());
        }
    }

    public static String findDateOfWeek(int semester, Integer weekNumber, String dayOfWeek) {
        // Specify the start date of the semester
        LocalDate semesterStartDate = SEMESTER_START_DATES.get(semester);

        // Calculate the date for the specified day of the week in the specified week
        LocalDate weekStartDate = semesterStartDate.plusWeeks(weekNumber - 1);
        LocalDate desiredDate =
                weekStartDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.valueOf(dayOfWeek.toUpperCase())));

        // Output the result
        return desiredDate.toString().replace('-', '/');
    }

    private static void parseEditCommand(String remainder, EventList eventList) throws NPExceptions {
        String[] details = remainder.split("-");
        String[] information = new String[6];

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
            case ("v"):
                information[5] = change;
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
        boolean isValidFormat = !information[0].equalsIgnoreCase("") && !information[2].equalsIgnoreCase("")
                && !information[1].equalsIgnoreCase("");

        // String recurFlag = information[5].split(" ")[2].trim();
        // isValidFormat = isValidFormat && (recurFlag.equalsIgnoreCase("W") || recurFlag.equalsIgnoreCase("D"));

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

        // revise location info
        if (!information[5].equals("")) {
            eventList.reviseLocation(eventIndex, information[5]);
        }

        Duke.LOGGER.log(Level.INFO, "User edited time of event.");
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

        // revise location info
        if (!information[5].equals("")) {
            eventList.reviseLocation(eventIndex, information[5]);
        }
        Duke.LOGGER.log(Level.INFO, "User edited time of event.");
        Ui.editSuccessMsg(eventList.getDescription(eventIndex), eventList.getTime(eventIndex));
    }
}
