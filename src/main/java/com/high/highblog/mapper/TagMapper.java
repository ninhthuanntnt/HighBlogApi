package com.high.highblog.mapper;


import com.high.highblog.model.dto.response.TagRes;
import com.high.highblog.model.entity.PostTag;
import com.high.highblog.model.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TagMapper {
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    TagRes toTagRes(Tag tag);

    List<TagRes> toListTagRes(List<Tag> tags);

    @Mapping(target = "id", source = "tagId")
    TagRes toTagRes(PostTag postTag);
}
