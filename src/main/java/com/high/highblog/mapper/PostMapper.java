package com.high.highblog.mapper;

import com.high.highblog.model.dto.request.PostCreateReq;
import com.high.highblog.model.dto.request.PostUpdateReq;
import com.high.highblog.model.dto.response.PostDetailRes;
import com.high.highblog.model.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {PostTagMapper.class, TagMapper.class})
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);


    @Mapping(target = "postTags", source = "tagCreateReqs")
    Post toPost(PostCreateReq postCreateReq);

    @Mapping(target = "postTags", source = "tagCreateReqs")
    Post toPost(PostUpdateReq postUpdateReq, @MappingTarget Post post);

    @Mapping(target = "tagsRes", source = "postTags")
    PostDetailRes toPostDetailRes(Post post);

}
