package edu.utdallas.cs6303.finalproject.controllers;

import java.net.MalformedURLException;

import javax.transaction.NotSupportedException;
import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.utdallas.cs6303.finalproject.model.database.repositories.UserRepository;
import edu.utdallas.cs6303.finalproject.model.validation.CreateUserForm;
import edu.utdallas.cs6303.finalproject.model.validation.ForgotPasswordForm;
import edu.utdallas.cs6303.finalproject.services.oauth.OAuth2UserAuthenticationService;

@Controller
@RequestMapping(LoginController.REQUEST_MAPPING)
public class LoginController {

    @Autowired
    private OAuth2UserAuthenticationService oAuth2UserAuthenticationService;

    @Autowired
    private UserRepository userRepository;

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

    private static final String CREATE_USER_FORM_ATTRIBUTE_NAME = "createUserForm";

    @GetMapping("/createUser")
    public String createUser(Model model) {
        if (!model.containsAttribute(CREATE_USER_FORM_ATTRIBUTE_NAME)) {
            model.addAttribute(CREATE_USER_FORM_ATTRIBUTE_NAME, new CreateUserForm());
        }
        return "createUser";
    }

    @PostMapping("/createUser")
    public String createUserStep2(@Valid CreateUserForm createUserForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        //TODO: Finish creating user (and maybe logging them in??)
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult." + CREATE_USER_FORM_ATTRIBUTE_NAME, bindingResult);
            redirectAttributes.addFlashAttribute(CREATE_USER_FORM_ATTRIBUTE_NAME, createUserForm);
            return "redirect:" + LoginController.REQUEST_MAPPING + "/createUser";
        }
        LoggerFactory.getLogger(LoginController.class).info(createUserForm.getUserName());
        LoggerFactory.getLogger(LoginController.class).info(createUserForm.getEmail());
        LoggerFactory.getLogger(LoginController.class).info(createUserForm.getPassword());
        LoggerFactory.getLogger(LoginController.class).info(createUserForm.getConfirmationPassword());
        return "login";
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
        // TODO: start next step. log this for real? do something else???
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult." + FORGOT_PASSWORD_FORM_ATTRIBUTE_NAME, bindingResult);
            redirectAttributes.addFlashAttribute(FORGOT_PASSWORD_FORM_ATTRIBUTE_NAME, forgotPasswordForm);
            return "redirect:" + LoginController.REQUEST_MAPPING + "/forgotPassword";
        }
        LoggerFactory.getLogger(HomeController.class).info(forgotPasswordForm.getEmailAddress());
        LoggerFactory.getLogger(HomeController.class).info(forgotPasswordForm.getUserName());
        return "redirect:" + LoginController.REQUEST_MAPPING + "/forgotPassword/2";
    }

    @PostMapping("/checkEmail")
    public @ResponseBody boolean checkEmail(@RequestParam("email") String email) {
        return email != null && userRepository.findByEmail(email) == null;
    }

    @PostMapping("/checkUserName")
    public @ResponseBody boolean checkUserName(@RequestParam("userName") String userName) {
        return userName != null && userRepository.findByUserName(userName) == null;
    }

}
