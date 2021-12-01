package com.example.climblabs.global.utils.image;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageUtils {

    List<String> saveToStorage(List<MultipartFile> images);
}
