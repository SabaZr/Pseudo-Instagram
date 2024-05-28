package app.instagram.database;

import app.instagram.objects.Group;
import app.instagram.objects.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public static final String USERS = "users.file";
    public static final String GROUPS = "groups.file";

    public static List<User> Users = new ArrayList<>();

    public static List<Group> Groups = new ArrayList<>();

    public static boolean read() {
        try {

            File media = new File("media");
            File mediaToUpload = new File("mediaToUpload");

            if (!media.isDirectory())
                media.mkdir();

            if (!mediaToUpload.isDirectory())
                mediaToUpload.mkdir();

            DatabaseHandler<User> userDatabaseHandler = new DatabaseHandler<>(USERS);
            Users = userDatabaseHandler.read();

            DatabaseHandler<Group> groupDatabaseHandler = new DatabaseHandler<>(GROUPS);
            Groups = groupDatabaseHandler.read();

            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean write() {
        try {
            DatabaseHandler<User> userDatabaseHandler = new DatabaseHandler<>(USERS);
            userDatabaseHandler.write(Users);

            DatabaseHandler<Group> groupDatabaseHandler = new DatabaseHandler<>(GROUPS);
            groupDatabaseHandler.write(Groups);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int generateUserId() {
        if (Users.size() == 0)
            return 1;
        else return Users.get(Users.size() - 1).getId() + 1;
    }

    public static int generateGroupId() {
        if (Groups.size() == 0)
            return 1;
        else return Groups.get(Groups.size() - 1).getId() + 1;
    }

}
