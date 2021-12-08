package com.example.climblabs.global.utils.image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Profile("prod")
@Slf4j
@Component
public class LocalSaveFileUtils implements ImageStorageUtils {

    @Override
    public List<String> saveToStorage(List<MultipartFile> images) {
        if (CollectionUtils.isEmpty(images)) {
            return new ArrayList<>();
        }

        return saveImageFile(images);
    }

    private List<String> saveImageFile(List<MultipartFile> images) {
        return images.parallelStream().map(it -> convertMultipartFileToFile(it))
                .collect(Collectors.toList());
    }

    private String convertMultipartFileToFile(MultipartFile multipartFile) {
        String extension = getExtension(multipartFile.getOriginalFilename());
        String fullPath = System.getProperty("user.dir") + "/" + UUID.randomUUID() + extension;
        return save(multipartFile, fullPath);
    }

    private String save(MultipartFile file, String fullPath) {
        File newFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        try {
            if (newFile.createNewFile()) {
                log.info("파일생성");
                try (FileOutputStream stream = new FileOutputStream(newFile)) {
                    stream.write(file.getBytes());
                }
            }
            return fullPath;
        } catch (IOException e) {
            log.warn(e.getMessage());
            return "";
        }
    }

    private String getExtension(String fileName) {
        if (Strings.isEmpty(fileName)) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
