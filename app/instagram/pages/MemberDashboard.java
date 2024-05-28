package app.instagram.pages;

import app.instagram.database.Database;
import app.instagram.objects.User;
import app.instagram.utilities.ConsoleConstants;
import app.instagram.utilities.ConsoleHelper;

public class MemberDashboard {

    private static final int EXIT = 0;
    private static final int HOME_PAGE = 1;
    private static final int DISCOVER = 2;
    private static final int MESSAGES = 3;
    private static final int PROFILE = 4;

    private final ConsoleHelper consoleHelper;
    private User user;

    private final int _menuFrom = 0;
    private final int _menuTo = 4;

    public MemberDashboard(User user, ConsoleHelper consoleHelper) {
        this.user = user;
        this.consoleHelper = consoleHelper;
    }

    public void show() {
        while (this.user != null) {
            int menuAction = this.consoleHelper.readInRange(ConsoleConstants.MEMBER_DASH,
                    _menuFrom, _menuTo);

            switch (menuAction) {
                case EXIT:
                    this.user = null;
                    break;
                case HOME_PAGE:
                    new HomePage(this.user, this.consoleHelper).show();
                    break;
                case DISCOVER:
                    new Discover(this.user, this.consoleHelper).show();
                    break;
                case MESSAGES:
                    new MessagesPage(this.user, this.consoleHelper).show();
                    break;
                case PROFILE:
                    new ProfilePage(this.user, this.consoleHelper).show();
                    break;
            }
            Database.write();
        }
    }
}
