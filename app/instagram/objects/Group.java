package app.instagram.objects;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private int id;
    private String name;
    private User owner;
    private List<User> members;
//    private List<Message> messages;
    private Chat chat;

    public Group(int id, String name, User owner, List<User> members, Chat chat) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.members = members;
//        this.messages = messages;
        this.chat = chat;
    }

    public Group(int id, User owner, String name) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.members = new ArrayList<>();
//        this.messages = new ArrayList<>();
        chat = new Chat(this.owner.generateChatId(), this.owner);
    }

    public void sendMessage(Message message) {
//        this.messages.add(message);
        this.chat.sendMessage(message);
    }

    public void join(User user) {
        this.members.add(user);
    }

    public void leave(User user) {
        this.members.remove(user);
    }

    public boolean isMember(User user) {
        return this.members.contains(user);
    }

    public boolean isOwner(User user) {
        return user == this.owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

//    public List<Message> getMessages() {
//        return messages;
//    }
//
//    public void setMessages(List<Message> messages) {
//        this.messages = messages;
//    }


    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public int generateMessageId() {
        if (getChat().getMessages().size() == 0)
            return 1;
        else return getChat().getMessages().get(getChat().getMessages().size() - 1).getId() + 1;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", name='" + name + '\'' +
                ", Number of members=" + members.size() +
                ", Number of messages=" + getChat().getMessages().size();
    }
}
