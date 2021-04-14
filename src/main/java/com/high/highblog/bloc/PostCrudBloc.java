package com.high.highblog.bloc;

import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.mapper.PostMapper;
import com.high.highblog.model.dto.request.PostCreateReq;
import com.high.highblog.model.dto.request.PostUpdateReq;
import com.high.highblog.model.entity.Post;
import com.high.highblog.model.entity.PostStatistic;
import com.high.highblog.model.entity.PostTag;
import com.high.highblog.model.entity.User;
import com.high.highblog.service.FavoritePostService;
import com.high.highblog.service.PostService;
import com.high.highblog.service.PostStatisticService;
import com.high.highblog.service.PostTagService;
import com.high.highblog.service.PostVoteService;
import com.high.highblog.service.SubscriptionService;
import com.high.highblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
public class PostCrudBloc {

    private final PostService postService;
    private final PostTagService postTagService;
    private final PostStatisticService postStatisticService;
    private final PostVoteService postVoteService;
    private final UserService userService;
    private final FavoritePostService favoritePostService;
    private final SubscriptionService subscriptionService;

    public PostCrudBloc(final PostService postService,
                        final PostTagService postTagService,
                        final PostStatisticService postStatisticService,
                        final PostVoteService postVoteService,
                        final UserService userService,
                        final FavoritePostService favoritePostService,
                        final SubscriptionService subscriptionService) {
        this.postService = postService;
        this.postTagService = postTagService;
        this.postStatisticService = postStatisticService;
        this.postVoteService = postVoteService;
        this.userService = userService;
        this.favoritePostService = favoritePostService;
        this.subscriptionService = subscriptionService;
    }

    @Transactional
    public Long createPost(final PostCreateReq postCreateReq) {
        log.info("Create new post with data #{}", postCreateReq);

        Post post = PostMapper.INSTANCE.toPost(postCreateReq);
        post.setUserId(SecurityHelper.getUserId());
        postService.saveNew(post);

        List<PostTag> postTags = (List<PostTag>) CollectionUtils.emptyIfNull(post.getPostTags());

        postTags.forEach(postTag -> postTag.setPostId(post.getId()));

        postTagService.saveNew(postTags);

        postStatisticService.saveNew(PostStatistic.builder()
                                                  .postId(post.getId())
                                                  .build());

        return post.getId();
    }

    @Transactional(readOnly = true)
    public Post getPostDetailForCurrentUser(final Long id) {
        log.info("Get post detail for user by id #{}", id);

        Post post = postService.getByIdAndUserId(id, SecurityHelper.getUserId());

        List<PostTag> postTags = postTagService.fetchByPostId(post.getId());

        post.setPostTags(postTags);

        return post;
    }

    @Transactional(readOnly = true)
    public Post getPostDetail(final Long id) {
        log.info("Get post detail by id #{}", id);

        Post post = postService.getById(id);

        post.setPostTags(postTagService.fetchByPostId(post.getId()));
        post.setUser(userService.getById(post.getUserId()));
        post.setPostStatistic(postStatisticService.getByPostId(post.getId()));

        includeExtraInfoForPostDetailIfUserLogined(post);

        return post;
    }

    @Transactional
    public void updatePostForCurrentUser(final Long id, final PostUpdateReq postUpdateReq) {

        Post dbPost = postService.getByIdAndUserId(id, SecurityHelper.getUserId());
        Post newPost = PostMapper.INSTANCE.toPost(postUpdateReq, dbPost);
        postService.save(newPost);

        List<PostTag> postTags = (List<PostTag>) CollectionUtils.emptyIfNull(newPost.getPostTags());

        postTags.forEach(postTag -> postTag.setPostId(id));

        postTagService.deleteOldAndSaveNew(newPost.getId(), postTags);
    }

    @Transactional
    public void deletePostForCurrentUser(final Long id) {
        Long userId = SecurityHelper.getUserId();
        log.info("Delete post by id #{} with userId #{}", id, userId);

        postService.delete(id, userId);
        postTagService.deleteAll(id);
    }

    private void includeExtraInfoForPostDetailIfUserLogined(Post post) {
        try {
            Long userId = SecurityHelper.getUserId();
            if (ObjectUtils.isNotEmpty(userId)) {
                post.setPostVote(postVoteService.getNullableByPostIdAndUserId(post.getId(), userId));

                post.setAddedToFavorite(favoritePostService.existsByPostIdAndUserId(post.getId(), userId));

                User postOwner = post.getUser();

                postOwner.setFollowed(subscriptionService.existsByUserIdAndFollowerId(postOwner.getId(), userId));
            }
        }catch (Exception ex){
            log.info("Extra info of post detail is not set");
        }
    }
}
