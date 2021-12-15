package com.example.climblabs.post.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Embeddable
public class Thumbnail {
    private String thumbNailName;
    private String thumbNailUrl;
}
