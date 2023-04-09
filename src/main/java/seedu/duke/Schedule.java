package seedu.duke;

// import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class Schedule {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean hasEndInfo;
    private boolean hasStartTime;
    private boolean hasEndTime;

    private boolean isRecurring;
    // format: x D for x day, or x W for x weeks and x M for x month.
    private String timeInterval;

    private String location;
    private boolean hasLocation;



    public Schedule(LocalDateTime start, boolean hasSt) {
        this.startTime = start;
        this.hasEndTime = false;
        this.hasStartTime = hasSt;
        this.hasEndInfo = false;
        this.isRecurring = false;
        this.hasLocation = false;
        this.location = "";
    }

    public Schedule(LocalDateTime start, boolean hasSt, String recurringTime) {
        this.startTime = start;
        this.hasEndTime = false;
        this.hasStartTime = hasSt;
        this.hasEndInfo = false;
        this.isRecurring = true;
        this.timeInterval = recurringTime;
        this.hasLocation = false;
        this.location = "";
    }

    public Schedule(LocalDateTime start, LocalDateTime end, boolean hasSt, boolean hasEd) {
        this.startTime = start;
        this.endTime = end;
        this.hasEndInfo = true;
        this.hasStartTime = hasSt;
        this.hasEndTime = hasEd;
        this.isRecurring = false;
        this.hasLocation = false;
        this.location = "";
    }

    public Schedule(LocalDateTime start, LocalDateTime end, boolean hasSt, boolean hasEd,
                    String recurringTime) {
        this.startTime = start;
        this.endTime = end;
        this.hasEndInfo = true;
        this.hasStartTime = hasSt;
        this.hasEndTime = hasEd;
        this.isRecurring = true;
        this.timeInterval = recurringTime;
        this.hasLocation = false;
        this.location = "";
    }

    public Schedule(LocalDateTime start, String location, boolean hasSt, boolean hasLocation) {
        this.startTime = start;
        this.hasEndTime = false;
        this.hasStartTime = hasSt;
        this.hasEndInfo = false;
        this.isRecurring = false;
        this.hasLocation = hasLocation;
        this.location = location;
    }

    public Schedule(LocalDateTime start, String location, boolean hasSt, boolean hasLocation,
                    String recurringTime) {
        this.startTime = start;
        this.hasEndTime = false;
        this.hasStartTime = hasSt;
        this.hasEndInfo = false;
        this.isRecurring = true;
        this.timeInterval = recurringTime;
        this.hasLocation = hasLocation;
        this.location = location;
    }

    public Schedule(LocalDateTime start, LocalDateTime end, String location, boolean hasSt, boolean hasEd,
                    boolean hasLocation) {
        this.startTime = start;
        this.endTime = end;
        this.hasEndInfo = true;
        this.hasStartTime = hasSt;
        this.hasEndTime = hasEd;
        this.isRecurring = false;
        this.hasLocation = hasLocation;
        this.location = location;
    }

    public Schedule(LocalDateTime start, LocalDateTime end, String location, boolean hasSt, boolean hasEd,
                    boolean hasLocation, String recurringTime) {
        this.startTime = start;
        this.endTime = end;
        this.hasEndInfo = true;
        this.hasStartTime = hasSt;
        this.hasEndTime = hasEd;
        this.isRecurring = false;
        this.timeInterval = recurringTime;
        this.hasLocation = hasLocation;
        this.location = location;
    }

    private String getOutputFormat(LocalDateTime timeInfo, boolean hasTimeDetail) {
        if (!hasTimeDetail) {
            String timeInString = formatter.format(timeInfo).split(" ")[0];
            return timeInString;
        }
        return formatter.format(timeInfo);
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public String getTime() {
        String recur = " not recurring";
        if (isRecurring) {
            String[] details = timeInterval.split(" ");
            String interval = "";
            switch (details[1].trim()) {
            case ("D"):
                interval = "Day(s)";
                break;
            case ("W"):
                interval = "Week(s)";
                break;
            case ("M"):
                interval = "Month(s)";
                break;
            default:
                break;
            }
            recur = " | recurring, time interval: " + details[0] + " " + interval;
        }

        if (hasEndInfo) {
            return getOutputFormat(startTime, hasStartTime) + " to " + getOutputFormat(endTime, hasEndTime)
                    + recur;
        } else {
            return getOutputFormat(startTime, hasStartTime) + recur;
        }
    }

    public void changeTimeInfo(LocalDateTime start, LocalDateTime end, boolean hasSt, boolean hasEd) {
        this.startTime = start;
        this.endTime = end;
        this.hasEndInfo = true;
        this.hasStartTime = hasSt;
        this.hasEndTime = hasEd;
    }

    public void changeTimeInfo(LocalDateTime start, boolean hasSt) {
        this.startTime = start;
        this.hasEndTime = false;
        this.hasStartTime = hasSt;
        this.hasEndInfo = false;
    }

    public int getActualInterval() {
        String[] details = this.timeInterval.split(" ");

        int actualDays = 0;
        switch (details[1].trim()) {
        case ("D"):
            actualDays = Integer.parseInt(details[0].trim());
            break;
        case ("W"):
            actualDays = Integer.parseInt(details[0].trim()) * 7;
            break;
        default:
            break;
        }
        return actualDays;
    }

    // fuinction mayhbe useful in arlarm?
    public ArrayList<Schedule> getNextNTimes(int n) {
        ArrayList<Schedule> list = new ArrayList<>();
        int actualDays = getActualInterval();

        LocalDateTime stTime = startTime;
        LocalDateTime edTime = endTime;

        for (int i = 0; i < n; i++) {
            stTime = stTime.plusDays(actualDays);
            edTime = edTime.plusDays(actualDays);

            list.add(new Schedule(stTime, edTime, true, true, timeInterval));
        }

        return list;
    }

    // need exception
    public void reviseTimeinterval(String recurringTime) {
        this.timeInterval = recurringTime;
    }

    public String getTimeInterval() {
        if (isRecurring) {
            return timeInterval;
        } else {
            return "Not a recurring event!";
        }
    }

    public void changeLocation(String location) {
        if (!location.equals("")) {
            hasLocation = true;
        }
        this.location = location;
    }

    public boolean hasEndTime() {
        return hasEndTime;
    }

    public boolean hasStartTime() {
        return hasStartTime;
    }

    public boolean hasEndInfo() {
        return hasEndInfo;
    }

    public boolean hasLocation() {
        return hasLocation;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Schedule)) {
            return false;
        }

        Schedule schedule = (Schedule) o;
        return getStartTime().equals(schedule.getStartTime()) && Objects.equals(getEndTime(), schedule.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartTime(), getEndTime());
    }
}
