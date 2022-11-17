package com.youtube.clone.youtubeclone.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service implements FileService {

    private final AmazonS3Client amazonS3Client;
    public final String BUCKET_NAME = "youtube-clone-videos-sohamg";

    @Override
    public String uploadFile(MultipartFile multipartFile) {

        String filenameExtension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        String key = UUID.randomUUID() + "." + filenameExtension;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3Client.putObject(BUCKET_NAME, key, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Exception occurred while uploading file to S3");
        }

        amazonS3Client.setObjectAcl(BUCKET_NAME, key, CannedAccessControlList.PublicRead);

        return amazonS3Client.getResourceUrl(BUCKET_NAME, key);
    }
}
