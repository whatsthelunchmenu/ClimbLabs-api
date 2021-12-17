package com.example.climblabs.post.web.dto.request;

import lombok.Value;

@Value
public class PostSearchRequest {
    private String searchType;
    private String searchValue;
}
