package edu.utdallas.cs6303.finalproject.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.utdallas.cs6303.finalproject.model.device.DeviceInfo;
import edu.utdallas.cs6303.finalproject.services.image.ImageFileURLBuilderInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.SmartView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class DeviceImageInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    ImageFileURLBuilderInterface imageFileURLBuilder;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && !isRedirectView(modelAndView)) {
            String userAgent = request.getHeader("user-agent");
            DeviceInfo deviceInfo = new DeviceInfo(userAgent);
            modelAndView.addObject("deviceInfo",deviceInfo);
            modelAndView.addObject("imageURLBuilder",imageFileURLBuilder);
        }
    }

    private boolean isRedirectView(ModelAndView modelAndView) {
        String viewName = modelAndView.getViewName();
        if (viewName != null && viewName.startsWith("redirect:/"))  {
            return true;
        }

        View view = modelAndView.getView();
        return (view != null && view instanceof SmartView && ((SmartView) view).isRedirectView());
    }

}