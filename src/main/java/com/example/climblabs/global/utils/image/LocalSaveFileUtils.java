package com.example.climblabs.global.utils.image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.example.climblabs.global.exception.OtherPlatformHttpException;
import com.example.climblabs.global.utils.image.dto.ImageFileDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Profile(value = {"local", "dev"})
@Slf4j
@Component
public class LocalSaveFileUtils implements ImageStorageUtils {


    @Override
    public List<ImageFileDto> saveToStorage(List<MultipartFile> images) {

        List<File> files = convertMultipartFileToFile(images);

        return saveImageFile(files);
    }

    private List<ImageFileDto> saveImageFile(List<File> images) {
        return images.stream()
            .map(saveFile())
            .collect(Collectors.toList());
    }

    private Function<File, ImageFileDto> saveFile() {
        return file -> {
            String fileName = UUID.randomUUID().toString();
            String extension = getExtension(file.getName());
            String fullPath = System.getProperty("user.dir") + "/" + fileName + extension;
            return ImageFileDto.builder()
                .name(fileName)
                .url(fullPath)
                .build();
        };
    }

    private List<File> convertMultipartFileToFile(List<MultipartFile> images) {
        return images.stream()
            .map(it -> toFile(it))
            .collect(Collectors.toList());
    }

    private File toFile(MultipartFile it) {
        try {
            return it.getResource().getFile();
        } catch (Exception ex) {
            return createTempFile(it);
        }
    }

    private File createTempFile(MultipartFile multipartFile) {
        try {
            File file = File.createTempFile("temp", ".jpg", null);
            try (FileOutputStream stream = new FileOutputStream(file)) {
                stream.write(multipartFile.getBytes());
                return file;
            }
        } catch (Exception ex) {
            throw new OtherPlatformHttpException();
        }
    }

    private String getExtension(String fileName) {
        if (Strings.isEmpty(fileName)) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
