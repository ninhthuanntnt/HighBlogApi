package com.high.highblog.mapper;

import com.high.highblog.model.dto.request.PostCreateReq;
import com.high.highblog.model.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    Post toPost(PostCreateReq postCreateReq);
}
