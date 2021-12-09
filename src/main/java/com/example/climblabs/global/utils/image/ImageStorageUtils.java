package com.example.climblabs.global.utils.image;

import java.util.List;

import com.example.climblabs.global.utils.image.dto.ImageFileDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageUtils {

    List<ImageFileDto> saveToStorage(List<MultipartFile> images);
}
