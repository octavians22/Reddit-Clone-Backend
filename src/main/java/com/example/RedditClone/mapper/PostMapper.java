package com.example.RedditClone.mapper;

import com.example.RedditClone.dto.PostRequest;
import com.example.RedditClone.dto.PostResponse;
import com.example.RedditClone.model.*;
import com.example.RedditClone.repository.CommentRepository;
import com.example.RedditClone.repository.VoteRepository;
import com.example.RedditClone.service.AuthService;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
     private VoteRepository voteRepository;
    @Autowired
     private AuthService authService;

    @Mapping(target = "createDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "user", source = "user")
    public abstract Post mapToEntity(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "postName", source = "postName")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    public abstract PostResponse mapToDto(Post post);
    public Integer commentCount(Post post){
        return commentRepository.findByPost(post).size();
    }

    public String getDuration(Post post){
        //return Duration.between((post.getCreateDate()), Instant.now()).toString(); // nu afiseaza bine durata
        return TimeAgo.using(post.getCreateDate().toEpochMilli());
    }
}


