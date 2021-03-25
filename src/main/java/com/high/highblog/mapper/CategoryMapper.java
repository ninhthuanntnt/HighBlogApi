package com.high.highblog.mapper;

import com.high.highblog.model.dto.response.CategoryRes;
import com.high.highblog.model.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    List<CategoryRes> toListCategoryRes(List<Category> categories);

}
