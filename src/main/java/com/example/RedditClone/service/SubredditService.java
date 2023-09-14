package com.example.RedditClone.service;

import com.example.RedditClone.dto.SubredditDto;
import com.example.RedditClone.exceptions.SpringRedditException;
import com.example.RedditClone.mapper.SubredditMapper;
import com.example.RedditClone.model.Subreddit;
import com.example.RedditClone.repository.SubredditRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;
    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
       Subreddit subreddit = subredditMapper.mapDtoToSubreddit(subredditDto);
        Subreddit save = subredditRepository.save(subreddit);
        subredditDto.setId(save.getId());
        return subredditDto;
    }

    @Transactional
    public List<SubredditDto> getAll() {
       return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(Collectors.toList());
    }

    public SubredditDto getSubredditById(Long id) {
        return subredditRepository.findById(id)
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .findAny().orElseThrow(() -> new SpringRedditException("No Subreddit with such id"));
//        Subreddit subreddit = subredditRepository.findById(id)
//                .orElseThrow(() -> new SpringRedditException("No Subreddit with such id"));
//
//        return subredditMapper.mapSubredditToDto(subreddit);

    }

//    private SubredditDto mapToDto(Subreddit subreddit) {
//        return SubredditDto.builder().subredditName(subreddit.getName())
//                .id(subreddit.getId())
//                .description(subreddit.getDescription())
//                .numberOfPosts(subreddit.getPosts().size())
//                .build();
//    }
//
//    private Subreddit mapSubredditDto(SubredditDto subredditDto) {
//       return Subreddit.builder().name(subredditDto.getSubredditName())
//                .description(subredditDto.getDescription())
//                .build();
//    }


}
