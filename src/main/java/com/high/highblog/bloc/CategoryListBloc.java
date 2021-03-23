package com.high.highblog.bloc;

import com.high.highblog.model.entity.Category;
import com.high.highblog.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
public class CategoryListBloc {
    private final CategoryService categoryService;

    public CategoryListBloc(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Transactional
    public List<Category> fetchAllCategories() {
        log.info("Fetch all categories");

        return categoryService.fetchAllCategories();
    }
}
