package edu.utdallas.cs6303.finalproject.model.database;

import java.net.URL;
import java.time.Instant;
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
import org.springframework.security.oauth2.core.user.OAuth2User;

@Entity
public class GoogleOAuth2User implements OAuth2User {

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
    // TODO: fix
    @Transient
    private List<String>  serverClientId;          // aud
    private String        clientID;                // azp
    private LocalDateTime assertionCreationTime;   // iat
    private LocalDateTime assertionExpirationTime; // exp
    private String        familyName;
    private String        email;

    @Column(unique = true, length = 255)
    private String googleId; // sub

    public GoogleOAuth2User() {
        this.attributes  = new HashMap<>();
        this.authorities = new ArrayList<>();
        this.name        = "";
    }

    public GoogleOAuth2User(OAuth2User oAuth2User) {
        this.attributes              = oAuth2User.getAttributes();
        this.authorities             = oAuth2User.getAuthorities();
        this.name                    = oAuth2User.getAttribute("name");
        this.assertionIssuer         = oAuth2User.getAttribute("iss");
        this.givenName               = oAuth2User.getAttribute("givenName");
        this.locale                  = oAuth2User.getAttribute("nonce");
        this.profilePicture          = oAuth2User.getAttribute("profilePicture");
        this.serverClientId          = oAuth2User.getAttribute("aud");
        this.clientID                = oAuth2User.getAttribute("azp");
        this.assertionCreationTime   = LocalDateTime.ofInstant(oAuth2User.getAttribute("iat"), ZoneId.systemDefault());
        this.assertionExpirationTime = LocalDateTime.ofInstant(oAuth2User.getAttribute("exp"), ZoneId.systemDefault());
        this.email                   = oAuth2User.getAttribute("email");
        this.googleId                = oAuth2User.getAttribute("sub");
        if (this.givenName == null || this.familyName == null) {
            String[] nameSplit = this.name.split("\\s+");
            this.givenName = nameSplit[0];
            this.familyName = nameSplit[nameSplit.length - 1];
        }
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
}
