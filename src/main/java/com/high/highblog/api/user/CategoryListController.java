package com.high.highblog.api.user;

import com.high.highblog.bloc.CategoryListBloc;
import com.high.highblog.mapper.CategoryMapper;
import com.high.highblog.model.dto.response.CategoryRes;
import com.high.highblog.model.entity.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/categories")
public class CategoryListController {

    private final CategoryListBloc categoryListBloc;

    public CategoryListController(final CategoryListBloc categoryListBloc) {
        this.categoryListBloc = categoryListBloc;
    }

    @GetMapping
    public ResponseEntity<List<CategoryRes>> fetchCategories() {
        List<Category> categories = categoryListBloc.fetchAllCategories();
        return ResponseEntity.ok(CategoryMapper.INSTANCE.toListCategoryRes(categories));
    }
}
