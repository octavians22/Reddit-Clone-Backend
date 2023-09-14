package com.example.RedditClone.service;

import com.example.RedditClone.dto.CommentsDto;
import com.example.RedditClone.exceptions.PostNotFoundException;
import com.example.RedditClone.mapper.CommentMapper;
import com.example.RedditClone.model.Comment;
import com.example.RedditClone.model.Post;
import com.example.RedditClone.model.User;
import com.example.RedditClone.repository.CommentRepository;
import com.example.RedditClone.repository.PostRepository;
import com.example.RedditClone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    private final MailContentBuilder mailContentBuilder;

    private final MailService mailService;
    public void save(CommentsDto commentsDto){
       Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(()-> new PostNotFoundException("No post with such id"));
       Comment comment = commentMapper.mapToEntity(commentsDto, post, authService.getCurrentUser());
       commentRepository.save(comment);

       String message = mailContentBuilder.build(comment.getUser().getName() + " posted a comment on a post made by " + post.getUser().getName());
       sendCommentNotification(message, comment.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMailNoTrapMail(user.getEmail(),
                user.getName() + " commented on your post." ,
                message);
    }

    public List<CommentsDto>  getAllCommentsByPostId(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("No post with such postId"));

        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentsDto> getAllCommentsByUsername(String username) {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user with such userId"));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
