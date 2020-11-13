package edu.utdallas.cs6303.finalproject.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.SmartView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import edu.utdallas.cs6303.finalproject.model.database.User;
import edu.utdallas.cs6303.finalproject.services.user.UserServiceInterface;

@Component
public class UserDetailsInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    UserServiceInterface userService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && !isRedirectView(modelAndView)) {
            User user = null;
            try {
                SecurityContextImpl contextImpl = (SecurityContextImpl) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
                user = userService.getUserFromAuthentication(contextImpl.getAuthentication());
            } catch (Exception e) {
                //nothing to do, contextImpl is probably null
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