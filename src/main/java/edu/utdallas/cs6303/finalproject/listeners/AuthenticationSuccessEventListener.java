package edu.utdallas.cs6303.finalproject.listeners;

import javax.naming.AuthenticationNotSupportedException;
import javax.servlet.http.HttpServletRequest;

import edu.utdallas.cs6303.finalproject.services.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private HttpServletRequest request;

    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        try {
            String userAgent = request.getHeader("user-agent");
            loginAttemptService.loginSucceeded(event.getAuthentication(), userAgent);
        } catch (AuthenticationNotSupportedException e) {
            //Nothing to do in this case
        }
    }
}