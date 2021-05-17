package com.high.highblog.bloc.admin;

import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.model.dto.request.admin.AdminPostSearchReq;
import com.high.highblog.model.entity.Post;
import com.high.highblog.model.entity.PostStatistic;
import com.high.highblog.model.entity.PostTag;
import com.high.highblog.model.entity.User;
import com.high.highblog.service.PostService;
import com.high.highblog.service.PostStatisticService;
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
@Component("adminPostlistBloc")
public class PostListBloc {

    private final PostService postService;
    private final PostTagService postTagService;
    private final UserService userService;
    private final PostStatisticService postStatisticService;

    public PostListBloc(final PostService postService,
                        final PostTagService postTagService,
                        final UserService userService,
                        final PostStatisticService postStatisticService) {
        this.postService = postService;
        this.postTagService = postTagService;
        this.userService = userService;
        this.postStatisticService = postStatisticService;
    }

    @Transactional(readOnly = true)
    public Page<Post> searchDynamicPosts(Long categoryId, final AdminPostSearchReq req) {
        // TODO: add binding data to convert string of default sort to an object
        PageRequest pageRequest = PaginationHelper.generatePageRequestWithDefaultSort(req,
                "-ps.numberOfVotes");
        Long userId =null;
        if(req.getNickName() != null) userId =  userService.getByNickName(req.getNickName()).getId();
        List<Long> tagIds = req.getTagIds();
        String keyword = req.getKeyWord();
        Page<Post> posts = postService.searchDynamicPosts(categoryId, userId, tagIds ,pageRequest ,keyword);

        // Should use include to make better performance
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
        Map<Long, User> userIdUserMap = userService.fetchByIdIn(posts.map(Post::getUserId).toSet())
                .stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        posts.forEach(post -> post.setUser(userIdUserMap.get(post.getUserId())));
    }

    private void includePostStatisticToPosts(Page<Post> posts) {
        Map<Long, PostStatistic> postIdPostStatisticMap =
                postStatisticService.fetchByPostIdIn(posts.map(Post::getId).toSet())
                        .stream()
                        .collect(Collectors.toMap(PostStatistic::getPostId,
                                postStatistic -> postStatistic));

        posts.forEach(post -> post.setPostStatistic(postIdPostStatisticMap.get(post.getId())));
    }
    @Transactional(readOnly = true)
    public Long countPosts() {
        log.info("count number of posts");
        return postService.countPosts();
    }
}

