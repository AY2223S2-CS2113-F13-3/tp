package seedu.duke;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventList {
    private static final String DTINIT = "2000/01/01 01:01";
    private static DateTimeFormatter dfWithTime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    protected ArrayList<Event> taskList;
    protected int listSize;

    public EventList() {
        this.taskList = new ArrayList<Event>();
        this.listSize = 0;
    }

    public EventList(ArrayList<Event> events) {
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
        return LocalDateTime.parse(date + " 00:00", dfWithTime);
    }
    /**
     * For two addEvent funcs below:
     *      if user doesn't input endDay(which means there is also no endTime),
     *      you can just call .addEvent(description, startTime, startDay)
     *      I also make the specific time(hh:mm) optional, so if user doesn't input the specfic time,
     *      you can just pass an empty String to that param and it will handle the rest things
     *      e.g. addEvent(descrption, "", startDay, "", endDay)
     *           addEvent(descrption, "", startDay, endTime, endDay)
     *           addEvent(descrption, "", startDay)
     *      so only startDay is strictly required.
     *      and the same for reviseTimeInfo()
     * @param time String representing Time to be converted to dateTime format in combinedTime. Format "HH:MM".
     * @param day String representing Date to be converted to dateTime format in combinedTime. Format "YYYY/MM/DD".
     * @return TimeAndFlag
     * @throws  NPExceptions if format of time or day is not as specified above
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
            throw new NPExceptions("Wrong date/time format! \nPlease use yyyy/MM/dd for date and HH:mm for time.");
        }
    }

    public void addEvent(String description, String startTime, String startDay) throws NPExceptions {
        TimeAndFlag startInfo = convertToTimeInfo(startTime, startDay);

        Event newEvent = new Event(description, startInfo.time, startInfo.hasInfo);
        taskList.add(newEvent);
        listSize++;
    }

    public void addEvent(String description, String startTime, String startDay, String endTime,
                         String endDay) throws NPExceptions {

        TimeAndFlag startInfo = convertToTimeInfo(startTime, startDay);
        TimeAndFlag endInfo = convertToTimeInfo(endTime, endDay);

        Event newEvent = new Event(description, startInfo.time, endInfo.time, startInfo.hasInfo, endInfo.hasInfo);
        if (!canAddNewEvent(newEvent)) {
            throw new NPExceptions("Slot " + startTime + " - "
                    + endTime + " on " + startDay + " is already occupied. You can't attend this class.");
        }
        taskList.add(newEvent);
        listSize++;
    }

    // Harsha - This method check if slots are clashing or not.
    public boolean canAddNewEvent(Event newEvent) {
        boolean overlap = false;
        for (Event event : taskList) {
            if (newEvent.getStartTime().isBefore(event.getEndTime())
                    && newEvent.getEndTime().isAfter(event.getStartTime())) {
                overlap = true;
                break;
            }
        }
        return !overlap;
    }

    public void reviseTimeInfo(int index, String startTime, String startDay, String endTime,
                               String endDay) throws NPExceptions {
        TimeAndFlag startInfo = convertToTimeInfo(startTime, startDay);
        TimeAndFlag endInfo = convertToTimeInfo(endTime, endDay);

        taskList.get(index).changeTimeInfo(startInfo.time, endInfo.time, startInfo.hasInfo, endInfo.hasInfo);
    }

    public void reviseTimeInfo(int index, String startTime, String startDay) throws NPExceptions {
        TimeAndFlag startInfo = convertToTimeInfo(startTime, startDay);

        taskList.get(index).changeTimeInfo(startInfo.time, startInfo.hasInfo);
    }

    //need handle exceptions when index = -1
    public void reviseTimeInfo(String description, String startTime, String startDay, String endTime,
                               String endDay) throws NPExceptions {
        int index = searchTaskIndex(description);
        if(index == -1){
            throw new NPExceptions("Event cannot be found!");
        }
        reviseTimeInfo(index, startTime, startDay, endTime, endDay);
    }

    //need handle exceptions when index = -1
    public void reviseTimeInfo(String description, String startTime, String startDay) throws NPExceptions {
        int index = searchTaskIndex(description);
        if(index == -1){
            throw new NPExceptions("Event cannot be found!");
        }
        reviseTimeInfo(index, startTime, startDay);
    }

    public ArrayList<Event> getFullList() {
        return this.taskList;
    }

    public int searchTaskIndex(String description) {
        int index = 0;
        for(Event cur: taskList) {
            if(cur.getDescription().trim().equals(description.trim())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public String getLastTaskDescription() {
        return taskList.get(listSize-1).toString();
    }

    public void deleteAll() {
        this.taskList = new ArrayList<Event>();
        this.listSize = 0;
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
