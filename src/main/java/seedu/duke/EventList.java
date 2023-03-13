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

    public void deleteThisTask(int index) {
        taskList.remove(index);
        listSize--;
    }

    public ArrayList<Event> fullList() {
        return this.taskList;
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
     * if user doesn't input endDay(which means there is also no endTime), 
     * you can just call .addEvent(description, startTime, startDay)
     * 
     * I also make the specific time(hh:mm) optional, so if user doesn't input the specfic time,
     * you can just pass an empty String to that param and it will handle the rest things
     * e.g. addEvent(descrption, "", startDay, "", endDay)
     *      addEvent(descrption, "", startDay, endTime, endDay)
     *      addEvent(descrption, "", startDay)
     * so only startDay is strictly required.
     * 
     * and the same for reviseTimeInfo()
     */
    public void addEvent(String description, String startTime, String startDay, String endTime,
            String endDay) {

        boolean hasStTime = true;
        boolean hasEdTime = true;
        LocalDateTime combinedStartTime = LocalDateTime.parse(DTINIT, dfWithTime);
        LocalDateTime combinedEndTime = LocalDateTime.parse(DTINIT, dfWithTime);

        if (startTime.equals("")) {
            hasStTime = false;
            combinedStartTime = changeToDate(startDay);
        } else {
            combinedStartTime = changeToDate(startTime, startDay);
        }

        if (endTime.equals("")) {
            hasEdTime = false;
            combinedEndTime = changeToDate(endDay);
        } else{
            combinedEndTime = changeToDate(startTime, startDay);
        }

        Event newEvent = new Event(description, combinedStartTime, combinedEndTime, hasStTime, hasEdTime);
        taskList.add(newEvent);
        listSize++;
    }

    public void addEvent(String description, String startTime, String startDay, String endTime) {
        boolean hasStTime = true;
        LocalDateTime combinedStartTime = LocalDateTime.parse(DTINIT, dfWithTime);

        if (startTime.equals("")) {
            hasStTime = false;
            combinedStartTime = changeToDate(startDay);
        } else {
            combinedStartTime = changeToDate(startTime, startDay);
        }

        Event newEvent = new Event(description, combinedStartTime, hasStTime);
        taskList.add(newEvent);
        listSize++;
    }
    //tobedone reviseTimeInfo()

    public ArrayList<Event> getFullList() {
        return this.taskList;
    }
}
