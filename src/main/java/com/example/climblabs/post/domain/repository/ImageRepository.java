package com.example.climblabs.post.domain.repository;

import com.example.climblabs.post.domain.Image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
