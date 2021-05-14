package com.high.highblog.model.entity;

import com.high.highblog.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "hb_posts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Post
        extends AbstractAuditingColumns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @Column(name = "category_id")
    private Long categoryId;

    @NotNull
    private String title;

    @Column(columnDefinition = "tinytext")
    private String summary;

    @Column(columnDefinition = "text")
    private String content;

    @Builder.Default
    @NotNull
    private boolean donated = false;

    @Column(name = "thumbnail_image_path")
    private String thumbnailImagePath;

    @Column(name = "cover_image_path")
    private String coverImagePath;

    @Column(name = "deleted")
    private Boolean deleted;

    @Builder.Default
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "post_type", nullable = false)
    private PostType postType = PostType.DRAFT;

    @Transient
    private Boolean addedToFavorite;

    @Transient
    private List<PostTag> postTags;

    @Transient
    private PostVote postVote;

    @Transient
    private User user;

    @Transient
    private PostStatistic postStatistic;

    public Post(final Post post, final PostStatistic postStatistic) {
        this.id = post.id;
        this.userId = post.userId;
        this.categoryId = post.categoryId;
        this.title = post.title;
        this.summary = post.summary;
        this.content = post.content;
        this.donated = post.donated;
        this.thumbnailImagePath = post.thumbnailImagePath;
        this.coverImagePath = post.coverImagePath;
        this.postType = post.postType;
        this.postTags = post.postTags;
        this.deleted = post.deleted;
        this.setCreatedBy(post.getCreatedBy());
        this.setCreatedDate(post.getCreatedDate());
        this.setLastModifiedBy(post.getLastModifiedBy());
        this.setLastModifiedDate(post.getLastModifiedDate());

        this.postStatistic = postStatistic;
    }
}
