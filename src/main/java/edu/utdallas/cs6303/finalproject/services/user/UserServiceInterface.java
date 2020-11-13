package edu.utdallas.cs6303.finalproject.services.user;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.google.zxing.WriterException;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import edu.utdallas.cs6303.finalproject.model.database.Privilege;
import edu.utdallas.cs6303.finalproject.model.database.User;
import edu.utdallas.cs6303.finalproject.model.validation.CreateUserForm;

public interface UserServiceInterface {
    User createUserFromUserForm(CreateUserForm createUserForm);

    CreateUserForm createUseFormFromUser(User user);

    void addAuthenticationTokenToSession(HttpServletRequest request, User user, String password);

    Optional<User> findByUserName(String userName);

    boolean isPasswordValid(User user, String password);

    Collection<GrantedAuthority> getAuthorities(User user);

    List<Privilege> getBasicUserPrivileges();

    List<Privilege> getAdminPrivileges();

    List<Privilege> getBasicEmployeePrivileges();
    
    ResponseEntity<Resource> getQRCode(Authentication authentication) throws WriterException, IOException;

    User getUserFromAuthentication(Authentication authentication);

    Map<String, String> getUserSecret(Authentication authentication);
}
