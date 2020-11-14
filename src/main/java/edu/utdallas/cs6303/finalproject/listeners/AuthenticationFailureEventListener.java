package edu.utdallas.cs6303.finalproject.listeners;

import javax.servlet.http.HttpServletRequest;

import edu.utdallas.cs6303.finalproject.services.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureEventListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private HttpServletRequest request;

    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String userAgent = request.getHeader("user-agent");
        loginAttemptService.loginFailed(event.getAuthentication(), userAgent);
    }

}