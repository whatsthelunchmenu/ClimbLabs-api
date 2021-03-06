package com.example.climblabs.global.utils.image;

import java.util.List;

import com.example.climblabs.global.utils.image.dto.ImageFileDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageUtils {

    List<ImageFileDto> saveToStorages(List<MultipartFile> images);

    ImageFileDto saveToStorage(MultipartFile image);

    void deleteToImages(List<String> paths);

    void deleteToImage(String path);
}
