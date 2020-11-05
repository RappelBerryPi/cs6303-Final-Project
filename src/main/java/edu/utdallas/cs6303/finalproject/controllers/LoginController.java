package edu.utdallas.cs6303.finalproject.controllers;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.NotSupportedException;
import javax.validation.Valid;

import com.google.zxing.WriterException;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.utdallas.cs6303.finalproject.model.database.User;
import edu.utdallas.cs6303.finalproject.model.database.repositories.UserRepository;
import edu.utdallas.cs6303.finalproject.model.validation.CreateUserForm;
import edu.utdallas.cs6303.finalproject.model.validation.ForgotPasswordForm;
import edu.utdallas.cs6303.finalproject.services.oauth.OidcUserAuthenticationService;
import edu.utdallas.cs6303.finalproject.services.user.UserServiceInterface;

@Controller
@RequestMapping(LoginController.REQUEST_MAPPING)
public class LoginController {

    // TODO: create a safehtml class to validate safehtml and especially for form
    // inputs such as LOB objects.

    @Autowired
    private OidcUserAuthenticationService oidcUserAuthenticationService;

    @Autowired
    private UserServiceInterface userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public static final String REQUEST_MAPPING = "/login";

    @GetMapping({ "", "/" })
    public String index() {
        return "login";
    }

    @GetMapping("/loginSuccess")
    public String getLoginInfo(@AuthenticationPrincipal OidcUser principal) throws NotSupportedException, MalformedURLException {
        oidcUserAuthenticationService.createUserAndLinkToOidc(principal);
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
    public String createUserStep2(@Valid CreateUserForm createUserForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest request) throws WriterException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult." + CREATE_USER_FORM_ATTRIBUTE_NAME, bindingResult);
            redirectAttributes.addFlashAttribute(CREATE_USER_FORM_ATTRIBUTE_NAME, createUserForm);
            return HomeController.REDIRECT_TO + LoginController.REQUEST_MAPPING + "/createUser";
        }
        User                                user            = userService.createUserFromUserForm(createUserForm);
        UsernamePasswordAuthenticationToken authReq         = new UsernamePasswordAuthenticationToken(user.getUsername(), createUserForm.getPassword());
        Authentication                      auth            = authenticationManager.authenticate(authReq);
        SecurityContext                     securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        redirectAttributes.addFlashAttribute("showQRCode", true);
        return HomeController.REDIRECT_TO + REQUEST_MAPPING;
    }

    @GetMapping("/qrCode/")
    @ResponseBody
    public ResponseEntity<Resource> getQrCode(Authentication authentication) throws WriterException, IOException {
        return userService.getQRCode(authentication);
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
            return HomeController.REDIRECT_TO + LoginController.REQUEST_MAPPING + "/forgotPassword";
        }
        LoggerFactory.getLogger(HomeController.class).info(forgotPasswordForm.getEmailAddress());
        LoggerFactory.getLogger(HomeController.class).info(forgotPasswordForm.getUserName());
        return HomeController.REDIRECT_TO + LoginController.REQUEST_MAPPING + "/forgotPassword/2";
    }

    @PostMapping("/checkEmail")
    public @ResponseBody boolean checkEmail(@RequestParam("email") String email) {
        return email != null && userRepository.findByEmail(email) == null;
    }

    @PostMapping("/checkUserName")
    public @ResponseBody boolean checkUserName(@RequestParam("userName") String userName) {
        return userName != null && userRepository.findByUsername(userName) == null;
    }

}
