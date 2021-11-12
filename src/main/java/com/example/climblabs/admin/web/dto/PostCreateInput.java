package com.example.climblabs.admin.web.dto;

import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Value
public class PostCreateInput {

    private String title;

    private String climbingTitle;

    private int level;

    private String zipCode;

    private String address;

    private String detailAddress;

    private String size;

    private String feature;

    private List<String> advantages;

    private List<String> disAdvantages;

    private List<MultipartFile> images;
}
