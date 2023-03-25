package seedu.duke;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean hasEndInfo;
    private boolean hasStartTime;
    private boolean hasEndTime;

    public Event(String eventDescription, LocalDateTime start, LocalDateTime end, boolean hasSt, boolean hasEd) {
        this.description = eventDescription;
        this.startTime = start;
        this.endTime = end;
        this.hasEndInfo = true;
        this.hasStartTime = hasSt;
        this.hasEndTime = hasEd;
    }

    public Event(String eventDescription, LocalDateTime start, boolean hasSt) {
        this.description = eventDescription;
        this.startTime = start;
        this.hasEndTime = false;
        this.hasStartTime = hasSt;
        this.hasEndInfo = false;
    }

    public String getDescription() {
        return description;
    }

    private String getOutputFormat(LocalDateTime timeInfo, boolean hasTimeDetail) {
        if(!hasTimeDetail) {
            String timeInString = formatter.format(timeInfo).split(" ")[0];
            return timeInString;
        }
        return formatter.format(timeInfo);
    }

    public String getTime() {
        if (hasEndInfo) {
            return getOutputFormat(startTime, hasStartTime) + " to " + getOutputFormat(endTime, hasEndTime);
        } else {
            return getOutputFormat(startTime, hasStartTime);
        }
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

    public String toString() {
        return "[E] " + getDescription() + " (" + getTime() + ")";
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
