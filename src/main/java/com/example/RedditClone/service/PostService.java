package com.example.RedditClone.service;

import com.example.RedditClone.dto.PostRequest;
import com.example.RedditClone.dto.PostResponse;
import com.example.RedditClone.exceptions.PostNotFoundException;
import com.example.RedditClone.exceptions.SpringRedditException;
import com.example.RedditClone.exceptions.SubredditNotFoundException;
import com.example.RedditClone.mapper.PostMapper;
import com.example.RedditClone.model.Post;
import com.example.RedditClone.model.Subreddit;
import com.example.RedditClone.model.User;
import com.example.RedditClone.repository.PostRepository;
import com.example.RedditClone.repository.SubredditRepository;
import com.example.RedditClone.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final UserRepository userRepository;
    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                        .orElseThrow(() -> new SubredditNotFoundException("Subreddit" + postRequest.getSubredditName() +"not found"));
        User currentUser = authService.getCurrentUser();
        postRepository.save(postMapper.mapToEntity(postRequest,subreddit,currentUser));
    }

    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("No such post with id " + id));
        return postMapper.mapToDto(post);
    }

    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subredditPost = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException("No subreddit with id"+ subredditId));
        List<Post> posts = postRepository.findAllBySubreddit(subredditPost);
        return posts.stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByUsername(String username) {

        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username" + username + "not found"));
        List<Post> posts = postRepository.findByUser(user);
        return posts.stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
