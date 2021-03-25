package com.high.highblog.bloc;

import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.mapper.PostMapper;
import com.high.highblog.model.dto.request.PostCreateReq;
import com.high.highblog.model.dto.request.PostUpdateReq;
import com.high.highblog.model.entity.Post;
import com.high.highblog.model.entity.PostTag;
import com.high.highblog.service.PostService;
import com.high.highblog.service.PostTagService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public void createPost(final PostCreateReq postCreateReq) {
        log.info("Create new post with data #{}", postCreateReq);

        Post post = PostMapper.INSTANCE.toPost(postCreateReq);
        post.setUserId(SecurityHelper.getUserId());
        postService.saveNew(post);

        List<PostTag> postTags = (List<PostTag>) CollectionUtils.emptyIfNull(post.getPostTags());

        postTags.forEach(postTag -> postTag.setPostId(post.getId()));

        postTagService.saveNew(postTags);
    }

    @Transactional(readOnly = true)
    public Post getPostDetailForCurrentUser(final Long id) {
        log.info("Get post detail for user by id #{}", id);

        Post post = postService.getByIdAndUserId(id, SecurityHelper.getUserId());

        List<PostTag> postTags = postTagService.getByPostId(post.getId());

        post.setPostTags(postTags);

        return post;
    }

    @Transactional
    public Post getPostDetail(final Long id) {
        log.info("Get post detail by id #{}", id);

        Post post = postService.getById(id);
        List<PostTag> postTags = postTagService.getByPostId(post.getId());

        post.setPostTags(postTags);

        return post;
    }

    @Transactional
    public void updatePost(final Long id, final PostUpdateReq postUpdateReq) {

        Post dbPost = postService.getByIdAndUserId(id, SecurityHelper.getUserId());
        Post newPost = PostMapper.INSTANCE.toPost(postUpdateReq, dbPost);
        postService.save(newPost);

        List<PostTag> postTags = (List<PostTag>) CollectionUtils.emptyIfNull(newPost.getPostTags());

        postTags.forEach(postTag -> postTag.setPostId(id));

        postTagService.deleteOldAndSaveNew(newPost.getId(), postTags);
    }
}
