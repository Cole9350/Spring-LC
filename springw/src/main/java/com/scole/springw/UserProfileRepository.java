package com.scole.springw;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
    // Additional custom queries can be added here if needed
}