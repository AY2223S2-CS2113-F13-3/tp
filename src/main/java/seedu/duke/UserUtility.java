package seedu.duke;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static seedu.duke.Parser.SEMESTER_START_DATES;

// User utility class. This class will hold methods required by user.
public class UserUtility {
    private static User user;

    public static User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public static void printScheduleTable(List<Schedule> events, int currentWeek){
        System.out.println("Showing schedule for semester " +
                UserUtility.getUser().getSemester() + " and week " + currentWeek);
        System.out.println();

        // define the starting and ending times for the table
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(18, 0);

        // define the semester start date and the current week number
        LocalDate semesterStartDate =SEMESTER_START_DATES.get(getUser().getSemester());

        // get the start and end dates for the current week
        LocalDate weekStartDate = semesterStartDate.plusWeeks(currentWeek - 1);
        LocalDate weekEndDate = weekStartDate.plusDays(6);

        // print the table header
        System.out.print(String.format("%-10s|", "TIME"));
        for (DayOfWeek day : DayOfWeek.values()) {
            System.out.print(String.format("%-15s|", day));
        }
        System.out.println();
        System.out.print(String.format("%-10s+", "----------"));
        for (int i = 0; i < DayOfWeek.values().length; i++) {
            System.out.print(String.format("%-15s+", "---------------"));
        }
        System.out.println();

        // loop through the starting times and print the table rows
        LocalTime time = start;
        while (time.isBefore(end)) {
            System.out.print(String.format("%-10s|", time.format(DateTimeFormatter.ofPattern("HH:mm"))));
            for (DayOfWeek day : DayOfWeek.values()) {
                boolean found = false;
                for (Schedule event : events) {
                    LocalDateTime startDateTime = event.getStartTime();
                    LocalDateTime endDateTime = event.getEndTime();
                    if (event.getStartTime().getDayOfWeek() == day && isValidInterval(time, startDateTime.toLocalTime(), endDateTime.toLocalTime())
                            && event.getStartTime().toLocalDate().isAfter(weekStartDate.minusDays(1))
                            && event.getStartTime().toLocalDate().isBefore(weekEndDate.plusDays(1))) {
                        System.out.print(String.format("%-15s|", event.getDescription()));
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.print(String.format("%-15s|", ""));
                }
            }
            System.out.println();
            System.out.print(String.format("%-10s+", ""));
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                System.out.print(String.format("%-15s+", "---------------"));
            }
            System.out.println();
            time = time.plusMinutes(30);
        }
        System.out.println();
        Ui.printDash();
    }

    private static boolean isValidInterval(LocalTime time, LocalTime startTime, LocalTime endTime) {
        return time.equals(startTime) || (time.isAfter(startTime) && time.isBefore(endTime)) || time.equals(endTime);
    }
}
