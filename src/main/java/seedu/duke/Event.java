package seedu.duke;

import java.time.LocalDateTime;

public class Event extends Schedule {

    private String description;

    public Event(String eventDescription, LocalDateTime start, LocalDateTime end, boolean hasSt,
            boolean hasEd) {
        super(start, end, hasSt, hasEd);
        this.description = eventDescription;
    }

    public Event(String eventDescription, LocalDateTime start, boolean hasSt) {
        super(start, hasSt);
        this.description = eventDescription;
    }

    public Event(String eventDescription, LocalDateTime start, LocalDateTime end, boolean hasSt,
            boolean hasEd, String recurTime) {
        super(start, end, hasSt, hasEd, recurTime);
        this.description = eventDescription;
    }

    public Event(String eventDescription, LocalDateTime start, boolean hasSt, String recurTime) {
        super(start, hasSt, recurTime);
        this.description = eventDescription;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return "[E] " + getDescription() + " (" + super.getTime() + ")";
    }

}
