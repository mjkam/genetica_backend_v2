package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AwsS3 {
    private final static String bucketName = "mjkamtestbucket";
    private final S3Client s3Client;

    public List<String> getFileList(String directory) {
        ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(listObjectRequest(directory));
        return listObjectsV2Response.contents().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }

    public String uploadFile(MultipartFile multipartFile, String directory) {
        try {
            String s3Name = UUID.randomUUID().toString();
            PutObjectResponse putObjectResponse = s3Client.putObject(createPutObjectRequest(s3Name, directory), getFileRequestBody(multipartFile));
            if (!putObjectResponse.sdkHttpResponse().isSuccessful()) {
                throw new RuntimeException("Upload Failed");
            }
            return s3Name;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(String fileName, String directory) {
        DeleteObjectResponse deleteObjectResponse = s3Client.deleteObject(deleteObjectRequest(fileName, directory));
        if (!deleteObjectResponse.sdkHttpResponse().isSuccessful()) {
            throw new RuntimeException("Remove Failed");
        }
    }

    private PutObjectRequest createPutObjectRequest(String fileName, String directory) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(directory + fileName)
                .build();
    }

    private DeleteObjectRequest deleteObjectRequest(String fileName, String directory) {
        return DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(directory + fileName)
                .build();
    }

    private ListObjectsV2Request listObjectRequest(String directory) {
        return ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(directory)
                .build();
    }

    private RequestBody getFileRequestBody(MultipartFile file) throws IOException {
        return RequestBody.fromInputStream(file.getInputStream(), file.getSize());
    }
}
