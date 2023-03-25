package seedu.duke;

// This class will hold methods required by user
public class UserUtility {
    private static User user;

    public static User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }
}
