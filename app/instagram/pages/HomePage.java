package app.instagram.pages;

import app.instagram.objects.Comment;
import app.instagram.objects.Post;
import app.instagram.objects.User;
import app.instagram.utilities.ConsoleConstants;
import app.instagram.utilities.ConsoleHelper;
import app.instagram.utilities.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class HomePage {

    private static final int EXIT = 0;
    private static final int LIKE = 1;
    private static final int COMMENT = 2;
    private static final int USER_PAGE = 3;
    private static final int SHOW_LIKES = 4;
    private static final int SHOW_COMMENTS = 5;

    private static final int COMMENT_REPLY = 1;
    private static final int COMMENT_LIKE = 2;
    private static final int COMMENT_SHOW_REPLIES = 3;

    private final ConsoleHelper consoleHelper;
    private User user;

    private int _postOptionFrom = 0;
    private int _postOptionTo = 5;

    public HomePage(User user, ConsoleHelper consoleHelper) {
        this.consoleHelper = consoleHelper;
        this.user = user;
    }

    public void show() {
        this.consoleHelper.println(ConsoleConstants.POSTS);
        List<Post> posts = getPosts();

        showPosts(posts);

        int postId = (int) this.consoleHelper.read(ConsoleConstants.HOME_PAGE_OPTIONS,
                ConsoleHelper.INTEGER);

        if (postId > 0) {
            Post post = getPost(posts, postId);
            SharedPreferences.add(ConsoleConstants.POST, post);

            if (post == null) {
                show();
            } else {
                showPost(post);
            }
        }
    }

    private void showPosts(List<Post> posts) {
        for (Post post : posts)
            this.consoleHelper.println(post);
    }

    private void showPost(Post post) {
        this.consoleHelper.println(post);
        showPostOptions(post);
    }

    private void showPostOptions(Post post) {
        int option = this.consoleHelper.readInRange(ConsoleConstants.POST_OPTIONS,
                _postOptionFrom, _postOptionTo);

        switch (option) {
            case EXIT:
                return;
            case LIKE:
                likePost(post);
                break;
            case COMMENT:
                commentPost(post);
                break;
            case USER_PAGE:
                new UserPage(this.user, findUser(post), this.consoleHelper).show();
                break;
            case SHOW_LIKES:
                showLikes(post);
                break;
            case SHOW_COMMENTS:
                showComments(post);
                break;
        }
    }

    private void showLikes(Post post) {
        this.consoleHelper.println(ConsoleConstants.LIKES);

        for (User user : post.getLikes())
            this.consoleHelper.println(user);
    }

    private void showComments(Post post) {
        this.consoleHelper.println(ConsoleConstants.COMMENTS);

        for (Comment comment : post.getCommentList())
            this.consoleHelper.println(comment);

        showCommentsOptions(post);
    }

    private void showCommentsOptions(Post post) {
        int commentId = (int) this.consoleHelper.read(ConsoleConstants.COMMENTS_OPTIONS,
                ConsoleHelper.INTEGER);

        if (commentId > 0) {
            Comment comment = getComment(post, commentId);

            if (comment == null) {
                this.consoleHelper.println(ConsoleConstants.NOT_404_FOUND);
            } else {
                showCommentOptions(post, comment);
            }
        }
    }

    private void showCommentOptions(Post post, Comment comment) {
        int option = this.consoleHelper.readInRange(ConsoleConstants.COMMENT_OPTIONS, 0, 3);

        switch (option) {
            case EXIT:
                break;
            case COMMENT_REPLY:
                replyComment(post, comment);
                break;
            case COMMENT_LIKE:
                likeComment(post, comment);
                break;
            case COMMENT_SHOW_REPLIES:
                showReplies(post, comment);
                break;
        }
    }

    private void showReplies(Post post, Comment comment) {
        this.consoleHelper.println(ConsoleConstants.REPLIES);

        for (Comment c : comment.getCommentList())
            this.consoleHelper.println(c);

        showCommentOptions(post, comment);
    }

    private void replyComment(Post post, Comment comment) {
        String reply = (String) this.consoleHelper.read(ConsoleConstants.REPLY_COMMENT,
                ConsoleHelper.STRING);

        if (!this.consoleHelper.isExit(reply)) {
            if (reply.isEmpty()) {
                this.consoleHelper.println(ConsoleConstants.EMPTY_INPUT);
                replyComment(post, comment);
                return;
            }

            Comment replyComment = new Comment(post.generateCommentId(), this.user, reply);
            comment.reply(replyComment);
        }
        showCommentOptions(post, comment);
    }

    private void likeComment(Post post, Comment comment) {
        comment.like(this.user);
        showCommentOptions(post, comment);
    }

    private User findUser(Post post) {
        User user = getPostUserId(post);

        if (user == null) {
            this.consoleHelper.println(ConsoleConstants.NOT_404_FOUND);
        } else
            return user;
        return null;
    }

    private void likePost(Post post) {
        post.like(this.user);
        showPost(post);
    }

    private void commentPost(Post post) {
        String body = (String) this.consoleHelper.read(ConsoleConstants.COMMENT_POST,
                ConsoleHelper.STRING);

        if (!this.consoleHelper.isExit(body)) {
            Comment comment = new Comment(post.generateCommentId(), this.user, body);
            post.comment(comment);
        }
    }

    private Post getPost(List<Post> posts, int postId) {
        for (Post post : posts)
            if (post.getId() == postId)
                return post;
        return null;
    }

    private Comment getComment(Post post, int commentId) {
        for (Comment comment : post.getCommentList())
            if (comment.getId() == commentId)
                return comment;
        return null;
    }

    private List<Post> getPosts() {
        List<Post> posts = new ArrayList<>();
        for (User user : this.user.getFollowings())
            if (!user.hasBlocked(this.user))
                posts.addAll(user.getPosts());
        return posts;
    }

    private User getPostUserId(Post post) {
        for (User user : this.user.getFollowings())
            for (Post p : user.getPosts())
                if (p.getId() == post.getId() && p.getBody().equals(post.getBody()))
                    return user;
        return null;
    }
}
