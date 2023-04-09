package seedu.duke;

import seedu.duke.storage.JsonNusModuleLoader;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class EventList {
    private static final String DTINIT = "2000/01/01 01:01";
    private static final String TIMEPLACEHOLDER = " 00:00";
    private static DateTimeFormatter dfWithTime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    protected ArrayList<Schedule> taskList;
    protected int listSize;

    public EventList() {
        this.taskList = new ArrayList<Schedule>();
        this.listSize = 0;
    }

    public EventList(ArrayList<Schedule> events) {
        this.taskList = events;
        this.listSize = events.size();
    }

    public int getSize() {
        return listSize;
    }

    public String getDetails(int index) {
        return taskList.get(index).toString();
    }

    public String getDescription(int index) {
        return taskList.get(index).getDescription();
    }

    public String getTime(int index) {
        return taskList.get(index).getTime();
    }

    public void deleteThisTask(int index) {
        taskList.remove(index);
        listSize--;
    }

    private LocalDateTime changeToDate(String time, String date) {
        String combination = date + " " + time;
        return LocalDateTime.parse(combination, dfWithTime);
    }

    private LocalDateTime changeToDate(String date) {
        return LocalDateTime.parse(date + TIMEPLACEHOLDER, dfWithTime);
    }

    /**
     * For two addEvent funcs below: if user doesn't input endDay(which means there is also no endTime),
     * you can just call .addEvent(description, startTime, startDay) I also make the specific time(hh:mm)
     * optional, so if user doesn't input the specfic time, you can just pass an empty String to that param
     * and it will handle the rest things
     * e.g.addEvent(descrption, "", startDay, "", endDay)
     *     addEvent(descrption, "", startDay, endTime, endDay)
     *     addEvent(descrption, "", startDay) so only startDay is strictly required. and the same for reviseTimeInfo()
     *
     * @param time String representing Time to be converted to dateTime format in combinedTime. Format "HH:MM".
     * @param day String representing Date to be converted to dateTime format in combinedTime. Format "YYYY/MM/DD".
     * @return TimeAndFlag
     * @throws NPExceptions if format of time or day is not as specified above
     * @see TimeAndFlag
     */
    public TimeAndFlag convertToTimeInfo(String time, String day) throws NPExceptions {
        try {
            boolean hasTime = true;
            LocalDateTime combinedTime = LocalDateTime.parse(DTINIT, dfWithTime);

            if (time.equals("")) {
                hasTime = false;
                combinedTime = changeToDate(day);
            } else {
                combinedTime = changeToDate(time, day);
            }

            TimeAndFlag result = new TimeAndFlag(hasTime, combinedTime);
            return result;
        } catch (Exception e) {
            throw new NPExceptions(
                    "Wrong date/time format! \nPlease use yyyy/MM/dd for date and HH:mm for time.");
        }
    }

    // private boolean checkConfliction(String description, String startTime, S\
    //tring startDay, String endTime, String endDay) {
    //     return
    // }

    public void addEvent(String description, String startTime, String startDay, String endTime, String endDay)
            throws NPExceptions {

        TimeAndFlag startInfo = convertToTimeInfo(startTime, startDay);
        TimeAndFlag endInfo = convertToTimeInfo(endTime, endDay);
        Event newEvent =
                new Event(description, startInfo.time, endInfo.time, startInfo.hasInfo, endInfo.hasInfo);

        UserUtility.validateAddEvent(newEvent, taskList);
        // if (!canAddNewEvent(newEvent)) {
        // throw new NPExceptions(
        // "Slot " + startTime + " - " + endTime + " on " +
        // startDay + " is already occupied. You can't attend this class.");
        // }

        taskList.add(newEvent);
        listSize++;
    }

    public void addEvent(String description, String startTime, String startDay) throws NPExceptions {
        TimeAndFlag startInfo = convertToTimeInfo(startTime, startDay);

        Event newEvent = new Event(description, startInfo.time, startInfo.hasInfo);
        UserUtility.validateAddEvent(newEvent, taskList);
        taskList.add(newEvent);
        listSize++;
    }

    public void addEvent(String description, String startTime, String startDay, String recurTime)
            throws NPExceptions {
        TimeAndFlag startInfo = convertToTimeInfo(startTime, startDay);

        Event newEvent = new Event(description, startInfo.time, startInfo.hasInfo, recurTime);
        UserUtility.validateAddEvent(newEvent, taskList);
        taskList.add(newEvent);
        listSize++;
    }

    public void addEvent(String description, String startTime, String startDay, String endTime, String endDay,
                         String recurTime) throws NPExceptions {

        TimeAndFlag startInfo = convertToTimeInfo(startTime, startDay);
        TimeAndFlag endInfo = convertToTimeInfo(endTime, endDay);

        Event newEvent = new Event(description, startInfo.time, endInfo.time, startInfo.hasInfo,
                endInfo.hasInfo, recurTime);
        UserUtility.validateAddEvent(newEvent, taskList);
        taskList.add(newEvent);
        listSize++;
    }

    public void reviseLocation(int index, String location) {
        taskList.get(index).changeLocation(location);
    }

    public void reviseTimeInfo(int index, String startTime, String startDay, String endTime, String endDay)
            throws NPExceptions {
        TimeAndFlag startInfo = convertToTimeInfo(startTime, startDay);
        TimeAndFlag endInfo = convertToTimeInfo(endTime, endDay);

        UserUtility.validateDates(startInfo.time, endInfo.time);

        taskList.get(index).changeTimeInfo(startInfo.time, endInfo.time, startInfo.hasInfo, endInfo.hasInfo);
    }

    public void reviseTimeInfo(int index, String startTime, String startDay) throws NPExceptions {
        TimeAndFlag startInfo = convertToTimeInfo(startTime, startDay);
        UserUtility.validateDates(startInfo.time, null);
        taskList.get(index).changeTimeInfo(startInfo.time, startInfo.hasInfo);
    }

    // need handle exceptions when index = -1
    public void reviseTimeInfo(String description, String startTime, String startDay, String endTime,
                               String endDay) throws NPExceptions {
        int index = searchTaskIndex(description);
        if (index == -1) {
            throw new NPExceptions("Event cannot be found!");
        }
        reviseTimeInfo(index, startTime, startDay, endTime, endDay);
    }

    // need handle exceptions when index = -1
    public void reviseTimeInfo(String description, String startTime, String startDay) throws NPExceptions {
        int index = searchTaskIndex(description);
        if (index == -1) {
            throw new NPExceptions("Event cannot be found!");
        }
        reviseTimeInfo(index, startTime, startDay);
    }

    public ArrayList<Schedule> getFullList() {
        return this.taskList;
    }

    public int searchTaskIndex(String description) {
        int index = 0;
        for (Schedule cur : taskList) {
            if (cur.getDescription().trim().equals(description.trim())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public String getLastTaskDescription() {
        return taskList.get(listSize - 1).toString();
    }

    public void deleteAll() {
        if (this.listSize == 0) {
            Ui.deleteAllError();
        } else {
            this.taskList = new ArrayList<Schedule>();
            this.listSize = 0;
            Ui.deleteAllSuccess();
        }
    }

    public boolean canAddNewEvent(Event newEvent) {
        boolean isOverlap = false;
        for (Schedule event : taskList) {
            if (newEvent.getStartTime().isBefore(event.getEndTime())
                    && newEvent.getEndTime().isAfter(event.getStartTime())) {
                isOverlap = true;
                break;
            }
        }
        return !isOverlap;
    }

    public void addModule(String[] information, JsonNusModuleLoader converter) throws NPExceptions {
        // count to store the number of classes added into eventList.
        int count = 0;

        // fetching information
        String moduleCode = information[0].toUpperCase();
        String classNumber = information[1];
        String lectureType = information[2];

        // loading modules. Need to update when singleton design is utilized.
        HashMap<String, NusModule> nusmods = converter.loadModules();
        Duke.LOGGER.log(Level.INFO, "loadModules() called");
        // Fetch NusModule from module code
        NusModule nusModule = nusmods.get(moduleCode);
        if (nusModule == null) {
            Duke.LOGGER.log(Level.INFO, "User selected module that does not exist.");
            throw new NPExceptions("Module " + moduleCode + " does not exist!");
        }

        // Fetch lessons from module
        List<Lesson> lessons =
                nusModule.getLesson(UserUtility.getUser().getSemester(), lectureType, classNumber);
        if (lessons == null || lessons.isEmpty()) {
            Duke.LOGGER.log(Level.INFO, "User selected module that is unavailable for semester.");
            Ui.printErrorMsg("Selected module is not available for semester "
                    + UserUtility.getUser().getSemester());
            return;
        }

        // Create event for each day of module
        for (Lesson lesson : lessons) {
            String venue = lesson.getVenue();
            for (Integer week : lesson.getWeeks()) {

                // Method to get date on the lesson's day in a given week number.
                if (week >= 7) {
                    week++;
                }

                String startDate =
                        Parser.findDateOfWeek(UserUtility.getUser().getSemester(), week, lesson.getDay());

                // Converting time to HH:mm format.
                StringBuilder sb = new StringBuilder(lesson.getStartTime());
                String startTime = sb.insert(2, ':').toString();
                sb = new StringBuilder(lesson.getEndTime());
                String endTime = sb.insert(2, ':').toString();

                int size = getSize();
                try {
                    addEvent(nusModule.getModuleCode(), startTime, startDate, endTime,
                            startDate);
                    reviseLocation(size++, venue);
                    count++;
                } catch (NPExceptions e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        Duke.LOGGER.log(Level.INFO, "User added module to event list.");
        Ui.addSuccessMsg("Added " + count + " classes of Module: " + moduleCode);
    }
}


final class TimeAndFlag {
    public boolean hasInfo;
    public LocalDateTime time;

    public TimeAndFlag(boolean info, LocalDateTime timeInfo) {
        this.hasInfo = info;
        this.time = timeInfo;
    }
}
