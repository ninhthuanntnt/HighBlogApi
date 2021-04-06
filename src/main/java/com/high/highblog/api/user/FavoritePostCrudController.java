package com.high.highblog.api.user;

import com.high.highblog.bloc.FavoritePostCrudBloc;
import com.high.highblog.model.dto.request.FavoritePostCreateReq;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/user/favorite-posts")
public class FavoritePostCrudController {

    private final FavoritePostCrudBloc favoritePostCrudBloc;

    public FavoritePostCrudController(final FavoritePostCrudBloc favoritePostCrudBloc) {
        this.favoritePostCrudBloc = favoritePostCrudBloc;
    }

    @PostMapping
    public ResponseEntity<?> createFavoritePost(@RequestBody FavoritePostCreateReq req) {

        favoritePostCrudBloc.createFavoritePostForCurrentUser(req);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFavoritePost(@RequestParam("postId") final Long postId) {

        favoritePostCrudBloc.deleteFavoritePostForCurrentUser(postId);

        return ResponseEntity.noContent().build();
    }
}
