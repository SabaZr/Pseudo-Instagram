package app.instagram.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    public static final int MEMBER = 1;
//    public static final int ADMIN = 2;

    public static final int REGISTERED = 1;
    public static final int NOT_REGISTERED = 2;

    private int id;
    private int type;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String bio;
    private String email;
    private List<Post> posts;
    private List<User> followers;
    private List<User> followings;
    private List<Chat> chats;
    private List<User> blackList;

    public User(int id, int type, String username, String password,
                String firstName, String lastName, String bio, String email, List<Post> posts,
                List<User> followers, List<User> followings, List<Chat> chats, List<User> blackList) {
        this.id = id;
        this.type = type;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.email = email;
        this.posts = posts;
        this.followers = followers;
        this.followings = followings;
        this.chats = chats;
        this.blackList = blackList;
    }

    public User(int id, int type, String username, String password) {
        this.id = id;
        this.type = type;
        this.username = username;
        this.password = password;
        this.firstName = "";
        this.lastName = "";
        this.bio = "";
        this.email = "";
        this.posts = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.followings = new ArrayList<>();
        this.chats = new ArrayList<>();
        this.blackList = new ArrayList<>();
    }

    public Chat newChat(User user) {
        Chat chat = new Chat(generateChatId(), user);
        this.chats.add(chat);
        return chat;
    }

    public void joinChat(Chat chat) {
        this.chats.add(chat);
    }

    public void post(Post post) {
        this.posts.add(post);
    }

    public boolean hasFollowed(User user) {
        return this.followings.contains(user);
    }

    public boolean hasBlocked(User user) {
        return this.blackList.contains(user);
    }

    public void removePost(Post post) {
        this.posts.remove(post);
    }

    public void unfollow(User user) {
        this.followings.remove(user);
        user.removeFollower(this);
    }

    public void follow(User user) {
        this.followings.add(user);
        user.addFollower(this);
    }

    public void block(User user) {
        this.blackList.add(user);
    }

    public void unblock(User user) {
        this.blackList.remove(user);
    }

    public void addFollower(User user) {
        this.followers.add(user);
    }

    public void removeFollower(User user) {
        this.followers.remove(user);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public List<User> getFollowings() {
        return followings;
    }

    public void setFollowings(List<User> followings) {
        this.followings = followings;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<User> getBlackList() {
        return blackList;
    }

    public void setBlackList(List<User> blackList) {
        this.blackList = blackList;
    }

    public int generatePostId() {
        String id = "";
        if (posts.size() == 0)
            id = "1";
        else id = String.valueOf(posts.size() + 1);

        return Integer.parseInt(this.id + id);
    }

    public int generateChatId() {
        if (chats.size() == 0)
            return 1;
        else return chats.get(chats.size() - 1).getId() + 1;
    }

    public String getProfile() {
        return "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", bio='" + bio + '\'' +
                ", email='" + email + '\'';
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", username='" + username + '\'';
    }
}
