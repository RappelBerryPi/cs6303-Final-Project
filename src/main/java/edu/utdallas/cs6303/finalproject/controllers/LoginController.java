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

import edu.utdallas.cs6303.finalproject.model.database.GithubOAuth2User;
import edu.utdallas.cs6303.finalproject.model.database.GoogleOAuth2User;
import edu.utdallas.cs6303.finalproject.model.database.repositories.GithubOAuth2UserRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.GoogleOAuth2UserRepository;
import edu.utdallas.cs6303.finalproject.model.validation.ForgotPasswordForm;
import edu.utdallas.cs6303.finalproject.services.oauth.OAuth2UserAuthenticationService;

@Controller
@RequestMapping(LoginController.REQUEST_MAPPING)
public class LoginController {

    @Autowired
    private OAuth2UserAuthenticationService oAuth2UserAuthenticationService;

    public static final String REQUEST_MAPPING = "/login";

    @GetMapping({ "", "/" })
    public String index() {
        return "login";
    }

    @GetMapping("/loginSuccess")
    public String getLoginInfo(@AuthenticationPrincipal OAuth2User principal) throws NotSupportedException, MalformedURLException {
        oAuth2UserAuthenticationService.createUserAndLinkToOAuth2(principal);
        return "redirect:/login";
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
