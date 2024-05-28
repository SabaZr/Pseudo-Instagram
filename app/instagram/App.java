package app.instagram;

import app.instagram.objects.User;
import app.instagram.pages.Authentication;
import app.instagram.pages.MemberDashboard;
import app.instagram.pages.UserTypeSelection;
import app.instagram.utilities.ConsoleConstants;
import app.instagram.utilities.ConsoleHelper;

import java.lang.reflect.Member;

public class App {

    private static ConsoleHelper consoleHelper;

    public static void Run() {
        if (consoleHelper == null)
            consoleHelper = new ConsoleHelper();

        consoleHelper.println(ConsoleConstants.WELCOME_MESSAGE);
        while (true) {
            int type = new UserTypeSelection().select();
            if (type == -1)
                continue;

            int registered = consoleHelper.readInRange(ConsoleConstants.IS_REGISTERED, 1, 2);
            User user = null;

            if (registered == User.REGISTERED)
                user = new Authentication().authenticate(type);
            else if (registered == User.NOT_REGISTERED)
                user = new Authentication().register(type);

            if (user == null)
                continue;

            if (type == User.MEMBER) {
                new MemberDashboard(user, consoleHelper).show();
            }
//            else if (type == User.ADMIN) {
//                consoleHelper.println("is an admin");
//            }
        }
    }
}
