package com.high.highblog.bloc;

import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.dto.request.PostSearchReq;
import com.high.highblog.model.entity.Post;
import com.high.highblog.model.entity.PostTag;
import com.high.highblog.model.entity.User;
import com.high.highblog.service.PostService;
import com.high.highblog.service.PostTagService;
import com.high.highblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PostListBloc {

    private final PostService postService;
    private final PostTagService postTagService;
    private final UserService userService;

    public PostListBloc(final PostService postService,
                        final PostTagService postTagService,
                        final UserService userService) {
        this.postService = postService;
        this.postTagService = postTagService;
        this.userService = userService;
    }

    public Page<Post> fetchPosts(final BasePaginationReq req) {
        // TODO: add binding data to convert string of default sort to an object
        PageRequest pageRequest = PaginationHelper.generatePageRequestWithDefaultSort(req, "-numberOfVotes");

        Page<Post> posts = postService.fetchPostsWithPageRequest(pageRequest);

        includePostTagsToPosts(posts);
        includeUserToPosts(posts);

        return posts;
    }

    @Transactional
    public Page<Post> searchPosts(final PostSearchReq req) {
        log.info("Search post with keyword #{}", req);

        // TODO: add binding data to convert string of default sort to an object
        PageRequest pageRequest = PaginationHelper.generatePageRequestWithDefaultSort(req, "-numberOfVotes");

        Page<Post> posts = postService.searchPostsByKeywordWithPageRequest(req.getKeyword(), pageRequest);

        includePostTagsToPosts(posts);
        includeUserToPosts(posts);

        return posts;
    }

    private void includePostTagsToPosts(Page<Post> posts) {
        Map<Long, List<PostTag>> postIdPostTagsMap =
                postTagService
                        .fetchByPostIdIn(posts.map(Post::getId).toSet())
                        .stream()
                        .collect(Collectors.groupingBy(PostTag::getPostId));

        posts.forEach(post -> post.setPostTags(postIdPostTagsMap.get(post.getId())));
    }

    private void includeUserToPosts(Page<Post> posts) {
        Map<Long, List<User>> userIdUserMap = userService.fetchByIdIn(posts.map(Post::getUserId).toSet())
                                                         .stream()
                                                         .collect(Collectors.groupingBy(User::getId));

        posts.forEach(post -> post.setUser(userIdUserMap.get(post.getUserId()).get(0)));
    }
}
