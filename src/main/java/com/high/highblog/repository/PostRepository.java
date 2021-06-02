package com.high.highblog.repository;

import com.high.highblog.enums.PostType;
import com.high.highblog.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository
        extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndUserId(Long id, Long userId);

    Optional<Post> findByIdAndPostType(Long id, PostType postType);

    @Query(value = "SELECT * FROM hb_posts AS p"
            + " INNER JOIN hb_post_statistics AS ps ON ps.post_id = p.id"
            + " WHERE MATCH (p.title, p.summary, p.content) AGAINST (:keyword WITH QUERY EXPANSION)"
            + " AND p.deleted = false ",
            nativeQuery = true)
    Page<Post> searchFullTextPosts(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT p FROM Post p"
            + " JOIN PostStatistic ps ON ps.postId = p.id"
            + " WHERE p.deleted = false"
            + " AND p.postType = 'NORMAL'"
            + " AND ( p.title LIKE %:keyword%"
            + " OR p.summary LIKE %:keyword%"
            + " OR p.content LIKE %:keyword%)")
    Page<Post> searchPosts(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT p.* FROM hb_posts AS p"
            + " JOIN hb_post_statistics AS ps ON ps.post_id = p.id"
            + " WHERE p.deleted = false"
            + " AND p.postType = 'NORMAL'"
            + " AND ( p.title REGEXP :keyword"
            + " OR p.summary REGEXP :keyword"
            + " OR p.content REGEXP :keyword)", nativeQuery = true)
    Page<Post> searchPostsRegexp(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT new Post(p, ps) FROM Post p"
            + " JOIN PostStatistic ps ON ps.postId = p.id"
            + " WHERE p.categoryId = :categoryId"
            + " AND p.deleted = false "
            + " AND p.postType = 'NORMAL'"
            + " ORDER BY ps.numberOfVotes - FUNCTION('DATEDIFF', CURRENT_DATE, p.createdDate)*20 DESC")
    Page<Post> fetchByCategoryId(Long categoryId, Pageable pageable);

    @Query("SELECT p FROM Post p "
            + " JOIN FavoritePost fp ON fp.postId = p.id"
            + " WHERE fp.userId = :userId"
            + " AND p.postType = 'NORMAL'"
            + " AND p.deleted = false ")
    Page<Post> fetchListFavoritePostsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT new Post(p, ps) FROM Post p"
            + " JOIN User u ON u.id = p.userId"
            + " JOIN PostStatistic ps ON ps.postId = p.id"
            + " WHERE u.nickName = :nickName"
            + " AND p.categoryId = :categoryId "
            + " AND p.postType = 'NORMAL'"
            + " AND p.deleted = false ")
    Page<Post> fetchListPostsByNickName(@Param("nickName") String nickName, Long categoryId, Pageable pageable);

    @Query("SELECT new Post(p, ps) FROM Post p"
            + " JOIN Subscription sub ON sub.userId = p.userId"
            + " JOIN PostStatistic ps ON ps.postId = p.id"
            + " WHERE sub.followerId = :followerId"
            + " AND p.categoryId = :categoryId "
            + " AND p.postType = 'NORMAL'"
            + " AND p.deleted = false ")
    Page<Post> fetchListPostsByFollowerId(@Param("followerId") Long followerId, Long categoryId, Pageable pageable);

    @Query("SELECT new Post(p, ps) FROM Post p"
            + " JOIN PostTag pt ON pt.postId = p.id"
            + " JOIN PostStatistic ps ON ps.postId = p.id"
            + " WHERE pt.tagId = :tagId"
            + " AND p.categoryId = :categoryId"
            + " AND p.postType = 'NORMAL'"
            + " AND p.deleted = false "
            + " ORDER BY ps.numberOfVotes - FUNCTION('DATEDIFF', CURRENT_DATE, p.createdDate)*20 DESC"
    )
    Page<Post> fetchByTagIdAndCategoryId(@Param("tagId") Long tagId, Long categoryId, Pageable pageable);

    @Query("SELECT DISTINCT new Post(p, ps) FROM Post p"
            + " JOIN PostTag pt ON pt.postId = p.id"
            + " JOIN PostStatistic ps ON ps.postId = p.id"
            + " WHERE p.categoryId = :categoryId "
            + " AND (:userId IS NULL OR p.userId = :userId)"
            + " AND (COALESCE(:tagIds) IS NULL OR pt.tagId IN (:tagIds))"
            + " AND (:keyword IS NULL OR p.title LIKE %:keyword% OR p.summary LIKE %:keyword% OR p.content LIKE %:keyword%)")
    Page<Post> searchDynamicPosts(Long categoryId, Long userId, List<Long> tagIds, String keyword, Pageable pageable);

    Long countPostsByCategoryId(Long id);
}
