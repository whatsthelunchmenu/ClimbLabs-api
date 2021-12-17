package com.example.climblabs.post.domain.repository;

import com.example.climblabs.post.domain.Post;
import com.example.climblabs.post.domain.ScaleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p left join fetch p.images left join fetch p.advantages left join fetch p.disAdvantages")
    List<Post> findAllByPosts(Pageable pageable);

    @Query("select p from Post p left join fetch p.images left join fetch p.advantages left join fetch p.disAdvantages where p.title like %:title%")
    List<Post> findLikeTitlePosts(@Param("title") String title, Pageable pageable);

    @Query("select count (p) from Post p where p.title like %:title%")
    long countLikeTitlePosts(@Param("title") String title);

    @Query(value = "select * from post order by rand() limit :limit", nativeQuery = true)
    List<Post> findByRandomLimitPost(int limit);

    @Query(value = "select * from post where scale_type = :scaleType order by rand() limit :limit", nativeQuery = true)
    List<Post> findByRandomScaleTypeLimit(String scaleType, Integer limit);
}
