package edu.utdallas.cs6303.finalproject.model.database;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String  username;
    private String  firstName;
    private String  middleName;
    private String  lastName;
    private String  email;
    private String  password;
    private boolean active;
    private boolean locked;

    @OneToOne
    private GithubOidcUser githubOidcUser;

    @OneToOne
    private GoogleOidcUser googleOidcUser;

    @OneToOne(mappedBy = "user")
    private Cart cart;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "UsersRoles", joinColumns = @JoinColumn(name = "UserID", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "RoleID", referencedColumnName = "id"))
    private Collection<Role> roles;

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return this.firstName + " " + this.middleName + " " + this.lastName;
    }

    public String getFirstAndLastName() {
        return this.firstName + " " + this.lastName;
    }

    public String getDisplayName() {
        return this.lastName + ", " + this.firstName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setRoles(final Collection<Role> roles) {
        this.roles = roles;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public boolean isEnabled() {
        return this.isActive();
    }

    public void Enable() {
        this.setActive();
    }

    public void Disable() {
        this.setInactive();
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive() {
        this.active = true;
    }

    public void setInactive() {
        this.active = false;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public boolean isNotLocked() {
        return !this.locked;
    }

    public void lockAccount() {
        this.locked = true;
    }

    public void unlockAccount() {
        this.locked = false;
    }

    public static final String USER_NAME_REGEX_LOOSE              = "^[A-Za-z0-9_]+$";
    public static final String USER_NAME_NOT_MATCH_MESSAGE        = "Please enter a valid Username.";
    public static final String USER_NAME_REGEX_TIGHT              = "^[A-Za-z][A-Za-z0-9_]{7,59}$";
    public static final String USER_NAME_REGEX_TIGHT_HTML_PATTERN = USER_NAME_REGEX_TIGHT.substring(1, USER_NAME_REGEX_TIGHT.length() - 1);

    public GithubOidcUser getGithubOidcUser() {
        return githubOidcUser;
    }

    public void setGithubOidcUser(GithubOidcUser githubOidcUser) {
        this.githubOidcUser = githubOidcUser;
    }

    public GoogleOidcUser getGoogleOidcUser() {
        return googleOidcUser;
    }

    public void setGoogleOidcUser(GoogleOidcUser googleOidcUser) {
        this.googleOidcUser = googleOidcUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> returnList =
                this.getRoles().stream().map(Role::getPrivileges).flatMap(Collection::stream).map(p -> new SimpleGrantedAuthority(p.getName())).collect(Collectors.toList());
        returnList.addAll(this.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList()));
        return returnList;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isNotLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isAccountNonExpired();
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}