package edu.utdallas.cs6303.finalproject.model.database.repositories;

import edu.utdallas.cs6303.finalproject.model.database.GithubOAuth2User;
import edu.utdallas.cs6303.finalproject.model.database.GoogleOAuth2User;
import edu.utdallas.cs6303.finalproject.model.database.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
    User findById(Integer id);
    User findByEmail(String email);
    User findByGoogleOAuth2User(GoogleOAuth2User googleOAuth2User);
    User findByGithubOAuth2User(GithubOAuth2User githubOAuth2User);

    @Override
    void delete(User user);
}