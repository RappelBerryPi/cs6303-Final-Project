package edu.utdallas.cs6303.finalproject.model.database;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String userName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String password;
    private boolean active;
    private boolean locked;

    @ManyToMany
    @JoinTable(
        name = "UsersRoles",
        joinColumns = @JoinColumn(
            name = "UserID", referencedColumnName = "id"
        ), inverseJoinColumns = @JoinColumn(
            name = "RoleID", referencedColumnName = "id"
        )
    )
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

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public static final String USER_NAME_REGEX_LOOSE = "^[A-Za-z0-9_]+$";
    public static final String USER_NAME_NOT_MATCH_MESSAGE = "Please enter a valid Username.";
    public static final String USER_NAME_REGEX_TIGHT = "^[A-Za-z][A-Za-z0-9_]{7,59}$";
}