package edu.utdallas.cs6303.finalproject.main;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private static final long serialVersionUID = -7812235407277418515L;

    private String            verificationCode;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.verificationCode = request.getParameter("code");
    }

    public String getVerificationCode() {
        return this.verificationCode;
    }

    @Override
    public int hashCode() {
        final int prime  = 31;
        int       result = super.hashCode();
        result = prime * result + ((verificationCode == null) ? 0 : verificationCode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        CustomWebAuthenticationDetails other = (CustomWebAuthenticationDetails) obj;
        if (verificationCode == null) {
            if (other.verificationCode != null) return false;
        } else if (!verificationCode.equals(other.verificationCode)) return false;
        return true;
    }

}
