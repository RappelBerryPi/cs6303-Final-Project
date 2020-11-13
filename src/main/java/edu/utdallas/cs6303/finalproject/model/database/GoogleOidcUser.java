package edu.utdallas.cs6303.finalproject.model.database;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Entity
public class GoogleOidcUser implements OidcUser {

    @Id
    private long                id;
    @Transient
    private Map<String, Object> attributes;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    private String name;
    private URL    assertionIssuer; // iss
    private String givenName;
    private String locale;
    private String nonce;           // to help protect against replay attacks
    private String profilePicture;
    @Transient
    private List<String>        serverClientId;          // aud
    private String              clientID;                // azp
    private LocalDateTime       assertionCreationTime;   // iat
    private LocalDateTime       assertionExpirationTime; // exp
    private String              familyName;
    private String              email;
    @Transient
    private Map<String, Object> claims;
    @Transient
    public OidcUserInfo         userInfo;
    @Transient
    public OidcIdToken          idToken;

    @Column(unique = true, length = 255)
    private String googleId; // sub

    public GoogleOidcUser() {
        this.attributes  = new HashMap<>();
        this.authorities = new ArrayList<>();
        this.name        = "";
    }

    public GoogleOidcUser(OidcUser oidcUser) {
        this.attributes              = oidcUser.getAttributes();
        this.authorities             = oidcUser.getAuthorities();
        this.name                    = oidcUser.getAttribute("name");
        this.assertionIssuer         = oidcUser.getAttribute("iss");
        this.givenName               = oidcUser.getAttribute("givenName");
        this.locale                  = oidcUser.getAttribute("nonce");
        this.profilePicture          = oidcUser.getAttribute("profilePicture");
        this.serverClientId          = oidcUser.getAttribute("aud");
        this.clientID                = oidcUser.getAttribute("azp");
        this.assertionCreationTime   = LocalDateTime.ofInstant(oidcUser.getAttribute("iat"), ZoneId.systemDefault());
        this.assertionExpirationTime = LocalDateTime.ofInstant(oidcUser.getAttribute("exp"), ZoneId.systemDefault());
        this.email                   = oidcUser.getAttribute("email");
        this.googleId                = oidcUser.getAttribute("sub");
        this.claims                  = oidcUser.getClaims();
        this.userInfo                = oidcUser.getUserInfo();
        this.idToken                 = oidcUser.getIdToken();
        if (this.givenName == null || this.familyName == null) {
            String[] nameSplit = this.name.split("\\s+");
            this.givenName  = nameSplit[0];
            this.familyName = nameSplit[nameSplit.length - 1];
        }
    }

    public GoogleOidcUser(OidcUser oidcUser, User user) {
        this(oidcUser);
        this.authorities = user.getAuthorities();
    }

    public long getId() {
        return id;
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

    @Override
    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
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

    public List<String> getServerClientId() {
        return serverClientId;
    }

    public void setServerClientId(List<String> serverClientId) {
        this.serverClientId = serverClientId;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    @Override
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

    @Override
    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public LocalDateTime getAssertionCreationTime() {
        return assertionCreationTime;
    }

    public void setAssertionCreationTime(LocalDateTime assertionCreationTime) {
        this.assertionCreationTime = assertionCreationTime;
    }

    public LocalDateTime getAssertionExpirationTime() {
        return assertionExpirationTime;
    }

    public void setAssertionExpirationTime(LocalDateTime assertionExpirationTime) {
        this.assertionExpirationTime = assertionExpirationTime;
    }

    @Override
    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public static String getGoogleId(OAuth2User principal) {
        return principal.getAttribute("sub");
    }

    @Override
    public Map<String, Object> getClaims() {
        return claims;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public OidcIdToken getIdToken() {
        return idToken;
    }
}
