package edu.utdallas.cs6303.finalproject.controllers;

import java.net.MalformedURLException;
import java.net.URL;

import javax.transaction.NotSupportedException;
import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.utdallas.cs6303.finalproject.model.oauth.GithubOAuth2User;
import edu.utdallas.cs6303.finalproject.model.oauth.GoogleOAuth2User;
import edu.utdallas.cs6303.finalproject.model.validation.ForgotPasswordForm;

@Controller
@RequestMapping(LoginController.REQUEST_MAPPING)
public class LoginController {

    public static final String REQUEST_MAPPING = "/login";

    @GetMapping({ "", "/" })
    public String index() {
        return "login";
    }

    @GetMapping("/loginSuccess")
    public String getLoginInfo(@AuthenticationPrincipal OAuth2User principal) throws NotSupportedException, MalformedURLException {
        URL url = principal.getAttribute("iss");
        if (url == null) {
            String strURL = principal.getAttribute("url");
            if (strURL == null) {
                throw new NotSupportedException("unable to get details from your provider. Please contact support.");
            }
            url = new URL(strURL);
        }
        if (url.getHost().contains("google")) {
            GoogleOAuth2User gPrincipal = new GoogleOAuth2User(principal);
            LoggerFactory.getLogger(LoginController.class).info(gPrincipal.getName());
        } else if (url.getHost().contains("github")) {
            GithubOAuth2User gPrincipal = new GithubOAuth2User(principal);
            LoggerFactory.getLogger(LoginController.class).info(gPrincipal.getName());
        }
        throw new NotSupportedException("Need to add users to the database and link to an actual profile.");
        //return "login";
    }

    @GetMapping("/createUser")
    public String createUser(Model model) {
        return "createUser";
    }

    private static final String FORGOT_PASSWORD_FORM_ATTRIBUTE_NAME = "forgotPasswordForm";

    @GetMapping("/forgotPassword")
    public String forgotPassword(Model model) {
        if (!model.containsAttribute(FORGOT_PASSWORD_FORM_ATTRIBUTE_NAME)) {
            model.addAttribute(FORGOT_PASSWORD_FORM_ATTRIBUTE_NAME, new ForgotPasswordForm());
        }
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String forgotPasswordStepTwo(@Valid ForgotPasswordForm forgotPasswordForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult." + FORGOT_PASSWORD_FORM_ATTRIBUTE_NAME, bindingResult);
            redirectAttributes.addFlashAttribute(FORGOT_PASSWORD_FORM_ATTRIBUTE_NAME, forgotPasswordForm);
            return "redirect:" + LoginController.REQUEST_MAPPING + "/forgotPassword";
        }
        LoggerFactory.getLogger(HomeController.class).info(forgotPasswordForm.getEmailAddress());
        LoggerFactory.getLogger(HomeController.class).info(forgotPasswordForm.getUserName());
        return "redirect:" + LoginController.REQUEST_MAPPING + "/forgotPassword/2";
    }

}
