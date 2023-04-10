package seedu.duke;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

// User utility class. This class will hold methods required by user.
public class UserUtility {
    private static User user;

    public static User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public static ArrayList<Event> eventInThisWeek(Event curEvent, LocalDate weekStartDate,
            LocalDate weekEndDate) {
        ArrayList<Event> events = new ArrayList<Event>();

        long timeToStart = weekStartDate.toEpochDay() - curEvent.getStartTime().toLocalDate().toEpochDay();
        int recurTime = curEvent.getActualInterval();
        int remdays = (int) timeToStart % recurTime;

        LocalDateTime stTime = curEvent.getStartTime();
        LocalDateTime edTime = curEvent.getEndTime();
        if (remdays + 7 > recurTime) {
            if (remdays == 0) {
                events.add(new Event(curEvent.getDescription(), stTime, edTime, true, true,
                        curEvent.getTimeInterval()));
            }

            stTime = stTime.plusDays(recurTime);
            edTime = edTime.plusDays(recurTime);

            while ((stTime.toLocalDate().toEpochDay() - weekStartDate.toEpochDay()) < 7) {

                events.add(new Event(curEvent.getDescription(), stTime, edTime, true, true,
                        curEvent.getTimeInterval()));
                stTime = stTime.plusDays(recurTime);
                edTime = edTime.plusDays(recurTime);
            }
        }

        return events;
    }

    static void drawEventThisWeek(ArrayList<Schedule> eventToShow, LocalDateTime time,
                                  LocalDate weekStartDate, LocalDate weekEndDate) {

        for (DayOfWeek day : DayOfWeek.values()) {
            boolean found = false;
            for (Schedule event : eventToShow) {
                LocalDateTime startDateTime = event.getStartTime();
                LocalDateTime endDateTime = event.getEndTime();

                if (startDateTime.toLocalTime().getMinute() > 0
                        && startDateTime.toLocalTime().getMinute() < 30) {
                    startDateTime = LocalDateTime.of(startDateTime.toLocalDate(),
                            LocalTime.of(startDateTime.getHour(), 0));
                }
                if (endDateTime.toLocalTime().getMinute() > 30
                        && startDateTime.toLocalTime().getMinute() <= 59) {
                    endDateTime = LocalDateTime.of(endDateTime.toLocalDate(),
                            LocalTime.of(endDateTime.getHour() + 1, 0));
                }

                if (time.getDayOfWeek() == day && isValidInterval(time, startDateTime, endDateTime)
                        && time.toLocalDate().isAfter(weekStartDate.minusDays(1))
                        && time.toLocalDate().isBefore(weekEndDate.plusDays(1))) {
                    System.out.print(String.format("%-15s|", event.getDescription().substring(0,
                            Math.min(event.getDescription().length(), 15))));
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.print(String.format("%-15s|", ""));
            }
            time = time.plusDays(1);
        }
    }

    private static boolean isValidInterval(LocalDateTime time, LocalDateTime startTime,
            LocalDateTime endTime) {
        return time.equals(startTime) || (time.isAfter(startTime) && time.isBefore(endTime))
                || time.equals(endTime);
    }

    public static void validateDates(LocalDateTime startDate, LocalDateTime endDate) throws NPExceptions {
        if (endDate != null && !endDate.isAfter(startDate)){
            throw new NPExceptions("Event end date time should be after start date time.");
        }
        LocalDateTime semStart = LocalDateTime.of(Parser.SEMESTER_START_DATES.get(getUser().getSemester()), LocalTime.of(0, 0));
        String semEndString = Parser.findDateOfWeek(getUser().getSemester(), 14, "FRIDAY");
        LocalDateTime semEnd = LocalDateTime.parse(semEndString + " 23:59", DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
        if (startDate.isBefore(semStart) || startDate.isAfter(semEnd)){
            throw new NPExceptions("Event start date time should be within semester dates.");
        }
        if (endDate != null && endDate.isAfter(semEnd)){
            throw new NPExceptions("Event end date time should be before semester dates.");
        }
        if (endDate != null && endDate.isBefore(semStart)){
            throw new NPExceptions("Event end date time should be before semester dates.");
        }
    }
}
