package com.henry.user.db;

import com.henry.user.profile.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class FakeDB {
    private static final List<UserProfile> USER_PROFILES = List.of(
            new UserProfile(UUID.fromString("71018221-2ad5-4f94-a8dd-a9994cd59191"), "Rose Kim", "16200225-2c9c-49a0-ac46-adb6c57dc6a6-rose.jpg"),
            new UserProfile(UUID.fromString("9d19fca8-880c-42e7-8993-7580d655faab"), "Jack Ma", "69144734-1bc3-4346-b9af-382e65210a1b-jack.jpg")
    );

    public List<UserProfile> getUserProfiles() {
        return USER_PROFILES;
    }
}
