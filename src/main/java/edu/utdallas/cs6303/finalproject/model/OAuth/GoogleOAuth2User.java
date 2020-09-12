package edu.utdallas.cs6303.finalproject.model.OAuth;

import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class GoogleOAuth2User implements OAuth2User {

    private Map<String, Object> attributes;

    private Collection<? extends GrantedAuthority> authorities;

    private String    name;
    private URL       assertionIssuer; //iss
    private String    givenName;
    private String    locale;
    private String    nonce; //to help protect against replay attacks
    private String    profilePicture;
    private String    serverClientId; //aud
    private String    clientID; //azp
    private Instant   assertionCreationTime; //iat
    private Instant   assertionExpirationTime; //exp
    private String    familyName;
    private String    email;

    public GoogleOAuth2User() {
        this.attributes  = new HashMap<String, Object>();
        this.authorities = new ArrayList<SimpleGrantedAuthority>();
        this.name        = "";
    }

    public GoogleOAuth2User(OAuth2User oAuth2User) {
        this.attributes  = oAuth2User.getAttributes();
        this.authorities = oAuth2User.getAuthorities();
        this.name        = oAuth2User.getAttribute("name");
        this.
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getServerClientId() {
        return serverClientId;
    }

    public void setServerClientId(String serverClientId) {
        this.serverClientId = serverClientId;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public URL getAssertionIssuer() {
        return assertionIssuer;
    }

    public void setAssertionIssuer(URL assertionIssuer) {
        this.assertionIssuer = assertionIssuer;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public Instant getAssertionCreationTime() {
        return assertionCreationTime;
    }

    public void setAssertionCreationTime(Instant assertionCreationTime) {
        this.assertionCreationTime = assertionCreationTime;
    }

    public Instant getAssertionExpirationTime() {
        return assertionExpirationTime;
    }

    public void setAssertionExpirationTime(Instant assertionExpirationTime) {
        this.assertionExpirationTime = assertionExpirationTime;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

}
