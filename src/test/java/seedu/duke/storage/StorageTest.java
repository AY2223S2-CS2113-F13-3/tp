package seedu.duke.storage;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import seedu.duke.EventList;
import seedu.duke.NPExceptions;

import java.io.File;
import java.io.FileNotFoundException;

class StorageTest {

    private static final String fileLocation = System.getProperty("user.dir") + "/save.json";
    File saveFile = new File(fileLocation);
    private final Storage storage = new Storage();
    @Test
    @Order(1)
    public void emptyLoad(){ //check that it can load when there isn't an instance of save.json in the path.
        saveFile.delete();
        EventList testList = new EventList(storage.loadEvents());
        storage.loadEvents();
        assert((testList.getSize()==0) && (!saveFile.exists()));
    }
    @Test
    @Order(2)
    public void saveEvents() throws NPExceptions{ //check that storage class saves a file
        EventList testList = new EventList();
        testList.addEvent("testing", "10:00", "2023/03/20", "10:00", "2023/03/21");
        testList.addEvent("testing2","03:24", "2023/04/01", "08:50", "2023/03/23");
        storage.saveToFile(testList);
        assert(saveFile.exists());
        saveFile.delete();
    }


    @Test
    @Order(3)
    public void loadEvents() throws FileNotFoundException, NPExceptions {
        //checks that storage EventList from deserialized save.json matches original EventList data
        EventList original = new EventList();
        original.addEvent("testing", "10:00", "2023/03/20",
                "10:00", "2023/03/21");
        original.addEvent("testing2","03:24", "2023/04/01",
                "08:50", "2023/03/23");
        storage.saveToFile(original);
        EventList testListCheck = new EventList();
        testListCheck.addEvent("testing", "10:00", "2023/03/20",
                "10:00", "2023/03/21");
        testListCheck.addEvent("testing2","03:24", "2023/04/01",
                "08:50", "2023/03/23");
        EventList testList = new EventList(storage.loadEvents());
        String a = testListCheck.getFullList().toString();
        String b = testList.getFullList().toString();
        assert(a.equals(b));
        saveFile.delete();
    }
    @Test
    @Order(4)
    public void updateEvents() throws NPExceptions{ //Check that data can be updated and matches what was updated.
        EventList original = new EventList();
        original.addEvent("testing", "10:00", "2023/03/20",
                "10:00", "2023/03/21");
        original.addEvent("testing2","03:24", "2023/04/01",
                "08:50", "2023/03/23");
        storage.saveToFile(original);
        EventList edited = new EventList(storage.loadEvents());
        edited.addEvent("edit new event", "19:00", "2023/03/21", "13:00", "2023/03/22");
        storage.saveToFile(edited);
        EventList savedEvent =  new EventList(storage.loadEvents());
        String a = edited.getFullList().toString();
        String b = savedEvent.getFullList().toString();
        assert(a.equals(b));
        saveFile.delete();
    }
}
