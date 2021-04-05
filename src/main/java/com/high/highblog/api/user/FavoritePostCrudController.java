package com.high.highblog.api.user;

import com.high.highblog.bloc.FavoritePostCrudBloc;
import com.high.highblog.model.dto.request.FavoritePostCreateReq;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/user/favorite-posts")
public class FavoritePostCrudController {

    private final FavoritePostCrudBloc favoritePostCrudBloc;

    public FavoritePostCrudController(final FavoritePostCrudBloc favoritePostCrudBloc) {
        this.favoritePostCrudBloc = favoritePostCrudBloc;
    }

    @PostMapping
    public ResponseEntity<FavoritePostCreateReq> createFavoritePost(@RequestBody FavoritePostCreateReq req) {

        favoritePostCrudBloc.createFavoritePostForCurrentUser(req);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
