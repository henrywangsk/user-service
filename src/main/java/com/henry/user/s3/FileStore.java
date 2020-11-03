package com.henry.user.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
public class FileStore {
    private final AmazonS3 s3;
    private static final String bucketName = "user-service-user-profile-img";

    @Autowired
    public FileStore(AmazonS3 s3) {
        this.s3 = s3;
    }

    private String getS3FilePath(String path) {
        return String.format("%s/%s", bucketName, path);
    }

    public void save(String path,
                       String fileName,
                       Map<String, String> metadata,
                       InputStream inputStream) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        metadata.forEach(objectMetadata::addUserMetadata);

        s3.putObject(getS3FilePath(path), fileName, inputStream, objectMetadata);
    }

    public byte[] downloadProfileImage(String path, String key) {
        final S3ObjectInputStream objectContent = s3.getObject(getS3FilePath(path), key).getObjectContent();
        try {
            return IOUtils.toByteArray(objectContent);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("Failed to download %s/%s", path, key));
        }
    }
}
