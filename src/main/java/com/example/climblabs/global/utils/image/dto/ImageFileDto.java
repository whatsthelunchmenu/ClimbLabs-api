package com.example.climblabs.global.utils.image.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ImageFileDto {
    private String name;
    private String url;
}
