package seedu.duke;

import java.util.List;
import java.util.stream.Collectors;

public class SemData {
    public int semester;
    public List<Lesson> timetable;

    public boolean hasExam;
    public String examDate;
    public final int examDuration;

    //Discard covidZone
    public SemData(int semester, List<Lesson> timetable, String examDate, int examDuration) {
        this.semester = semester;
        this.timetable = timetable;
        this.examDate = examDate;
        this.examDuration = examDuration;
        this.hasExam = true;
    }

    public SemData(int semester, List<Lesson> timetable, boolean hasExam){
        this.semester = semester;
        this.timetable = timetable;
        this.hasExam = hasExam;
        this.examDate = null;
        this.examDuration = 0;
    }

    /**
     * Returns a list of lesson objects corresponding to the relevant fields entered.
     * @param type String indicating type of lesson.
     * @param classNum String indicating the classNumber
     * @return List of seedu.duke.Lesson objects (can be multiple, ie CG2111A LAB[XX], CG2023 LEC[XX])
     */
    public List<Lesson> getLesson(String type, String classNum){
        return timetable.stream().filter(lesson ->
                        (lesson.lessonType.equalsIgnoreCase(type)) && (lesson.classNumber.equals(classNum)))
                .collect(Collectors.toList());
    }

    public int getSemester() {
        return semester;
    }
}
