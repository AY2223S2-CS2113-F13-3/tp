package seedu.duke;

import java.util.HashMap;
import java.util.List;

public class NusModule {
    public String description;
    public String title;
    public String faculty;
    public String moduleCredit;
    public String moduleCode;
    //public List<SemData> semesterData;
    public HashMap<Integer, SemData> semesterData;

    public NusModule(String description, String title, String faculty, String moduleCredit,
                     String moduleCode, HashMap<Integer, SemData> semesterData) {
        this.description = description;
        this.title = title;
        this.faculty = faculty;
        this.moduleCredit = moduleCredit;
        this.moduleCode = moduleCode;
        this.semesterData = semesterData;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getModuleCredit() {
        return moduleCredit;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public List<Lesson> getLesson(int semesterNumber, String type, String classNum) {
        SemData semester = semesterData.get(semesterNumber);
        if (semester != null)
            return semester.getLesson(type, classNum);
        return null;
    }
}

