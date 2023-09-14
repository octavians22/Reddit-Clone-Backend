package com.example.RedditClone.mapper;

import com.example.RedditClone.dto.SubredditDto;
import com.example.RedditClone.model.Post;
import com.example.RedditClone.model.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {


    @Mapping(target ="numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> numberOfPosts){return numberOfPosts.size();}

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    @Mapping(target ="name", source = "subredditName")
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto);
}
