package com.henry.user.profile;

import com.henry.user.db.FakeDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserProfileDAO {
    private final FakeDB db;

    @Autowired
    public UserProfileDAO(FakeDB db) {
        this.db = db;
    }

    public List<UserProfile> getUserProfiles() {
        return db.getUserProfiles();
    }
}
