package edu.utdallas.cs6303.finalproject.model.database.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utdallas.cs6303.finalproject.model.database.GoogleOAuth2User;

@Repository
public interface GoogleOAuth2UserRepository extends JpaRepository<GoogleOAuth2User, Long> { 
    GoogleOAuth2User findByGoogleId(String googleId);
}
