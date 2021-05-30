package com.high.highblog.bloc;

import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.dto.request.PostSearchReq;
import com.high.highblog.model.entity.Post;
import com.high.highblog.model.entity.PostStatistic;
import com.high.highblog.model.entity.PostTag;
import com.high.highblog.model.entity.User;
import com.high.highblog.service.PostService;
import com.high.highblog.service.PostStatisticService;
import com.high.highblog.service.PostTagService;
import com.high.highblog.service.SubscriptionService;
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
    public Page<Post> fetchPosts(Long categoryId, final BasePaginationReq req) {
        // TODO: add binding data to convert string of default sort to an object
        PageRequest pageRequest = PaginationHelper.generatePageRequestWithoutSort(req);

        Page<Post> posts = postService.fetchPostsWithPageRequest(categoryId, pageRequest);

        // Should use include to make better performance
        includePostTagsToPosts(posts);
        includeUserToPosts(posts);

        return posts;
    }

    public Page<Post> fetchPostsByNickName(final String nickName, Long categoryId, final BasePaginationReq req) {
        log.info("Fetch list posts by nickName #{} with req #{}", nickName, req);
        PageRequest pageRequest = PaginationHelper.generatePageRequestWithDefaultSort(req,
                                                                                      "-ps.id");

        Page<Post> posts = postService.fetchPostsByNickNameWithPageRequest(nickName, categoryId, pageRequest);

        includePostTagsToPosts(posts);
        includeUserToPosts(posts);
        return posts;
    }

    public Page<Post> fetchPostsByTagId(final Long tagId, final Long categoryId, final BasePaginationReq req) {
        log.info("Fetch list posts by tagId #{} with req #{}", tagId, req);
        PageRequest pageRequest = PaginationHelper.generatePageRequestWithDefaultSort(req,
                                                                                      "-ps.numberOfVotes");

        Page<Post> posts = postService.fetchPostsByTagIdWithPageRequest(tagId, categoryId, pageRequest);

        includePostTagsToPosts(posts);
        includeUserToPosts(posts);
        return posts;
    }

    public Page<Post> fetchSubscriptionPostsForCurrentUser(Long categoryId, final BasePaginationReq req) {
        log.info("Fetch list subscription posts for current user");

        PageRequest pageRequest = PaginationHelper.generatePageRequest(req);

        Page<Post> posts = postService
                .fetchPostsByFollowerIdWithPageRequest(SecurityHelper.getUserId(), categoryId, pageRequest);

        includePostTagsToPosts(posts);
        includeUserToPosts(posts);

        return posts;
    }

    @Transactional(readOnly = true)
    public Page<Post> searchPosts(final PostSearchReq req) {
        log.info("Search post with keyword #{}", req);

        // TODO: add binding data to convert string of default sort to an object
        Page<Post> posts;
        if (req.getKeyword().length() <= 2) {
            PageRequest pageRequest = PaginationHelper.generatePageRequestWithDefaultSort(req,
                                                                                          "-ps.numberOfVotes");
            posts = postService.searchPostsByKeywordLikeWithPageRequest(req.getKeyword(), pageRequest);
        } else {
            PageRequest pageRequest = PaginationHelper.generatePageRequestWithoutSort(req);
            String keyword = req.getKeyword().replace("( +)", " ")
                                .trim();
            posts = postService.searchFullTextPostsByKeywordWithPageRequest(keyword, pageRequest);
            if (posts.getContent().size() == 0) {
                keyword = keyword.replace(" ", "|");
                posts = postService.searchPostsByKeywordRegexpWithPageRequest(keyword, pageRequest);
            }
        }

        includePostTagsToPosts(posts);
        includePostStatisticToPosts(posts);
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
}
