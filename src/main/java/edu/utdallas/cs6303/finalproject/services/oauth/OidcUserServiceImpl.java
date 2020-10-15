package edu.utdallas.cs6303.finalproject.services.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class OidcUserServiceImpl extends OidcUserService {

    @Autowired
    private OidcUserAuthenticationService oidc2UserAuthenticationService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        try {
            oidcUser = oidc2UserAuthenticationService.correctOidcUserAuthorities(oidcUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oidcUser;
    }


    
}