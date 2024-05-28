package app.instagram.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {

    private int id;
    private User sender;
    private User receiver;
    private String body;
    private List<Message> replies;

    public Message(int id, User sender, User receiver, String body, List<Message> replies) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.body = body;
        this.replies = replies;
    }

    public Message(int id, User sender, User receiver, String body) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.body = body;
        this.replies = new ArrayList<>();
    }

    public void reply(Message message) {
        this.replies.add(message);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Message> getReplies() {
        return replies;
    }

    public void setReplies(List<Message> replies) {
        this.replies = replies;
    }

    public String getReplyMessages() {
        StringBuilder replies = new StringBuilder();
        for (Message message : this.replies) {
            replies.append(getString(message));
            replies.append("\n");
        }
        return replies.toString();
    }

    public String getString(Message message) {
        return "sender=" + message.getSender().getUsername() +
                ", body='" + message.getBody() + '\'';
    }

    @Override
    public String toString() {
        String result =  "id=" + id +
                ", sender=" + sender.getUsername() +
                ", body='" + body + '\'' +
                ", Number of replies=" + replies.size();

        if (replies.size() > 0) {
            result += ", \nreplies {\n" +
                    getReplyMessages() +
                    "}";
        }
        return result;
    }
}
