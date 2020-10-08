package edu.utdallas.cs6303.finalproject.model.validation;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.stereotype.Component;

import edu.utdallas.cs6303.finalproject.model.database.User;
import edu.utdallas.cs6303.finalproject.model.validation.email.UniqueEmail;
import edu.utdallas.cs6303.finalproject.model.validation.password.ValidPassword;
import edu.utdallas.cs6303.finalproject.model.validation.username.UniqueUserName;

@Component
public class CreateUserForm {

    @NotNull
    @NotBlank(message = "email must not be blank")
    @Email
    @UniqueEmail
    protected String email;

    @NotNull
    @NotBlank(message = "User Name must not be blank")
    @Pattern(regexp = User.USER_NAME_REGEX_TIGHT, message = User.USER_NAME_NOT_MATCH_MESSAGE)
    @UniqueUserName
    protected String userName;

    protected String firstName;

    protected String lastName;

    @NotNull
    @NotBlank(message = "Please enter a password.")
    @ValidPassword
    protected String password;

    @NotNull
    @NotBlank(message = "Password confirmation blank")
    @ValidPassword
    protected String confirmationPassword;

    @AssertTrue
    private boolean isMatching() {
        return confirmationPassword.equals(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmationPassword() {
        return confirmationPassword;
    }

    public void setConfirmationPassword(String confirmationPassword) {
        this.confirmationPassword = confirmationPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


}