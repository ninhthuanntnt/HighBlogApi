package com.high.highblog.mapper;

import com.high.highblog.model.dto.request.UserUpdateReq;
import com.high.highblog.model.dto.response.PostRes;
import com.high.highblog.model.dto.response.UserRes;
import com.high.highblog.model.dto.response.UserTransactionRes;
import com.high.highblog.model.entity.Post;
import com.high.highblog.model.entity.User;
import com.high.highblog.model.entity.UserTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface UserTransactionMapper {
    UserTransactionMapper INSTANCE = Mappers.getMapper(UserTransactionMapper.class);

    UserTransactionRes toUserTransactionRes(UserTransaction userTransaction);
}
