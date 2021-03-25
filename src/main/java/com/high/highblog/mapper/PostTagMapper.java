package com.high.highblog.mapper;

import com.high.highblog.model.dto.request.TagCreateReq;
import com.high.highblog.model.entity.PostTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface PostTagMapper {
    PostTagMapper INSTANCE = Mappers.getMapper(PostTagMapper.class);

    @Mappings({
            @Mapping(target = "tagId", source = "id"),
            @Mapping(target = "id", ignore = true)
    })
    PostTag toPostTag(TagCreateReq tagCreateReq);
}
