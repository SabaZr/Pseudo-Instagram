package app.instagram.pages;

import app.instagram.database.Database;
import app.instagram.objects.User;
import app.instagram.utilities.ConsoleConstants;
import app.instagram.utilities.ConsoleHelper;

import java.util.ArrayList;
import java.util.List;

public class Discover {

//    private static final

    private User user;
    private ConsoleHelper consoleHelper;

    public Discover(User user, ConsoleHelper consoleHelper) {
        this.user = user;
        this.consoleHelper = consoleHelper;
    }

    public void show() {
        boolean exited = false;
        while (!exited) {
            String option = (String) this.consoleHelper.read(ConsoleConstants.DISCOVER,
                    ConsoleHelper.STRING);

            if (this.consoleHelper.isExit(option)) {
                exited = true;
                continue;
            }

            if(option.isEmpty()) {
                this.consoleHelper.println(ConsoleConstants.EMPTY_INPUT);
                continue;
            }

            searchUser(option);
        }
    }

    private void searchUser(String username) {
        List<User> users = getUsers(username);

        if (users.isEmpty()) {
            this.consoleHelper.println(ConsoleConstants.NOT_404_FOUND);
        }else {
            showSearchResult(users);
        }
    }

    private void showSearchResult(List<User> users) {
        this.consoleHelper.println(ConsoleConstants.SEARCH_RESULTS);

        for (User user : users)
            this.consoleHelper.println(user);
        showUserOptions(users);
    }

    private void showUserOptions(List<User> users) {
        int userId = (int) this.consoleHelper.read(ConsoleConstants.USER_OPTIONS,
                ConsoleHelper.INTEGER);
        if (userId > 0) {
            User user = getUser(users, userId);

            if (user == null) {
                this.consoleHelper.println(ConsoleConstants.NOT_404_FOUND);
            }else {
                new UserPage(this.user, user, this.consoleHelper).show();
            }
        }
    }

    private List<User> getUsers(String username) {
        List<User> result = new ArrayList<>();
        for(User user : Database.Users)
            if(user.getUsername().contains(username))
                result.add(user);
        return result;
    }

    private User getUser(List<User> users, int id) {
        for (User user : users) {
            if(user.getId() == id)
                return user;
        }
        return null;
    }
}
