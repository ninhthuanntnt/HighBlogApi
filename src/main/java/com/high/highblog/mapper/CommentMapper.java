package com.high.highblog.mapper;

import com.high.highblog.model.dto.request.CommentCreateReq;
import com.high.highblog.model.dto.response.CommentRes;
import com.high.highblog.model.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mappings({
            @Mapping(target = "childCommentsRes", source = "childComments"),
            @Mapping(target = "userRes", source = "user")
    })
    CommentRes toCommentRes(Comment comment);

    Comment toComment(CommentCreateReq commentCreateReq);

    List<CommentRes> toListCommentsRes(List<Comment> comments);
}
