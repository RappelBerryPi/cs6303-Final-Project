package edu.utdallas.cs6303.finalproject.services.oauth;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;

import javax.transaction.NotSupportedException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import edu.utdallas.cs6303.finalproject.model.database.GithubOidcUser;
import edu.utdallas.cs6303.finalproject.model.database.GoogleOidcUser;
import edu.utdallas.cs6303.finalproject.model.database.User;
import edu.utdallas.cs6303.finalproject.model.database.repositories.GithubOidcUserRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.GoogleOidcUserRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.RoleRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.UserRepository;
import net.bytebuddy.utility.RandomString;

@Service
public class OidcUserAuthenticationService {

    @Autowired
    private GoogleOidcUserRepository googleOidcUserRepository;

    @Autowired
    private GithubOidcUserRepository githubOidcUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUserAndLinkToOidc(OidcUser principal) throws NotSupportedException, MalformedURLException {
        User user = null;
        if (OidcUserAuthenticationService.isGoogleAuthenticationUser(principal)) {
            user = this.createUserFromGoogleOidcDetails(principal);
        } else if (OidcUserAuthenticationService.isGithubAuthenticationUser(principal)) {
            user = this.createUserFromGithubOidcDetails(principal);
        }
        return user;
    }

    public User createUserFromGoogleOidcDetails(OidcUser principal) {
        User           user       = null;
        String         googleId   = principal.getAttribute("sub");
        GoogleOidcUser gPrincipal = googleOidcUserRepository.findByGoogleId(googleId);
        if (gPrincipal == null) {
            gPrincipal = new GoogleOidcUser(principal);
            googleOidcUserRepository.save(gPrincipal);
        } else {
            user = userRepository.findByGoogleOidcUser(gPrincipal);
        }
        if (user == null) {
            user = new User();
            user.setEmail(gPrincipal.getEmail());
            user.setUsername(gPrincipal.getEmail());
            int length = new Random().nextInt(100);
            user.setPassword(passwordEncoder.encode(RandomString.make(length)));
            user.Enable();
            user.setFirstName(gPrincipal.getGivenName());
            user.setLastName(gPrincipal.getFamilyName());
            user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
            user.setGoogleOidcUser(gPrincipal);
            userRepository.save(user);
        }
        return user;
    }

    public User createUserFromGithubOidcDetails(OidcUser principal) {
        User           user       = null;
        long           githubId   = GithubOidcUser.getGithubId(principal);
        GithubOidcUser gPrincipal = githubOidcUserRepository.findByGithubId(githubId);
        if (gPrincipal == null) {
            gPrincipal = new GithubOidcUser(principal);
            githubOidcUserRepository.save(gPrincipal);
        } else {
            user = userRepository.findByGithubOidcUser(gPrincipal);
        }
        if (user == null) {
            user = new User();
            user.setEmail(gPrincipal.getEmail());
            user.setUsername(String.valueOf(gPrincipal.getGithubID()));
            user.setPassword(RandomString.make());
            user.Enable();
            String[] nameSplit = gPrincipal.getName().split("\\s+");
            user.setFirstName(nameSplit[0]);
            user.setLastName(nameSplit[nameSplit.length - 1]);
            user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
            user.setGithubOidcUser(gPrincipal);
            userRepository.save(user);
        }
        return user;
    }

    public User getUserFromOAuth2AuthenticationToken(OAuth2AuthenticationToken token) {
        if (OidcUserAuthenticationService.isGithubAuthenticationToken(token)) {
            long             githubId = GithubOidcUser.getGithubId(token.getPrincipal());
            GithubOidcUser user     = githubOidcUserRepository.findByGithubId(githubId);
            if (user == null) {
                return null;
            }
            return userRepository.findByGithubOidcUser(user);
        } else if (OidcUserAuthenticationService.isGoogleAuthenticationToken(token)) {
            String           googleId = GoogleOidcUser.getGoogleId(token.getPrincipal());
            GoogleOidcUser user     = googleOidcUserRepository.findByGoogleId(googleId);
            if (user == null) {
                return null;
            }
            return userRepository.findByGoogleOidcUser(user);
        } else {
            return null;
        }
    }

    @Transactional
    public OidcUser correctOidcUserAuthorities(OidcUser oidcUser) throws MalformedURLException, NotSupportedException {
        User user = this.createUserAndLinkToOidc(oidcUser);
        if (OidcUserAuthenticationService.isGoogleAuthenticationUser(oidcUser)) {
            return new GoogleOidcUser(oidcUser, user);
        } else if (OidcUserAuthenticationService.isGithubAuthenticationUser(oidcUser)) {
            return new GithubOidcUser(oidcUser, user);
        }
        return oidcUser;
    }

    public static boolean isGoogleAuthenticationToken(OAuth2AuthenticationToken token) {
        return OidcUserAuthenticationService.isGoogleAuthenticationUser(token.getPrincipal());
    }

    public static boolean isGoogleAuthenticationUser(OAuth2User principal) {
        return OidcUserAuthenticationService.genericAuthenticationUserHostMatches(principal, "google");
    }

    public static boolean isGithubAuthenticationToken(OAuth2AuthenticationToken token) {
        return OidcUserAuthenticationService.isGithubAuthenticationUser(token.getPrincipal());
    }

    public static boolean isGithubAuthenticationUser(OAuth2User principal) {
        return OidcUserAuthenticationService.genericAuthenticationUserHostMatches(principal, "github");
    }

    private static boolean genericAuthenticationUserHostMatches(OAuth2User principal, String hostToMatch) {
        try {
            URL url = OidcUserAuthenticationService.getURL(principal);
            if (url.getHost().contains(hostToMatch)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private static URL getURL(OAuth2User principal) throws NotSupportedException, MalformedURLException {
        URL url = principal.getAttribute("iss");
        if (url == null) {
            String strURL = principal.getAttribute("url");
            if (strURL == null) {
                throw new NotSupportedException("unable to get details from your provider. Please contact support.");
            }
            url = new URL(strURL);
        }
        return url;
    }

}
