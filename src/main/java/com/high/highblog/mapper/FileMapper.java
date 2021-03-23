package com.high.highblog.mapper;

import com.high.highblog.model.dto.response.ImageUploadRes;
import com.high.highblog.model.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FileMapper {

    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    ImageUploadRes toFileRes(File file);
}
