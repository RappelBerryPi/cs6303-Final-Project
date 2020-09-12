package edu.utdallas.cs6303.finalproject.controllers;

import java.util.ArrayList;

import javax.print.DocFlavor.URL;
import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.utdallas.cs6303.finalproject.model.OAuth.GoogleOAuth2User;
import edu.utdallas.cs6303.finalproject.model.validation.ForgotPasswordForm;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping({ "", "/" })
    public String index() {
        return "login";
    }

    @GetMapping("/loginSuccess")
    public String getLoginInfo(@AuthenticationPrincipal OAuth2User principal) {
        try {
            java.net.URL url = principal.getAttribute("iss");
            if (url.getHost().contains("google")) {
                GoogleOAuth2User gPrincipal = new GoogleOAuth2User(principal);
            }
        } finally {

        }
        return "login";
        // OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
        // authentication.getAuthorizedClientRegistrationId(),
        // authentication.getName());
        // String userInfoEndpointUri =
        // client.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
        // if (!StringUtils.isEmpty(userInfoEndpointUri)) {
        // RestTemplate restTemplate = new RestTemplate();
        // HttpHeaders headers = new HttpHeaders();
        // headers.add(HttpHeaders.AUTHORIZATION, "Bearer " +
        // client.getAccessToken().getTokenValue());
        // HttpEntity entity = new HttpEntity("", headers);
        // ResponseEntity <Map>response = restTemplate.exchange(userInfoEndpointUri,
        // HttpMethod.GET, entity, Map.class);
        // Map userAttributes = response.getBody();
        // model.addAttribute("name", userAttributes.get("name"));
        // LoggerFactory.getLogger(HomeController.class).info(userAttributes.get("name"));

        // }
        // return "loginSuccess";
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
            return "redirect:/forgotPassword";
        }
        LoggerFactory.getLogger(HomeController.class).info(forgotPasswordForm.getEmailAddress());
        LoggerFactory.getLogger(HomeController.class).info(forgotPasswordForm.getUserName());
        return "redirect:/forgotPassword/2";
    }

}
