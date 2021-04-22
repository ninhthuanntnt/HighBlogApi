package com.high.highblog.bloc;

import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.entity.Post;
import com.high.highblog.model.entity.User;
import com.high.highblog.service.FavoritePostService;
import com.high.highblog.service.PostService;
import com.high.highblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FavoritePostListBloc {

    private final PostService postService;
    private final UserService userService;

    public FavoritePostListBloc(final PostService postService, final UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @Transactional
    public Page<Post> fetchFavoritePostForCurrentUser(final BasePaginationReq basePaginationReq){
        log.info("Fetch favorite post for current user");

        PageRequest pageRequest = PaginationHelper.generatePageRequest(basePaginationReq);

        Page<Post> posts = postService.fetchFavoritePostByUserIdWithPageRequest(SecurityHelper.getUserId(),
                                                                                pageRequest);
        includeUserToPosts(posts);

        return posts;
    }


    private void includeUserToPosts(Page<Post> posts) {
        Map<Long, User> userIdUserMap = userService.fetchByIdIn(posts.map(Post::getUserId).toSet())
                                                   .stream()
                                                   .collect(Collectors.toMap(User::getId, user -> user));

        posts.forEach(post -> post.setUser(userIdUserMap.get(post.getUserId())));
    }
}
