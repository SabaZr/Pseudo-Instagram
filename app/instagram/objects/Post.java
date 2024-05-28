package app.instagram.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Post implements Serializable {

    private int id;
    private String body;
    private String media;
    private List<User> likes;
    private List<Comment> commentList;

    public Post(int id, String body, String media, List<User> likes, List<Comment> commentList) {
        this.id = id;
        this.body = body;
        this.media = media;
        this.likes = likes;
        this.commentList = commentList;
    }

    public Post(int id, String body) {
        this.id = id;
        this.body = body;
        this.media = "No media";
        this.likes = new ArrayList<>();
        this.commentList = new ArrayList<>();
    }

    public Post(int id, String body, String media) {
        this.id = id;
        this.body = body;
        this.media = media;
        this.likes = new ArrayList<>();
        this.commentList = new ArrayList<>();
    }

    public void like(User user) {
        if (likes.contains(user))
            likes.remove(user);
        else likes.add(user);
    }

    public void comment(Comment comment) {
        commentList.add(comment);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<User> getLikes() {
        return likes;
    }

    public void setLikes(List<User> likes) {
        this.likes = likes;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public int generateCommentId() {
        if (commentList.size() == 0)
            return 1;
        else return commentList.get(commentList.size() - 1).getId() + 1;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", body='" + body + '\'' +
                ", Number of likes=" + likes.size() +
                ", Number of comments=" + commentList.size() +
                ", media name='" + this.media + '\'';
    }
}
