package app.instagram.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Comment implements Serializable {

    private int id;
    private User user;
    private String body;
    private List<Comment> commentList;
    private List<User> likes;

    public Comment(int id, User user, String body, List<Comment> commentList,
                   List<User> likes) {
        this.id = id;
        this.user = user;
        this.body = body;
        this.commentList = commentList;
        this.likes = likes;
    }

    public Comment(int id, User user, String body) {
        this.id = id;
        this.user = user;
        this.body = body;
        this.commentList = new ArrayList<>();
        this.likes = new ArrayList<>();
    }

    public void like(User user) {
        if(this.likes.contains(user))
            this.likes.remove(user);
        else this.likes.add(user);
    }

    public void reply(Comment comment) {
        this.commentList.add(comment);
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public List<User> getLikes() {
        return likes;
    }

    public void setLikes(List<User> likes) {
        this.likes = likes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", user=" + user.getUsername() +
                ", body='" + body + '\'' +
                ", Number of replies=" + commentList.size() +
                ", Number of likes=" + likes.size();
    }
}
