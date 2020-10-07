package edu.utdallas.cs6303.finalproject.model.database.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utdallas.cs6303.finalproject.model.database.GithubOAuth2User;

@Repository
public interface GithubOAuth2UserRepository extends JpaRepository<GithubOAuth2User, Long> { 
    GithubOAuth2User findByGithubId(long githubId);

}
