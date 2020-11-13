package edu.utdallas.cs6303.finalproject.model.database.repositories;

import edu.utdallas.cs6303.finalproject.model.database.GithubOidcUser;
import edu.utdallas.cs6303.finalproject.model.database.GoogleOidcUser;
import edu.utdallas.cs6303.finalproject.model.database.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findById(Integer id);
    User findByEmail(String email);
    User findByGoogleOidcUser(GoogleOidcUser googleOidcUser);
    User findByGithubOidcUser(GithubOidcUser githubOidcUser);

    @Override
    void delete(User user);
}