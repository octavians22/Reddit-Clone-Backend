package com.example.RedditClone.exceptions;

public class SpringRedditException extends RuntimeException {

    public SpringRedditException(String message, Exception e) {
        super(message, e);
    }
    public SpringRedditException(String message) {
        super(message);
    }
}
