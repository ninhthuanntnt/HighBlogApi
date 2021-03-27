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

    @NotNull
    @Builder.Default
    private boolean donated = false;

    @Builder.Default
    @NotNull
    @Column(name = "number_of_votes")
    private Long numberOfVotes = 0L;

    @Column(name = "thumbnail_image_path")
    private String thumbnailImagePath;

    @Column(name = "cover_image_path")
    private String coverImagePath;

    @NotNull
    @Column(name = "post_type")
    private PostType postType;

    @Transient
    private List<PostTag> postTags;

    @Transient
    private User user;
}
