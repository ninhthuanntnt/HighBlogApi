package com.high.highblog.bloc;

import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.entity.Post;
import com.high.highblog.model.entity.PostTag;
import com.high.highblog.service.PostService;
import com.high.highblog.service.PostTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PostListBloc {

    private final PostService postService;
    private final PostTagService postTagService;

    public PostListBloc(final PostService postService,
                        final PostTagService postTagService) {
        this.postService = postService;
        this.postTagService = postTagService;
    }

    public Page<Post> fetchPosts(final BasePaginationReq req) {
        PageRequest pageRequest = PaginationHelper.generatePageRequest(req);

        Page<Post> posts = postService.fetchPostsWithPageRequest(pageRequest);

        Map<Long, List<PostTag>> postTagsMap = postTagService.fetchPostTagsByPostIdIn(posts.map(Post::getId).toList())
                                                             .stream()
                                                             .collect(Collectors.groupingBy(PostTag::getPostId));

        return posts.map(post -> {
            post.setPostTags(postTagsMap.get(post.getId()));
            return post;
        });
    }
}
