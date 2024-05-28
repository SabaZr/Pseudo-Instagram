package app.instagram.pages;

import app.instagram.objects.Comment;
import app.instagram.objects.Post;
import app.instagram.objects.User;
import app.instagram.utilities.ConsoleConstants;
import app.instagram.utilities.ConsoleHelper;

public class UserPage {

    public static final int EXIT = 0;
    public static final int VIEW_PROFILE = 1;
    public static final int POSTS = 2;
    public static final int FOLLOW_UNFOLLOW = 3;
    private static final int BLOCK_UNBLOCK = 4;

    private static final int LIKE = 1;
    private static final int COMMENT = 2;
    private static final int USER_PAGE = 3;

    private static final int YES = 1;
    private static final int NO = 2;

    private User loggedUser;
    private User user;
    private ConsoleHelper consoleHelper;

    private int _from = 0;
    private int _to = 4;

    private int _postOptionFrom = 0;
    private int _postOptionTo = 2;

    public UserPage(User loggedUser, User user, ConsoleHelper consoleHelper) {
        this.loggedUser = loggedUser;
        this.user = user;
        this.consoleHelper = consoleHelper;
    }

    public void show() {
        boolean exited = false;
        while (!exited) {
            int option = this.consoleHelper.readInRange(ConsoleConstants.USER_PAGE, _from, _to);

            switch (option) {
                case EXIT:
                    exited = true;
                    break;
                case VIEW_PROFILE:
                    showProfile();
                    break;
                case POSTS:
                    showPosts();
                    break;
                case FOLLOW_UNFOLLOW:
                    followUnfollow();
                    break;
                case BLOCK_UNBLOCK:
                    blockUnblock();
                    break;
            }
        }
    }

    private void showProfile() {
        if (this.user.hasBlocked(this.loggedUser))
            this.consoleHelper.println(ConsoleConstants.BLOCKED);
        else
            this.consoleHelper.println(this.user.getProfile());
    }

    private void showPosts() {
        if (this.user.hasBlocked(this.loggedUser))
            this.consoleHelper.println(ConsoleConstants.BLOCKED);
        else {
            this.consoleHelper.println(ConsoleConstants.POSTS);

            for (Post post : this.user.getPosts())
                this.consoleHelper.println(post);

            showPostsOptions();
        }
    }

    private void showPostsOptions() {
        int postId = (int) this.consoleHelper.read(ConsoleConstants.USER_POSTS_OPTIONS,
                ConsoleHelper.INTEGER);

        if (postId > 0) {
            Post post = getPost(postId);

            if (post == null) {
                this.consoleHelper.println(ConsoleConstants.NOT_404_FOUND);
            } else {
                showPostOptions(post);
            }
        }
    }

    private void showPostOptions(Post post) {
        int option = this.consoleHelper.readInRange(ConsoleConstants.USER_POST_OPTIONS,
                _postOptionFrom, _postOptionTo);

        switch (option) {
            case EXIT:
                break;
            case LIKE:
                likePost(post);
                break;
            case COMMENT:
                commentPost(post);
                break;
        }
        showPosts();
    }

    private void likePost(Post post) {
        post.like(this.loggedUser);
        showPosts();
    }

    private void commentPost(Post post) {
        String body = (String) this.consoleHelper.read(ConsoleConstants.COMMENT_POST,
                ConsoleHelper.STRING);

        if (!this.consoleHelper.isExit(body)) {
            Comment comment = new Comment(post.generateCommentId(), this.loggedUser, body);
            post.comment(comment);
        }
    }

    private void followUnfollow() {
        boolean toFollow = false;
        if (this.loggedUser.hasFollowed(this.user)) {
            this.consoleHelper.println(ConsoleConstants.FOLLOW_STATE);
        } else {
            this.consoleHelper.println(ConsoleConstants.UNFOLLOW_STATE);
            toFollow = true;
        }

        int option = this.consoleHelper.readInRange(ConsoleConstants.ARE_YOU_SURE, 1, 2);

        switch (option) {
            case YES:
                doProcessFollowState(toFollow);
                break;
            case NO:
                break;
        }
    }

    private void blockUnblock() {
        boolean toBlock = false;
        if (this.loggedUser.hasBlocked(this.user)) {
            this.consoleHelper.println(ConsoleConstants.BLOCK_STATE);
        } else {
            this.consoleHelper.println(ConsoleConstants.UNBLOCK_STATE);
            toBlock = true;
        }

        int option = this.consoleHelper.readInRange(ConsoleConstants.ARE_YOU_SURE, 1, 2);

        switch (option) {
            case YES:
                doProcessBlockState(toBlock);
                break;
            case NO:
                break;
        }
    }

    private void doProcessFollowState(boolean state) {
        if (state) {
            this.loggedUser.follow(this.user);
        } else {
            this.loggedUser.unfollow(this.user);
        }
    }

    private void doProcessBlockState(boolean state) {
        if (state) {
            this.loggedUser.block(this.user);
        } else {
            this.loggedUser.unblock(this.user);
        }
    }

    private Post getPost(int postId) {
        for (Post post : this.user.getPosts())
            if (post.getId() == postId)
                return post;
        return null;
    }
}
