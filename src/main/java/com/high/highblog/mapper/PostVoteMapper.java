package com.high.highblog.mapper;

import com.high.highblog.model.dto.request.PostVoteCreateReq;
import com.high.highblog.model.dto.request.PostVoteUpdateReq;
import com.high.highblog.model.entity.PostVote;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface PostVoteMapper {

    PostVoteMapper INSTANCE = Mappers.getMapper(PostVoteMapper.class);

    PostVote toPostVote(PostVoteCreateReq postVoteCreateReq);

    PostVote toPostVote(PostVoteUpdateReq postVoteUpdateReq, @MappingTarget PostVote postVote);
}
