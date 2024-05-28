package app.instagram.pages;

import app.instagram.database.Database;
import app.instagram.objects.Post;
import app.instagram.objects.User;
import app.instagram.utilities.ConsoleConstants;
import app.instagram.utilities.ConsoleHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

public class ProfilePage {

    private static final int EXIT = 0;
    private static final int POSTS = 1;
    private static final int FOLLOWERS = 2;
    private static final int FOLLOWINGS = 3;
    private static final int EDIT_PROFILE = 4;

    private static final int CREATE_POST = -1;

    private static final int EDIT = 1;
    private static final int DELETE = 2;

    private static final int YES = 1;
    private static final int NO = 2;

    private static final int VIEW_PAGE = 1;
    private static final int UNFOLLOW = 2;
    private static final int SEND_MESSAGE = 3;

    private final ConsoleHelper consoleHelper;
    private User user;

    private int _from = 0;
    private int _to = 4;

    private int _postOptionFrom = 0;
    private int _postOptionTo = 2;

    private int _deleteFrom = 1;
    private int _deleteTo = 2;

    public ProfilePage(User user, ConsoleHelper consoleHelper) {
        this.consoleHelper = consoleHelper;
        this.user = user;
    }

    public void show() {
        boolean exited = false;
        while (!exited) {
            int option = this.consoleHelper.readInRange(ConsoleConstants.PROFILE, _from, _to);

            switch (option) {
                case EXIT:
                    exited = true;
                    break;
                case POSTS:
                    showPosts();
                    break;
                case FOLLOWERS:
                    showFollowers();
                    break;
                case FOLLOWINGS:
                    showFollowings();
                    break;
                case EDIT_PROFILE:
                    new EditProfilePage(this.user, this.consoleHelper).show();
                    break;
            }
        }
    }

    private void showPosts() {
        this.consoleHelper.println(ConsoleConstants.POSTS);

        for (Post post : this.user.getPosts())
            this.consoleHelper.println(post);
        showPostsOptions();
    }

    private void showPostsOptions() {
        int option = (int) this.consoleHelper.read(ConsoleConstants.POSTS_OPTIONS,
                ConsoleHelper.INTEGER);
        if (option <= 0) {
            switch (option) {
                case EXIT:
                    break;
                case CREATE_POST:
                    createPost();
                    break;
            }
        } else {
            Post post = getPost(option);

            if (post == null) {
                this.consoleHelper.println(ConsoleConstants.NOT_404_FOUND);
                showPosts();
            } else {
                showSelfPostOption(post);
            }
        }
    }

    private void createPost() {
        String postBody = (String) this.consoleHelper.read(ConsoleConstants.NEW_POST,
                ConsoleHelper.STRING);
        String media = (String) this.consoleHelper.read(ConsoleConstants.POST_MEDIA,
                ConsoleHelper.STRING);

        if (postBody.isEmpty()) {
            this.consoleHelper.println(ConsoleConstants.EMPTY_INPUT);
            createPost();
            return;
        }
        Post post;
        if (media.isEmpty())
            post = new Post(this.user.generatePostId(), postBody);
        else {
            if (!checkMediaExist(media)) {
                this.consoleHelper.println(String.format(ConsoleConstants.MEDIA_404, media));
                createPost();
                return;
            }
            LocalDateTime time = LocalDateTime.now();
            if (!copyMedia(time, media)) {
                this.consoleHelper.println(ConsoleConstants.SENDING_FAILED);
                createPost();
                return;
            }
            String fileName = String.format("%s-%s", time.toString().replace(":", "")
                    .replace("-", ""), media);
            post = new Post(this.user.generatePostId(), postBody, fileName);
        }
        this.user.post(post);

        this.consoleHelper.println(ConsoleConstants.POST_CREATED);
    }

    private void showSelfPostOption(Post post) {
        this.consoleHelper.println(post);

        int option = this.consoleHelper.readInRange(ConsoleConstants.SELF_POST_OPTIONS,
                _postOptionFrom, _postOptionTo);

        switch (option) {
            case EXIT:
                break;
            case EDIT:
                editPost(post);
                break;
            case DELETE:
                deletePost(post);
                break;
        }
    }

    private void editPost(Post post) {
        String input = (String) this.consoleHelper.read(ConsoleConstants.EDIT_POST,
                ConsoleHelper.STRING);

        if (!this.consoleHelper.isExit(input)) {
            post.setBody(input);
        }
        showPosts();
    }

    private void deletePost(Post post) {
        int option = this.consoleHelper.readInRange(ConsoleConstants.DELETE_POST, _deleteFrom,
                _deleteFrom);

        switch (option) {
            case YES:
                this.user.removePost(post);
                break;
            case NO:
                break;
        }
        showPosts();
    }

    private void showFollowers() {
        this.consoleHelper.println(ConsoleConstants.FOLLOWERS);

        for (User user : this.user.getFollowers())
            this.consoleHelper.println(user);

        showFollowersOptions();
    }

    private void showFollowersOptions() {
        int userId = (int) this.consoleHelper.read(ConsoleConstants.FOLLOWERS_OPTIONS,
                ConsoleHelper.INTEGER);
        if (userId > 0) {
            User user = getUser(userId);

            if (user == null) {
                this.consoleHelper.println(ConsoleConstants.NOT_404_FOUND);
                showFollowers();
            } else {
                new UserPage(this.user, user, this.consoleHelper);
            }
        }
    }

    private void showFollowings() {
        this.consoleHelper.println(ConsoleConstants.FOLLOWINGS);

        for (User user : this.user.getFollowings())
            this.consoleHelper.println(user);

        showFollowingsOptions();
    }

    private void showFollowingsOptions() {
        int userId = (int) this.consoleHelper.read(ConsoleConstants.FOLLOWINGS_OPTIONS,
                ConsoleHelper.INTEGER);
        if (userId > 0) {
            User user = getUser(userId);

            if (user == null) {
                this.consoleHelper.println(ConsoleConstants.NOT_404_FOUND);
                showFollowers();
            } else {
                showFollowingMoreOptions(user);
            }
        }
    }

    private void showFollowingMoreOptions(User user) {
        int option = this.consoleHelper.readInRange(ConsoleConstants.FOLLOWING_OPTIONS, 0, 3);

        switch (option) {
            case EXIT:
                break;
            case VIEW_PAGE:
                new UserPage(this.user, user, this.consoleHelper);
                break;
            case UNFOLLOW:
                unfollowUser(user);
                break;
            case SEND_MESSAGE:
                break;
        }
    }

    private void unfollowUser(User user) {
        this.user.unfollow(user);
    }

    private Post getPost(int postId) {
        for (Post post : this.user.getPosts())
            if (post.getId() == postId)
                return post;
        return null;
    }

    private User getUser(int userId) {
        for (User user : Database.Users)
            if (user.getId() == userId)
                return user;
        return null;
    }

    private boolean checkMediaExist(String media) {
        String src = String.format("mediaToUpload/%s", media.trim());
        File file = new File(src);
        System.out.println(file.toPath());

        return file.isFile();
    }

    public boolean copyMedia(LocalDateTime time, String media) {
        String src = String.format("mediaToUpload/%s", media);

        String destination = String.format("media/%s-%s", time.toString().replace(":", "")
                .replace("-", ""), media);

        try {
            Files.copy(new File(src).toPath(), new File(destination).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
