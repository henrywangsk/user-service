package com.henry.user.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/v1/profiles")
public class UserProfileController {
    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<UserProfile> getUserProfiles() {
        return userProfileService.getUserProfiles();
    }

    @PostMapping(
            path = "{userId}/image/upload",
            consumes = MULTIPART_FORM_DATA_VALUE
    )
    public String uploadProfileImage(@PathVariable UUID userId,
                                   @RequestParam("file") MultipartFile file) {
        return userProfileService.uploadProfileImage(userId, file);
    }

    @GetMapping(
            path = "{userId}/image/{imageLink}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public byte[] downloadProfileImage(@PathVariable UUID userId, @PathVariable String imageLink) {
        return userProfileService.downloadProfileImage(userId, imageLink);
    }
}
