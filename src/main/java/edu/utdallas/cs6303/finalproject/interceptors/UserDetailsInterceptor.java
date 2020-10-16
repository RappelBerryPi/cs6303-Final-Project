package edu.utdallas.cs6303.finalproject.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.utdallas.cs6303.finalproject.model.database.User;
import edu.utdallas.cs6303.finalproject.model.database.repositories.UserRepository;
import edu.utdallas.cs6303.finalproject.services.oauth.OidcUserAuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.SmartView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class UserDetailsInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OidcUserAuthenticationService oAuth2UserAuthenticationService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && !isRedirectView(modelAndView)) {
            User user = null;
            try {
                SecurityContextImpl contextImpl = (SecurityContextImpl) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
                if (contextImpl.getAuthentication() instanceof OAuth2AuthenticationToken) {
                    OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) contextImpl.getAuthentication();
                    user = oAuth2UserAuthenticationService.getUserFromOAuth2AuthenticationToken(token);
                } else {
                    String userName = contextImpl.getAuthentication().getName();
                    if (userName != null) {
                        user = userRepository.findByUsername(userName);
                    }
                }
            } catch (Exception e) {
                // todo: nothing
            }
            if (user == null) {
                user = new User();
                user.setFirstName("");
            }
            modelAndView.addObject("currentUser", user);
        }
    }

    private boolean isRedirectView(ModelAndView modelAndView) {
        String viewName = modelAndView.getViewName();
        if (viewName != null && viewName.startsWith("redirect:/")) {
            return true;
        }

        View view = modelAndView.getView();
        return (view != null && view instanceof SmartView && ((SmartView) view).isRedirectView());
    }

}