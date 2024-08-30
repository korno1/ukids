package com.modernfamily.ukids.global.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
public class S3Manager {

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;

    public void deleteFile(String s3FileName) throws IOException {
        try {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, s3FileName));
        } catch (AmazonS3Exception e) {
            throw new IOException("Error deleting photo ", e);
        }
    }

    public Map<String, Object> uploadFile(MultipartFile multipartFile, String path) throws IOException {
        Map<String, Object> uploadParam = new HashMap<>();

        String localFileName = UUID.randomUUID() +"_" +multipartFile.getOriginalFilename();
        File uploadFile = convert(multipartFile, localFileName)
                .orElseThrow(() -> new ExceptionResponse(CustomException.FAIL_TO_CONVERT_FILE_EXCEPTION));

        String generatedFileName = path + "/" + localFileName;

        uploadParam.put("originalName", uploadFile.getName());
        uploadParam.put("s3FileName", generatedFileName);

        try {
            amazonS3Client.putObject(
                    new PutObjectRequest(bucketName, generatedFileName, uploadFile)
                            .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonS3Exception e) {
            throw new IOException("Error uploading file", e);
        }
        String uploadImageUrl = amazonS3Client.getUrl(bucketName, generatedFileName).toString();
        uploadParam.put("uploadImageUrl", uploadImageUrl);

        uploadFile.delete();
        log.info("Local file deleted : {}", uploadFile.getAbsolutePath());

        return uploadParam;
    }

    private Optional<File> convert(MultipartFile file, String fileName) throws IOException {
        log.info("Converting file: {}", fileName);
        File convertFile = new File(fileName);
        if (convertFile.createNewFile()) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(convertFile)) {
                fileOutputStream.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
}
