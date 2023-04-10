package seedu.duke;

import java.util.ArrayList;

// import seedu.duke.storage.EventListAdapter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static seedu.duke.Parser.SEMESTER_START_DATES;
import static seedu.duke.Parser.SEMESTER_END_DATES;
import static seedu.duke.UserUtility.getUser;


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

    private static LocalDateTime changeToDate(String time, String date) {
        String combination = date + " " + time;
        return LocalDateTime.parse(combination, dfWithTime);
    }

    private static LocalDateTime changeToDate(String date) {
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
    public static TimeAndFlag convertToTimeInfo(String time, String day) throws NPExceptions {
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

    private static boolean checkSingleOverlap(Schedule eventA, Schedule eventB) {
        return ((eventA.getStartTime().compareTo(eventB.getStartTime()) >= 0
                && eventA.getStartTime().compareTo(eventB.getEndTime()) <= 0)
                || (eventA.getEndTime().compareTo(eventB.getStartTime()) >= 0
                        && eventA.getEndTime().compareTo(eventB.getEndTime()) <= 0));
    }

    public static boolean canAddNewEvent(Event newEvent, int index, ArrayList<Schedule> eventList) {
        boolean isOverlap = true;
        for (int i = 0; i < eventList.size(); i++) {
            if (!eventList.get(i).hasEndInfo() || i == index) {
                continue;
            }

            Event eventA = (eventList.get(i) instanceof Event) ? (Event) eventList.get(i) : null;
            Event eventB = newEvent;

            LocalDate semStart = SEMESTER_START_DATES.get(getUser().getSemester());
            LocalDate semEnd = SEMESTER_END_DATES.get(getUser().getSemester());
            LocalDate eventADate = eventA.getStartTime().toLocalDate();
            LocalDate eventBDate = eventB.getStartTime().toLocalDate();

            long minKa = !eventA.isRecurring() || eventADate.isAfter(semStart) ? 0
                    : (semStart.toEpochDay() - eventADate.toEpochDay()) / eventA.getActualInterval();
            long maxKa = !eventA.isRecurring() || eventADate.isAfter(semEnd) ? 0
                    : (semEnd.toEpochDay() - eventADate.toEpochDay()) / eventA.getActualInterval();
            long minKb = !eventB.isRecurring() || eventBDate.isAfter(semStart) ? 0
                    : (semStart.toEpochDay() - eventBDate.toEpochDay()) / eventB.getActualInterval();
            long maxKb = !eventB.isRecurring() || eventBDate.isAfter(semEnd) ? 0
                    : (semEnd.toEpochDay() - eventBDate.toEpochDay()) / eventB.getActualInterval();
            boolean breakFlag = false;

            for (long j = minKa; j <= maxKa; j++) {
                for (long k = minKb; k <= maxKb; k++) {
                    Schedule curA = new Schedule(eventA.getStartTime(), eventA.getEndTime(), true, true);
                    Schedule curB = new Schedule(eventB.getStartTime(), eventB.getEndTime(), true, true);

                    if (maxKa != 0) {
                        curA = new Schedule(eventA.getStartTime().plusDays(eventA.getActualInterval() * j),
                                eventA.getEndTime().plusDays(eventA.getActualInterval() * j), true, true);
                    }
                    if (maxKb != 0) {
                        curB = new Schedule(eventB.getStartTime().plusDays(eventB.getActualInterval() * k),
                                eventB.getEndTime().plusDays(eventB.getActualInterval() * k), true, true);
                    }

                    if ((checkSingleOverlap(curA, curB) || checkSingleOverlap(curB, curA))) {
                        isOverlap = false;

                        Ui.printOverlapInfo(eventA.toString(), curA.getTime());
                        breakFlag = true;
                        break;
                    }
                }
                if (breakFlag) {
                    break;
                }
            }
        }
        return isOverlap;
    }

    public static Event getInEventType(String description, String startTime, String startDay, String endTime,
            String endDay) throws NPExceptions {

        TimeAndFlag startInfo = convertToTimeInfo(startTime, startDay);
        TimeAndFlag endInfo = convertToTimeInfo(endTime, endDay);
        Event newEvent =
                new Event(description, startInfo.time, endInfo.time, startInfo.hasInfo, endInfo.hasInfo);

        LocalDateTime startDate = newEvent.getStartTime();
        LocalDateTime endDate = newEvent.getEndTime();
        UserUtility.validateDates(startDate, endDate);

        if (newEvent.getStartTime().isAfter(newEvent.getEndTime())) {
            throw new NPExceptions("Starting time is after ending time!");
        }

        return newEvent;
    }

    public void addEvent(String description, String startTime, String startDay, String endTime, String endDay)
            throws NPExceptions {


        Event newEvent = getInEventType(description, startTime, startDay, endTime, endDay);

        LocalDateTime startDate = newEvent.getStartTime();
        LocalDateTime endDate = newEvent.getEndTime();
        UserUtility.validateDates(startDate, endDate);

        if (!canAddNewEvent(newEvent, -1, taskList)) {
            throw new NPExceptions("Events/classes conflition!");
        }

        taskList.add(newEvent);
        listSize++;
        assert taskList.size() == listSize : "size of taskList is different from listSize attribute";
    }

    public void addEvent(String description, String startTime, String startDay) throws NPExceptions {
        TimeAndFlag startInfo = convertToTimeInfo(startTime, startDay);

        Event newEvent = new Event(description, startInfo.time, startInfo.hasInfo);

        LocalDateTime startDate = newEvent.getStartTime();
        LocalDateTime endDate = newEvent.getEndTime();
        UserUtility.validateDates(startDate, endDate);

        taskList.add(newEvent);
        listSize++;
        assert taskList.size() == listSize : "size of taskList is different from listSize attribute";
    }

    public void addEvent(String description, String startTime, String startDay, String recurTime)
            throws NPExceptions {
        TimeAndFlag startInfo = convertToTimeInfo(startTime, startDay);

        Event newEvent = new Event(description, startInfo.time, startInfo.hasInfo, recurTime);
        taskList.add(newEvent);
        listSize++;
        assert taskList.size() == listSize : "size of taskList is different from listSize attribute";
    }

    public void addEvent(String description, String startTime, String startDay, String endTime, String endDay,
            String recurTime) throws NPExceptions {

        TimeAndFlag startInfo = convertToTimeInfo(startTime, startDay);
        TimeAndFlag endInfo = convertToTimeInfo(endTime, endDay);

        Event newEvent = new Event(description, startInfo.time, endInfo.time, startInfo.hasInfo,
                endInfo.hasInfo, recurTime);

        if (newEvent.getStartTime().isAfter(newEvent.getEndTime())) {
            throw new NPExceptions("Starting time is after ending time!");
        }
        if (!canAddNewEvent(newEvent, -1, taskList)) {
            throw new NPExceptions("Events/classes conflition!");
        }

        taskList.add(newEvent);
        listSize++;
        assert taskList.size() == listSize : "size of taskList is different from listSize attribute";
    }

    public void addEvent(ArrayList<Schedule> allClasses, ArrayList<String> allVenues) throws NPExceptions{
        for(int i =0; i <allClasses.size(); i++) {
            Event curClass = (Event) allClasses.get(i);
            if(!canAddNewEvent(curClass, -1, taskList)){
                throw new NPExceptions("class clashes with events/other modules!");
            }
        }
        for(int i =0; i <allClasses.size(); i++) {
            Event curClass = (Event) allClasses.get(i);
            taskList.add(curClass);
            reviseLocation(taskList.size()-1, allVenues.get(i));
        }
        listSize = taskList.size();
        assert taskList.size() == listSize : "size of taskList is different from listSize attribute";
    }

    public void reviseLocation(int index, String location) throws NPExceptions {
        taskList.get(index).changeLocation(location);
    }

    public void reviseTimeInfo(int index, String startTime, String startDay, String endTime, String endDay)
            throws NPExceptions {
        TimeAndFlag startInfo = convertToTimeInfo(startTime, startDay);
        TimeAndFlag endInfo = convertToTimeInfo(endTime, endDay);

        UserUtility.validateDates(startInfo.time, endInfo.time);
        Event eventToCheck = new Event(taskList.get(index).getDescription(), startInfo.time, endInfo.time,
                startInfo.hasInfo, endInfo.hasInfo);
        if (!canAddNewEvent(eventToCheck, index, taskList)) {
            throw new NPExceptions("Events/classes conflition!");
        }

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
}


final class TimeAndFlag {
    public boolean hasInfo;
    public LocalDateTime time;

    public TimeAndFlag(boolean info, LocalDateTime timeInfo) {
        this.hasInfo = info;
        this.time = timeInfo;
    }
}
