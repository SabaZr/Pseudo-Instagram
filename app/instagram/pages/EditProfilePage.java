package app.instagram.pages;

import app.instagram.objects.User;
import app.instagram.utilities.ConsoleConstants;
import app.instagram.utilities.ConsoleHelper;

public class EditProfilePage {

    private static final int EXIT = 0;
    private static final int VIEW_PROFILE = 1;
    private static final int EDIT_NAME = 2;
    private static final int EDIT_BIO = 3;
    private static final int EDIT_EMAIL = 4;

    private static final String EMAIL_REGEXP =
            "[a-zA-Z0-9-_.]+@[a-zA-Z0-9]+.(com|org|net|ir|co.uk)";

    private User user;
    private ConsoleHelper consoleHelper;

    private int _from = 0;
    private int _to = 4;

    public EditProfilePage(User user, ConsoleHelper consoleHelper) {
        this.user = user;
        this.consoleHelper = consoleHelper;
    }

    public void show() {
        boolean exited = false;
        while (!exited) {
            int option = this.consoleHelper.readInRange(ConsoleConstants.EDIT_PROFILE, _from, _to);

            switch (option) {
                case EXIT:
                    exited = true;
                    break;
                case VIEW_PROFILE:
                    viewProfile();
                    break;
                case EDIT_NAME:
                    editName();
                    break;
                case EDIT_BIO:
                    editBio();
                    break;
                case EDIT_EMAIL:
                    editEmail();
            }
        }
    }

    private void viewProfile() {
        this.consoleHelper.println(this.user.getProfile());
    }

    private void editName() {
        String firstName = (String) this.consoleHelper.read(ConsoleConstants.FIRST_NAME,
                ConsoleHelper.STRING);

        if (firstName.isEmpty()) {
            this.consoleHelper.println(ConsoleConstants.EMPTY_INPUT);
            editName();
            return;
        }

        String lastName = (String) this.consoleHelper.read(ConsoleConstants.LAST_NAME,
                ConsoleHelper.STRING);

        if (lastName.isEmpty()) {
            this.consoleHelper.println(ConsoleConstants.EMPTY_INPUT);
            editName();
            return;
        }

        this.user.setFirstName(firstName);
        this.user.setLastName(lastName);

        this.consoleHelper.println(ConsoleConstants.EDITED_SUCCESSFULLY);
    }

    private void editBio() {

        String bio = (String) this.consoleHelper.read(ConsoleConstants.BIO, ConsoleHelper.STRING);

        if (bio.isEmpty()) {
            this.consoleHelper.println(ConsoleConstants.EMPTY_INPUT);
            editBio();
            return;
        }

        this.user.setBio(bio);

        this.consoleHelper.println(ConsoleConstants.EDITED_SUCCESSFULLY);
    }

    private void editEmail() {
        String email = (String) this.consoleHelper.read(ConsoleConstants.EMAIL, ConsoleHelper.STRING);

        if (email.isEmpty()) {
            this.consoleHelper.println(ConsoleConstants.EMPTY_INPUT);
            editEmail();
            return;
        }

        if (!email.matches(EMAIL_REGEXP)) {
            this.consoleHelper.println(ConsoleConstants.INVALID_EMAIL);
            editEmail();
            return;
        }

        this.user.setEmail(email);

        this.consoleHelper.println(ConsoleConstants.EDITED_SUCCESSFULLY);
    }
}
