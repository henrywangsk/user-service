package com.henry.user.profile;

import com.henry.user.s3.FileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.springframework.http.MediaType.*;

@Service
public class UserProfileService {
    private final UserProfileDAO userProfileDAO;
    private final FileStore fileStore;

    private final static List<String> ACCEPTED_FILE_TYPES = List.of(
            IMAGE_JPEG_VALUE,
            IMAGE_PNG_VALUE,
            IMAGE_GIF_VALUE
    );

    @Autowired
    public UserProfileService(UserProfileDAO userProfileDAO, FileStore fileStore) {
        this.userProfileDAO = userProfileDAO;
        this.fileStore = fileStore;
    }

    public List<UserProfile> getUserProfiles() {
        return List.copyOf(userProfileDAO.getUserProfiles());
    }

    public String uploadProfileImage(UUID userId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload empty file.");
        }

        if (!ACCEPTED_FILE_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("File must be an image");
        }

        final UserProfile userProfile = getUserProfileOrThrow(userId);

        Map<String, String> metadata = Map.of(
                "Content-Type", Objects.requireNonNullElse(file.getContentType(), ""),
                "Content-Length", String.valueOf(file.getSize())
        );

        final String fileName = String.format("%s-%s", UUID.randomUUID(), file.getOriginalFilename());
        try {
            fileStore.save(userId.toString(), fileName, metadata, file.getInputStream());
            userProfile.setImageLink(fileName);
            return fileName;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private UserProfile getUserProfileOrThrow(UUID userId) {
        return userProfileDAO.getUserProfiles()
                .stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("User %s not found", userId)));
    }

    public byte[] downloadProfileImage(UUID userId, String imageLink) {
        final UserProfile userProfile = getUserProfileOrThrow(userId);
        return userProfile.getImageLink()
                .filter(link -> Objects.equals(link, imageLink))
                .map(link -> fileStore.downloadProfileImage(userId.toString(), link))
                .orElse(null);
    }
}
