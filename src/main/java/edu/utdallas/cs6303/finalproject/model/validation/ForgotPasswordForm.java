package edu.utdallas.cs6303.finalproject.model.validation;

import org.springframework.stereotype.Component;

import edu.utdallas.cs6303.finalproject.model.database.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Component
public class ForgotPasswordForm {

    @NotNull(message = "Please enter an Email.")
    @NotBlank(message = "Please enter an Email.")
    @Email(message = "Please enter a valid Email.")
    private String emailAddress;

    @NotBlank(message = "Please enter a UserName")
    @NotNull(message = "PLease enter a UserName")
    @Pattern(regexp = User.USER_NAME_REGEX_TIGHT, message = User.USER_NAME_NOT_MATCH_MESSAGE)
    private String userName;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
}
