package com.example.RedditClone.controller;

import com.example.RedditClone.dto.CommentsDto;
import com.example.RedditClone.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentsController {

    private final CommentService commentService;
    @PostMapping()
    public ResponseEntity<Void> createComments (@RequestBody CommentsDto commentsDto){
        commentService.save(commentsDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId){
        List<CommentsDto> allComments = commentService.getAllCommentsByPostId(postId);
        return new ResponseEntity<>(allComments, HttpStatus.OK);
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@PathVariable String username){
        List<CommentsDto> allComments = commentService.getAllCommentsByUsername(username);
        return new ResponseEntity<>(allComments, HttpStatus.OK);
    }
}



