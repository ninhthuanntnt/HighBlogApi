package com.high.highblog.bloc;

import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.request.PostCreateReq;
import com.high.highblog.model.entity.Post;
import com.high.highblog.model.entity.PostTag;
import com.high.highblog.mapper.PostMapper;
import com.high.highblog.service.PostService;
import com.high.highblog.service.PostTagService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PostCrudBloc {

    private final PostService postService;
    private final PostTagService postTagService;

    public PostCrudBloc(final PostService postService,
                        final PostTagService postTagService) {
        this.postService = postService;
        this.postTagService = postTagService;
    }

    public void createPost(final PostCreateReq postCreateReq) {
        log.info("Create new post with data #{}", postCreateReq);

        Post post = PostMapper.INSTANCE.toPost(postCreateReq);
        post.setUserId(SecurityHelper.getUserId());
        postService.saveNew(post);

        if (ObjectUtils.isNotEmpty(postCreateReq.getTagCreateReqs())) {
            List<PostTag> postTags =
                    postCreateReq.getTagCreateReqs().stream().map(tagCreateReq -> PostTag.builder()
                                                                                         .postId(post.getId())
                                                                                         .tagId(tagCreateReq.getId())
                                                                                         .build())
                                 .collect(Collectors.toList());

            postTagService.saveNew(postTags);
        }
    }
}
