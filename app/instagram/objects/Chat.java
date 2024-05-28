package app.instagram.objects;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    private int id;
    private User receiver;
    private List<Message> messages;

    public Chat(int id, User receiver, List<Message> messages) {
        this.id = id;
        this.receiver = receiver;
        this.messages = messages;
    }

    public Chat(int id, User receiver) {
        this.id = id;
        this.receiver = receiver;
        this.messages = new ArrayList<>();
    }

    public void sendMessage(Message message) {
        this.messages.add(message);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public int generateMessageId() {
        if (messages.size() == 0)
            return 1;
        else return messages.get(messages.size() - 1).getId() + 1;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", receiver=" + receiver.getUsername() +
                ", Number of messages=" + messages.size();
    }
}
