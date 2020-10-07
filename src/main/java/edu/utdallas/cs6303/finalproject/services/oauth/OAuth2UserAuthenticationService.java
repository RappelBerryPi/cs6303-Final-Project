package edu.utdallas.cs6303.finalproject.services.oauth;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import javax.transaction.NotSupportedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import edu.utdallas.cs6303.finalproject.model.database.GithubOAuth2User;
import edu.utdallas.cs6303.finalproject.model.database.GoogleOAuth2User;
import edu.utdallas.cs6303.finalproject.model.database.User;
import edu.utdallas.cs6303.finalproject.model.database.repositories.GithubOAuth2UserRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.GoogleOAuth2UserRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.UserRepository;
import net.bytebuddy.utility.RandomString;

@Service
public class OAuth2UserAuthenticationService {

    @Autowired
    private GoogleOAuth2UserRepository googleOAuth2UserRepository;

    @Autowired
    private GithubOAuth2UserRepository githubOAuth2UserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createUserAndLinkToOAuth2(OAuth2User principal) throws NotSupportedException, MalformedURLException {
        if (OAuth2UserAuthenticationService.isGoogleAuthenticationUser(principal)) {
            this.createUserFromGoogleOAuthDetails(principal);
        } else if (OAuth2UserAuthenticationService.isGithubAuthenticationUser(principal)) {
            this.createUserFromGithubOAuthDetails(principal);
        }
    }

    public User createUserFromGoogleOAuthDetails(OAuth2User principal) {
        User             user       = null;
        String           googleId   = principal.getAttribute("sub");
        GoogleOAuth2User gPrincipal = googleOAuth2UserRepository.findByGoogleId(googleId);
        if (gPrincipal == null) {
            gPrincipal = new GoogleOAuth2User(principal);
            googleOAuth2UserRepository.save(gPrincipal);
        } else {
            user = userRepository.findByGoogleOAuth2User(gPrincipal);
        }
        if (user == null) {
            user = new User();
            user.setEmail(gPrincipal.getEmail());
            user.setUserName(gPrincipal.getEmail());
            int length = new Random().nextInt(100);
            user.setPassword(passwordEncoder.encode(RandomString.make(length)));
            user.Enable();
            user.setFirstName(gPrincipal.getGivenName());
            user.setLastName(gPrincipal.getFamilyName());
            user.setRoles(null);
            user.setGoogleOAuth2User(gPrincipal);
            userRepository.save(user);
        }
        return user;
    }

    public User createUserFromGithubOAuthDetails(OAuth2User principal) {
        User             user       = null;
        long             githubId   = GithubOAuth2User.getGithubId(principal);
        GithubOAuth2User gPrincipal = githubOAuth2UserRepository.findByGithubId(githubId);
        if (gPrincipal == null) {
            gPrincipal = new GithubOAuth2User(principal);
            githubOAuth2UserRepository.save(gPrincipal);
        } else {
            user = userRepository.findByGithubOAuth2User(gPrincipal);
        }
        if (user == null) {
            user = new User();
            user.setEmail(gPrincipal.getEmail());
            user.setUserName(String.valueOf(gPrincipal.getGithubID()));
            user.setPassword(RandomString.make());
            user.Enable();
            String[] nameSplit = gPrincipal.getName().split("\\s+");
            user.setFirstName(nameSplit[0]);
            user.setLastName(nameSplit[nameSplit.length - 1]);
            user.setRoles(null);
            user.setGithubOAuth2User(gPrincipal);
            userRepository.save(user);
        }
        return user;
    }

    public User getUserFromOAuth2AuthenticationToken(OAuth2AuthenticationToken token) {
        if (OAuth2UserAuthenticationService.isGithubAuthenticationToken(token)) {
            long             githubId = GithubOAuth2User.getGithubId(token.getPrincipal());
            GithubOAuth2User user     = githubOAuth2UserRepository.findByGithubId(githubId);
            if (user == null) {
                return null;
            }
            return userRepository.findByGithubOAuth2User(user);
        } else if (OAuth2UserAuthenticationService.isGoogleAuthenticationToken(token)) {
            String           googleId = GoogleOAuth2User.getGoogleId(token.getPrincipal());
            GoogleOAuth2User user     = googleOAuth2UserRepository.findByGoogleId(googleId);
            if (user == null) {
                return null;
            }
            return userRepository.findByGoogleOAuth2User(user);
        } else {
            return null;
        }
    }

    public static boolean isGoogleAuthenticationToken(OAuth2AuthenticationToken token) {
        return OAuth2UserAuthenticationService.isGoogleAuthenticationUser(token.getPrincipal());
    }

    public static boolean isGoogleAuthenticationUser(OAuth2User principal) {
        return OAuth2UserAuthenticationService.genericAuthenticationUserHostMatches(principal, "google");
    }

    public static boolean isGithubAuthenticationToken(OAuth2AuthenticationToken token) {
        return OAuth2UserAuthenticationService.isGithubAuthenticationUser(token.getPrincipal());
    }

    public static boolean isGithubAuthenticationUser(OAuth2User principal) {
        return OAuth2UserAuthenticationService.genericAuthenticationUserHostMatches(principal, "github");
    }

    private static boolean genericAuthenticationUserHostMatches(OAuth2User principal, String hostToMatch) {
        try {
            URL url = OAuth2UserAuthenticationService.getURL(principal);
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
