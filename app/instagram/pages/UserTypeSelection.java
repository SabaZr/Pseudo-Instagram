package app.instagram.pages;
import app.instagram.utilities.ConsoleConstants;
import app.instagram.utilities.ConsoleHelper;

import java.io.IOException;

public class UserTypeSelection {

    private ConsoleHelper consoleHelper;

    public UserTypeSelection() {
        this.consoleHelper = new ConsoleHelper();
    }

    public int select() {
        try {
            return chooseType(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int chooseType(String note) throws IOException {
        if (note != null)
            this.consoleHelper.println(note);
        int type = (int) this.consoleHelper.read(ConsoleConstants.CHOOSE_TYPE, ConsoleHelper.INTEGER);

        if (type != 1)
            chooseType(ConsoleConstants.ILLEGAL_INPUT);
        return type;
    }
}
