package com.example.climblabs.global.utils.image;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.climblabs.global.exception.OtherPlatformHttpException;
import com.example.climblabs.global.utils.image.dto.ImageFileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Profile("prod")
@Slf4j
@RequiredArgsConstructor
@Component
public class AwsS3UploaderUtils implements ImageStorageUtils {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public List<ImageFileDto> saveToStorage(List<MultipartFile> images) {
        List<File> files = convert(images);
        return uploads(files);
    }

    private List<ImageFileDto> uploads(List<File> files) {
        List<ImageFileDto> imageFileDtos = files.stream().map(upload()).collect(Collectors.toList());
        removeFiles(files);
        return imageFileDtos;
    }

    private Function<File, ImageFileDto> upload() {
        return file -> {
            String convertName = UUID.randomUUID() + getExtension(file.getName());
            String urlPath = putS3(convertName, file);
            return ImageFileDto.builder()
                    .name(convertName)
                    .url(urlPath)
                    .build();
        };
    }

    private void removeFiles(List<File> files) {
        files.stream()
                .forEach(it -> it.delete());
    }

    private String putS3(String fileName, File uploadFile) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private String getExtension(String fileName) {
        if (Strings.isEmpty(fileName)) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private List<File> convert(List<MultipartFile> images) {
        return images.stream()
                .map(toFile())
                .collect(Collectors.toList());
    }

    private Function<MultipartFile, File> toFile() {
        return multipartFile -> {
            try {
                return multipartFile.getResource().getFile();
            } catch (IOException e) {
                log.warn(e.getMessage());
                return createTempFile(multipartFile);
            }
        };
    }

    private File createTempFile(MultipartFile multipartFile) {
        try {
            File tempFile = File.createTempFile("temp", ".jpg", null);
            try (FileOutputStream stream = new FileOutputStream(tempFile)) {
                stream.write(multipartFile.getBytes());
                return tempFile;
            }
        } catch (IOException e) {
            throw new OtherPlatformHttpException();
        }
    }
}
