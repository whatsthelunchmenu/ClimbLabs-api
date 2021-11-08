package com.example.climblabs.post.domain.repository;

import com.example.climblabs.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
