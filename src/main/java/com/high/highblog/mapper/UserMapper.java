package com.high.highblog.mapper;

import com.high.highblog.model.dto.request.UserUpdateReq;
import com.high.highblog.model.dto.response.AdminUserRes;
import com.high.highblog.model.dto.response.UserRes;
import com.high.highblog.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserRes toUserRes(User user);

    AdminUserRes toAdminUserRes(User user);

    User toUser(UserUpdateReq userUpdateReq,@MappingTarget User dbUser);

}
