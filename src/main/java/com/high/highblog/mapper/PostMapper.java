package com.high.highblog.mapper;

import com.high.highblog.helper.DateTimeHelper;
import com.high.highblog.model.dto.request.PostCreateReq;
import com.high.highblog.model.dto.request.PostUpdateReq;
import com.high.highblog.model.dto.response.PostDetailRes;
import com.high.highblog.model.dto.response.PostDetailToUpdateRes;
import com.high.highblog.model.dto.response.PostRes;
import com.high.highblog.model.dto.response.admin.AdminPostRes;
import com.high.highblog.model.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {PostTagMapper.class,
                                                               TagMapper.class})
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);


    @Mapping(target = "postTags", source = "tagCreateReqs")
    Post toPost(PostCreateReq postCreateReq);

    @Mapping(target = "postTags", source = "tagCreateReqs")
    Post toPost(PostUpdateReq postUpdateReq, @MappingTarget Post post);

    @Mapping(target = "tagsRes", source = "postTags")
    PostDetailToUpdateRes toPostDetailToUpdateRes(Post post);

    @Mapping(target = "numberOfVotes", source = "postStatistic.numberOfVotes")
    @Mapping(target = "numberOfComments", source = "postStatistic.numberOfComments")
    @Mapping(target = "numberOfFavorites", source = "postStatistic.numberOfFavorites")
    @Mapping(target = "tagsRes", source = "postTags")
    @Mapping(target = "postVoteRes", source = "postVote")
    @Mapping(target = "userRes", source = "user")
    PostDetailRes toPostDetailRes(Post post);

    @Mapping(target = "numberOfVotes", source = "postStatistic.numberOfVotes")
    @Mapping(target = "numberOfComments", source = "postStatistic.numberOfComments")
    @Mapping(target = "numberOfFavorites", source = "postStatistic.numberOfFavorites")
    @Mapping(target = "tagsRes", source = "postTags")
    @Mapping(target = "userRes", source = "user")
    PostRes toPostRes(Post posts);

    @Mapping(target = "numberOfVotes", source = "postStatistic.numberOfVotes")
    @Mapping(target = "numberOfComments", source = "postStatistic.numberOfComments")
    @Mapping(target = "numberOfFavorites", source = "postStatistic.numberOfFavorites")
    @Mapping(target = "tagsRes", source = "postTags")
    @Mapping(target = "userRes", source = "user")
    @Mapping(target = "deleted", source = "deleted")
    AdminPostRes toAdminPostRes(Post post);

    default Long toLongFromInstant(Instant instant) {
        return DateTimeHelper.toMilli(instant);
    }
}
