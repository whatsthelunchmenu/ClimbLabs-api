package com.example.climblabs.post.domain.repository;

import com.example.climblabs.post.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p left join fetch p.images left join fetch p.advantages left join fetch p.disAdvantages")
    List<Post> findAllByPosts(Pageable pageable);
}
