package com.high.highblog.model.mapper;

import com.high.highblog.model.dto.response.FileRes;
import com.high.highblog.model.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FileMapper {

    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    FileRes toFileRes(File file);
}
