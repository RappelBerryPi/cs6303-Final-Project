package edu.utdallas.cs6303.finalproject.model.database;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
public class GithubOidcUser implements OidcUser {

    @Id
    private long id;

    @Transient
    private Map<String, Object> attributes;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    private String        name;
    private String        login;
    @Column(unique = true)
    private long          githubId;
    private String        nodeId;
    private String        avatarUrl;
    private String        gravatarId;
    private String        url;
    private String        htmlUrl;
    private String        followersUrl;
    private String        followingUrl;
    private String        gistsUrl;
    private String        starredUrl;
    private String        subscriptionsUrl;
    private String        organizationsUrl;
    private String        reposUrl;
    private String        eventsUrl;
    private String        receivedEventsUrl;
    private String        type;
    private boolean       siteAdmin;
    private String        company;
    private String        blog;
    private String        location;
    private String        email;
    private boolean       hireable;
    private String        bio;
    private String        twitterUsername;
    private int           publicRepos;
    private int           publicGists;
    private int           followers;
    private int           following;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int           privateGists;
    private int           totalPrivateRepos;
    private int           ownedPrivateRepos;
    private int           diskUsage;
    private int           collaborators;
    private boolean       twoFactorAuthentication;
    // TODO: determine if necessary??
    @Transient
    private Map<String, Object> plan;
    @Transient
    private Map<String, Object> claims;
    @Transient
    public OidcUserInfo         userInfo;
    @Transient
    public OidcIdToken          idToken;

    public GithubOidcUser() {
        this.attributes  = new HashMap<>();
        this.authorities = new ArrayList<>();
        this.name        = "";
    }

    public long getGithubID() {
        return githubId;
    }

    public void setGithubID(long githubID) {
        this.githubId = githubID;
    }

    private static Object tryGetAttributeValue(OidcUser oidcUser, String attributeName) {
        try {
            return oidcUser.getAttribute(attributeName);
        } catch (Exception e) {
            return null;
        }
    }

    private static String tryGetStringAttributeValue(OidcUser oidcUser, String attributeName) {
        return (String) tryGetAttributeValue(oidcUser, attributeName);
    }

    private static int tryGetIntAttributeValue(OidcUser oidcUser, String attributeName) {
        return (int) tryGetAttributeValue(oidcUser, attributeName);
    }

    private static boolean tryGetBooleanAttributeValue(OidcUser oidcUser, String attributeName) {
        Object val = tryGetAttributeValue(oidcUser, attributeName);
        if (val == null) {
            return false;
        } else {
            return (boolean) val;
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> tryGetStringObjectMapAttributeValue(OidcUser oidcUser, String attributeName) {
        Object val = tryGetAttributeValue(oidcUser, attributeName);
        if (val == null) {
            return null;
        } else {
            if (val instanceof Map) {
                return ((Map<String, Object>) val).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            } else {
                return null;
            }
        }
    }

    public GithubOidcUser(OidcUser oidcUser) {
        this.attributes              = oidcUser.getAttributes();
        this.authorities             = oidcUser.getAuthorities();
        this.name                    = tryGetStringAttributeValue(oidcUser, "name");
        this.login                   = tryGetStringAttributeValue(oidcUser, "login");
        this.githubId                = tryGetIntAttributeValue(oidcUser, "id");
        this.nodeId                  = tryGetStringAttributeValue(oidcUser, "node_id");
        this.avatarUrl               = tryGetStringAttributeValue(oidcUser, "avatar_url");
        this.gravatarId              = tryGetStringAttributeValue(oidcUser, "gravatar_id");
        this.url                     = tryGetStringAttributeValue(oidcUser, "url");
        this.htmlUrl                 = tryGetStringAttributeValue(oidcUser, "html_url");
        this.followersUrl            = tryGetStringAttributeValue(oidcUser, "followers_url");
        this.followingUrl            = tryGetStringAttributeValue(oidcUser, "following_url");
        this.gistsUrl                = tryGetStringAttributeValue(oidcUser, "gists_url");
        this.starredUrl              = tryGetStringAttributeValue(oidcUser, "starred_url");
        this.subscriptionsUrl        = tryGetStringAttributeValue(oidcUser, "subscriptions_url");
        this.organizationsUrl        = tryGetStringAttributeValue(oidcUser, "organizations_url");
        this.reposUrl                = tryGetStringAttributeValue(oidcUser, "repos_url");
        this.eventsUrl               = tryGetStringAttributeValue(oidcUser, "events_url");
        this.receivedEventsUrl       = tryGetStringAttributeValue(oidcUser, "received_events_url");
        this.type                    = tryGetStringAttributeValue(oidcUser, "type");
        this.siteAdmin               = tryGetBooleanAttributeValue(oidcUser, "site_admin");
        this.company                 = tryGetStringAttributeValue(oidcUser, "company");
        this.blog                    = tryGetStringAttributeValue(oidcUser, "blog");
        this.location                = tryGetStringAttributeValue(oidcUser, "location");
        this.email                   = tryGetStringAttributeValue(oidcUser, "email");
        this.hireable                = tryGetBooleanAttributeValue(oidcUser, "hireable");
        this.bio                     = tryGetStringAttributeValue(oidcUser, "bio");
        this.twitterUsername         = tryGetStringAttributeValue(oidcUser, "twitter_username");
        this.publicRepos             = tryGetIntAttributeValue(oidcUser, "public_repos");
        this.publicGists             = tryGetIntAttributeValue(oidcUser, "public_gists");
        this.followers               = tryGetIntAttributeValue(oidcUser, "followers");
        this.following               = tryGetIntAttributeValue(oidcUser, "following");
        this.createdAt               = LocalDateTime.ofInstant(Instant.parse(tryGetStringAttributeValue(oidcUser, "created_at")), ZoneId.systemDefault());
        this.updatedAt               = LocalDateTime.ofInstant(Instant.parse(tryGetStringAttributeValue(oidcUser, "updated_at")), ZoneId.systemDefault());
        this.privateGists            = tryGetIntAttributeValue(oidcUser, "private_gists");
        this.totalPrivateRepos       = tryGetIntAttributeValue(oidcUser, "total_private_repos");
        this.ownedPrivateRepos       = tryGetIntAttributeValue(oidcUser, "owned_private_repos");
        this.diskUsage               = tryGetIntAttributeValue(oidcUser, "disk_usage");
        this.collaborators           = tryGetIntAttributeValue(oidcUser, "collaborators");
        this.twoFactorAuthentication = tryGetBooleanAttributeValue(oidcUser, "two_factor_authentication");
        this.plan                    = tryGetStringObjectMapAttributeValue(oidcUser, "plan");
        this.claims                  = oidcUser.getClaims();
        this.userInfo                = oidcUser.getUserInfo();
        this.idToken                 = oidcUser.getIdToken();
    }

    public GithubOidcUser(OidcUser oidcUser, User user) {
        this(oidcUser);
        this.authorities = user.getAuthorities();
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getGravatarId() {
        return gravatarId;
    }

    public void setGravatarId(String gravatarId) {
        this.gravatarId = gravatarId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getFollowersUrl() {
        return followersUrl;
    }

    public void setFollowersUrl(String followersUrl) {
        this.followersUrl = followersUrl;
    }

    public String getFollowingUrl() {
        return followingUrl;
    }

    public void setFollowingUrl(String followingUrl) {
        this.followingUrl = followingUrl;
    }

    public String getGistsUrl() {
        return gistsUrl;
    }

    public void setGistsUrl(String gistsUrl) {
        this.gistsUrl = gistsUrl;
    }

    public String getStarredUrl() {
        return starredUrl;
    }

    public void setStarredUrl(String starredUrl) {
        this.starredUrl = starredUrl;
    }

    public String getSubscriptionsUrl() {
        return subscriptionsUrl;
    }

    public void setSubscriptionsUrl(String subscriptionsUrl) {
        this.subscriptionsUrl = subscriptionsUrl;
    }

    public String getOrganizationsUrl() {
        return organizationsUrl;
    }

    public void setOrganizationsUrl(String organizationsUrl) {
        this.organizationsUrl = organizationsUrl;
    }

    public String getReposUrl() {
        return reposUrl;
    }

    public void setReposUrl(String reposUrl) {
        this.reposUrl = reposUrl;
    }

    public String getEventsUrl() {
        return eventsUrl;
    }

    public void setEventsUrl(String eventsUrl) {
        this.eventsUrl = eventsUrl;
    }

    public String getReceivedEventsUrl() {
        return receivedEventsUrl;
    }

    public void setReceivedEventsUrl(String receivedEventsUrl) {
        this.receivedEventsUrl = receivedEventsUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSiteAdmin() {
        return siteAdmin;
    }

    public void setSiteAdmin(boolean siteAdmin) {
        this.siteAdmin = siteAdmin;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isHireable() {
        return hireable;
    }

    public void setHireable(boolean hireable) {
        this.hireable = hireable;
    }

    public String getTwitterUsername() {
        return twitterUsername;
    }

    public void setTwitterUsername(String twitterUsername) {
        this.twitterUsername = twitterUsername;
    }

    public int getPublicRepos() {
        return publicRepos;
    }

    public void setPublicRepos(int publicRepos) {
        this.publicRepos = publicRepos;
    }

    public int getPublicGists() {
        return publicGists;
    }

    public void setPublicGists(int publicGists) {
        this.publicGists = publicGists;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Instant getUpdatedAt() {
        return updatedAt.toInstant(ZoneOffset.UTC);
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getPrivateGists() {
        return privateGists;
    }

    public void setPrivateGists(int privateGists) {
        this.privateGists = privateGists;
    }

    public int getTotalPrivateRepos() {
        return totalPrivateRepos;
    }

    public void setTotalPrivateRepos(int totalPrivateRepos) {
        this.totalPrivateRepos = totalPrivateRepos;
    }

    public int getOwnedPrivateRepos() {
        return ownedPrivateRepos;
    }

    public void setOwnedPrivateRepos(int ownedPrivateRepos) {
        this.ownedPrivateRepos = ownedPrivateRepos;
    }

    public int getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(int diskUsage) {
        this.diskUsage = diskUsage;
    }

    public int getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(int collaborators) {
        this.collaborators = collaborators;
    }

    public boolean isTwoFactorAuthentication() {
        return twoFactorAuthentication;
    }

    public void setTwoFactorAuthentication(boolean twoFactorAuthentication) {
        this.twoFactorAuthentication = twoFactorAuthentication;
    }

    public Map<String, Object> getPlan() {
        return plan;
    }

    public void setPlan(Map<String, Object> plan) {
        this.plan = plan;
    }

    public static long getGithubId(OAuth2User principal) {
        return ((Number) principal.getAttribute("id")).longValue();
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
