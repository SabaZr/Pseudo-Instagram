package app.instagram;

import app.instagram.database.Database;

public class Main {

    public static void main(String[] args) {
        if (Database.read())
            App.Run();
    }
}
