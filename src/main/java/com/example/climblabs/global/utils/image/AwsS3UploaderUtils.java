package com.example.climblabs.global.utils.image;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Profile("local")
@Slf4j
@RequiredArgsConstructor
@Component
public class AwsS3UploaderUtils implements ImageStorageUtils {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public List<String> saveToStorage(List<MultipartFile> images) {
        if (CollectionUtils.isEmpty(images)) {
            return new ArrayList<>();
        }

        List<Optional<File>> convertedFile = images.stream()
                .map(it -> convert(it))
                .collect(Collectors.toList());

        return upload(convertedFile);
    }

    private List<String> upload(List<Optional<File>> convertedFile) {

        return convertedFile.stream().map(it -> {
            File uploadFile = it.orElseGet(() -> {
                try {
                    return File.createTempFile("temp", ".png");
                } catch (IOException e) {
                    log.warn(e.getMessage());
                }
                return null;
            });
            String fileName = uploadFile.getName();
            String convertedName = UUID.randomUUID() + getExtension(fileName);
            return putS3(convertedName, uploadFile);
        }).collect(Collectors.toList());
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

    private Optional<File> convert(MultipartFile file) {
        File newFile = new File( System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        try {
            if (newFile.createNewFile()) {
                try (FileOutputStream stream = new FileOutputStream(newFile)) {
                    stream.write(file.getBytes());
                }
                return Optional.of(newFile);
            }
        } catch (Exception ex) {
            log.warn("MultipartFIle -> File Convert fail~!!!");
        }
        return Optional.empty();
    }
}
