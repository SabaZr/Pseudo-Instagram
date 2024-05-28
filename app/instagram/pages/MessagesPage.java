package app.instagram.pages;

import app.instagram.database.Database;
import app.instagram.objects.Chat;
import app.instagram.objects.Group;
import app.instagram.objects.Message;
import app.instagram.objects.User;
import app.instagram.utilities.ConsoleConstants;
import app.instagram.utilities.ConsoleHelper;

import java.util.ArrayList;
import java.util.List;

public class MessagesPage {

    private static final int EXIT = 0;
    private static final int VIEW_CHATS = 1;
    private static final int VIEW_GROUPS = 2;
    private static final int NEW_MESSAGE = 3;
    private static final int CREATE_GROUP = 4;

    private static final int MESSAGE = 1;
    private static final int REPLY = 2;

    private static final int ENTER_CHAT = 1;
    private static final int VIEW_MEMBERS = 2;
    private static final int ADD_USER = 3;
    private static final int REMOVE_USER = 4;

    private User user;
    private ConsoleHelper consoleHelper;

    private int _from = 0;
    private int _to = 4;

    public MessagesPage(User user, ConsoleHelper consoleHelper) {
        this.user = user;
        this.consoleHelper = consoleHelper;
    }

    public void show() {
        boolean exited = false;
        while (!exited) {
            int option = this.consoleHelper.readInRange(ConsoleConstants.MESSAGE_PAGE, _from, _to);

            switch (option) {
                case EXIT:
                    exited = true;
                    break;
                case VIEW_CHATS:
                    showChats();
                    break;
                case VIEW_GROUPS:
                    showGroups();
                    break;
                case NEW_MESSAGE:
                    newMessage();
                    break;
                case CREATE_GROUP:
                    createGroup();
                    break;
            }
        }
    }

    private void showChats() {
        this.consoleHelper.println(ConsoleConstants.CHATS);

        for (Chat chat : this.user.getChats())
            this.consoleHelper.println(chat);

        showChatsOptions();
    }

    private void showChatsOptions() {
        int chatId = (int) this.consoleHelper.read(ConsoleConstants.CHATS_OPTIONS,
                ConsoleHelper.INTEGER);

        if (chatId > 0) {
            Chat chat = getChat(chatId);

            if (chat == null) {
                this.consoleHelper.println(ConsoleConstants.NOT_404_FOUND);
            } else {
                showChat(chat);
            }
        }
    }

    private void showChat(Chat chat) {
        getMessages(chat);

        showChatOptions(chat);
    }

    private void getMessages(Chat chat) {
        this.consoleHelper.println(ConsoleConstants.MESSAGES);

        for (Message message : chat.getMessages())
            this.consoleHelper.println(message);
    }

    private void showChatOptions(Chat chat) {
        boolean exited = false;
        while (!exited) {
            int option = this.consoleHelper.readInRange(ConsoleConstants.CHAT_OPTIONS, 0, 3);
            switch (option) {
                case EXIT:
                    exited = true;
                    break;
                case MESSAGE:
                    sendMessage(chat);
                    break;
                case REPLY:
                    replyMessage(chat);
                    break;
            }
            if (option != EXIT)
                getMessages(chat);
        }
    }

    private void sendMessage(Chat chat) {
        String messageBody = (String) this.consoleHelper.read(ConsoleConstants.CHAT_MESSAGE,
                ConsoleHelper.STRING);

        if (!this.consoleHelper.isExit(messageBody)) {
            if (messageBody.isEmpty()) {
                this.consoleHelper.println(ConsoleConstants.EMPTY_INPUT);
                sendMessage(chat);
            } else {
                Message message = new Message(chat.generateMessageId(), this.user,
                        chat.getReceiver(), messageBody);
                chat.sendMessage(message);
            }
        }
    }

    private void replyMessage(Chat chat) {
        int chatId = (int) this.consoleHelper.read(ConsoleConstants.CHAT_REPLY,
                ConsoleHelper.INTEGER);

        if (chatId > 0) {
            Message message = getMessage(chat, chatId);

            if (message == null) {
                this.consoleHelper.println(ConsoleConstants.EMPTY_INPUT);
                replyMessage(chat);
            } else {
                replyToMessage(chat, message);
            }
        }
    }

    private void replyToMessage(Chat chat, Message message) {
        String replyBody = (String) this.consoleHelper.read(ConsoleConstants.CHAT_REPLY_MESSAGE,
                ConsoleHelper.STRING);

        if (!this.consoleHelper.isExit(replyBody)) {
            if (replyBody.isEmpty()) {
                this.consoleHelper.println(ConsoleConstants.EMPTY_INPUT);
                replyToMessage(chat, message);
                return;
            }

            Message replyMessage = new Message(chat.generateMessageId(), this.user,
                    chat.getReceiver(), replyBody);
            message.reply(replyMessage);
        }
    }

    private void newMessage() {
        getFollowings();
        int userId = (int) this.consoleHelper.read(ConsoleConstants.CHAT_NEW_MESSAGE,
                ConsoleHelper.INTEGER);

        if (userId > 0) {
            User receiver = getUser(userId);

            if (receiver == null) {
                this.consoleHelper.println(ConsoleConstants.NOT_404_FOUND);
                newMessage();
                return;
            }

            Chat chat = getUserChat(userId);

            if (chat == null) {
                chat = this.user.newChat(receiver);
                receiver.joinChat(chat);
            }
            sendMessage(chat);
        }
    }

    private void showGroups() {
        this.consoleHelper.println(ConsoleConstants.GROUPS);
        List<Group> groups = getGroups();
        printGroups(groups);

        int groupId = (int) this.consoleHelper.read(ConsoleConstants.GROUPS_OPTIONS,
                ConsoleHelper.INTEGER);

        if (groupId > 0) {
            Group group = getGroup(groups, groupId);

            if (group == null) {
                this.consoleHelper.println(ConsoleConstants.NOT_404_FOUND);
                showGroups();
            } else {
                showGroupOptions(group);
            }
        }
    }

    private void showGroupOptions(Group group) {
        boolean exited = false;
        while (!exited) {
            int option = this.consoleHelper.readInRange(ConsoleConstants.GROUP_OPTIONS, 0, 3);

            switch (option) {
                case EXIT:
                    exited = true;
                    break;
                case ENTER_CHAT:
                    enterChat(group);
                    break;
                case VIEW_MEMBERS:
                    showMembers(group);
                    break;
                case ADD_USER:
                    addUser(group);
                    break;
                case REMOVE_USER:
                    removeUser(group);
                    break;
            }
            if (option != EXIT)
                printGroups(getGroups());
        }
    }

    private void enterChat(Group group) {
        getMessages(group.getChat());

        boolean exited = false;
        while (!exited) {
            int option = this.consoleHelper.readInRange(ConsoleConstants.GROUP_CHAT_OPTIONS, 0, 2);

            switch (option) {
                case EXIT:
                    exited = true;
                    break;
                case MESSAGE:
                    sendMessage(group.getChat());
                    break;
                case REPLY:
                    replyMessage(group.getChat());
                    break;
            }
            if(option != EXIT)
                getMessages(group.getChat());
        }

    }

    private void showMembers(Group group) {
        this.consoleHelper.println(ConsoleConstants.MEMBERS);

        for (User user : group.getMembers())
            this.consoleHelper.println(user);
    }

    private void addUser(Group group) {
        getFollowings();

        int userId = (int) this.consoleHelper.read(ConsoleConstants.GROUP_ADD_USER, ConsoleHelper.INTEGER);

        if (userId > 0) {
            User user = getUser(userId);

            if (user == null) {
                this.consoleHelper.println(ConsoleConstants.NOT_404_FOUND);
                addUser(group);
            } else {
                if (group.isMember(user)) {
                    this.consoleHelper.println(ConsoleConstants.ALREADY_MEMBER);
                    addUser(group);
                    return;
                }

                group.join(user);
            }
        }
    }

    private void removeUser(Group group) {
        getMembers(group);

        int userId = (int) this.consoleHelper.read(ConsoleConstants.GROUP_REMOVE_USER,
                ConsoleHelper.INTEGER);

        if (userId > 0) {
            User user = getUser(userId);

            if (user == null) {
                this.consoleHelper.println(ConsoleConstants.NOT_404_FOUND);
                removeUser(group);
            } else {

                if (user.getId() == this.user.getId()) {
                    this.consoleHelper.println(ConsoleConstants.SELF_DESTRUCTION);
                    removeUser(group);
                    return;
                }

                if (group.isOwner(user)){
                    this.consoleHelper.println(ConsoleConstants.ADMIN_DESTRUCTION);
                    removeUser(group);
                    return;
                }

                if (!group.isMember(user)) {
                    this.consoleHelper.println(ConsoleConstants.NOT_A_MEMBER);
                    removeUser(group);
                    return;
                }

                group.leave(user);
            }
        }
    }

    private void createGroup() {
        String groupName = (String) this.consoleHelper.read(ConsoleConstants.CREATE_GROUP,
                ConsoleHelper.STRING);

        if (!this.consoleHelper.isExit(groupName)) {
            if (groupName.isEmpty()) {
                this.consoleHelper.println(ConsoleConstants.EMPTY_INPUT);
                createGroup();
                return;
            }

            Group group = new Group(Database.generateGroupId(), this.user, groupName);
            group.join(this.user);
            Database.Groups.add(group);
        }
    }

    private void getFollowings() {
        for (User user : this.user.getFollowings())
            this.consoleHelper.println(user);
    }

    private User getUser(int userId) {
        for (User user : this.user.getFollowings())
            if (user.getId() == userId)
                return user;
        return null;
    }

    private Chat getUserChat(int userId) {
        for (Chat chat : this.user.getChats())
            if (chat.getReceiver().getId() == userId)
                return chat;
        return null;
    }

    private Chat getChat(int chatId) {
        for (Chat chat : this.user.getChats())
            if (chat.getId() == chatId)
                return chat;
        return null;
    }

    private Message getMessage(Chat chat, int messageId) {
        for (Message message : chat.getMessages())
            if (message.getId() == messageId)
                return message;
        return null;
    }

    private void getMembers(Group group) {
        for (User user : group.getMembers())
            this.consoleHelper.println(user);
    }

    private Group getGroup(List<Group> groups, int groupId) {
        for (Group group : groups)
            if (group.getId() == groupId)
                return group;
        return null;
    }

    private List<Group> getGroups() {
        List<Group> groups = new ArrayList<>();
        for (Group group : Database.Groups)
            if (group.isMember(this.user))
                groups.add(group);
        return groups;
    }

    private void printGroups(List<Group> groups) {
        for (Group group : groups)
            this.consoleHelper.println(group);
    }
}
