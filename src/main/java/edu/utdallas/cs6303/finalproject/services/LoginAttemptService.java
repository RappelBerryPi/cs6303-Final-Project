package edu.utdallas.cs6303.finalproject.services;

import java.util.HashMap;

import javax.naming.AuthenticationNotSupportedException;

import edu.utdallas.cs6303.finalproject.model.database.LoginAttemptLog;
import edu.utdallas.cs6303.finalproject.model.database.User;
import edu.utdallas.cs6303.finalproject.model.database.repositories.UserRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.LoginAttemptLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginAttemptLogRepository loginAttemptLogRepository;

    private static final int         MAX_ATTEMPTS = 10;
    private HashMap<String, Integer> attemptsCache;

    public LoginAttemptService() {
        attemptsCache = new HashMap<>();
    }

    public void loginSucceeded(Authentication auth, String userAgent) throws AuthenticationNotSupportedException {
        this.logEntry(auth, userAgent, true);
        int loginAttempts = attemptsCache.getOrDefault(auth.getName(), 0);
        if (loginAttempts < MAX_ATTEMPTS) {
            attemptsCache.remove(auth.getName());
        } else {
            throw new AuthenticationNotSupportedException("This account has been locked dut to a number of Authentication failures.");
        }
    }

    public void loginFailed(Authentication auth, String userAgent) {
        this.logEntry(auth, userAgent, false);
        String name          = auth.getName();
        int    loginAttempts = attemptsCache.getOrDefault(name, 0);
        loginAttempts++;
        if (loginAttempts >= MAX_ATTEMPTS) {
            User user = userRepository.findByUsername(name);
            if (user != null) {
                user.lockAccount();
                userRepository.save(user);
            }
        }
        attemptsCache.put(name, loginAttempts);
    }

    public boolean isBlocked(String key) {
        return attemptsCache.getOrDefault(key, 0) >= MAX_ATTEMPTS;
    }

    private void logEntry(Authentication auth, String userAgent, boolean success) {
        WebAuthenticationDetails details   = (WebAuthenticationDetails) auth.getDetails();
        String                   ipAddress = details.getRemoteAddress();
        LoginAttemptLog          log       = new LoginAttemptLog();
        log.setIpAddress(ipAddress);
        log.setSuccess(success);
        log.setUserAgent(userAgent);
        log.setUserName(auth.getName());
        loginAttemptLogRepository.save(log);
    }

}