package com.example.climblabs.global.utils.image;

import com.example.climblabs.global.exception.ClimbLabsException;
import com.example.climblabs.global.exception.ExceptionCode;
import com.example.climblabs.global.utils.image.dto.ImageFileDto;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Profile(value = "default")
@Slf4j
@Component
public class LocalSaveFileUtils implements ImageStorageUtils {

    @Override
    public List<ImageFileDto> saveToStorages(List<MultipartFile> images) {
        if (CollectionUtils.isEmpty(images)) {
            return Collections.emptyList();
        }
        List<File> files = convertMultipartFileToFile(images);
        return getFileInfos(files);
    }

    @Override
    public ImageFileDto saveToStorage(MultipartFile image) {
        if (image == null) {
            return new ImageFileDto("", "");
        }
        File file = toFile(image);
        return fileInfo().apply(file);
    }

    @Override
    public void deleteToImage(String fileName) {
        try {
            String path = Paths.get(System.getProperty("java.io.tmpdir"), fileName).toString();
            File readFile = new File(path);
            readFile.delete();
        } catch (Exception e) {
            log.info("File remove error : {}", e.getMessage());
            throw new ClimbLabsException(ExceptionCode.FAIL_DELETE_IMAGE);
        }
    }

    @Override
    public void deleteToImages(List<String> paths) {
        paths.stream()
                .forEach(it -> deleteToImage(it));
    }

    private List<ImageFileDto> getFileInfos(List<File> images) {
        return images.stream()
                .map(fileInfo())
                .collect(Collectors.toList());
    }

    private Function<File, ImageFileDto> fileInfo() {
        return file -> {
            String fileName = file.getName();
            String fullPath = file.getPath();//Paths.get(System.getProperty("java.io.tmpdir"), fileName).toString();
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
            File file = File.createTempFile("temp-", ".jpg", null);
            try (FileOutputStream stream = new FileOutputStream(file)) {
                stream.write(multipartFile.getBytes());
                return file;
            }
        } catch (Exception ex) {
            throw new ClimbLabsException(ExceptionCode.FAIL_SAVE_IMAGE);
        }
    }

    private String getExtension(String fileName) {
        if (Strings.isEmpty(fileName)) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
