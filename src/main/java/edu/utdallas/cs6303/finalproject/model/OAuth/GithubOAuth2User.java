package edu.utdallas.cs6303.finalproject.model.oauth;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class GithubOAuth2User implements OAuth2User {

    private Map<String, Object> attributes;

    private Collection<? extends GrantedAuthority> authorities;

    private String              name;
    private String              login;
    private int                 id;
    private String              nodeId;
    private String              avatarUrl;
    private String              gravatarId;
    private String              url;
    private String              htmlUrl;
    private String              followersUrl;
    private String              followingUrl;
    private String              gistsUrl;
    private String              starredUrl;
    private String              subscriptionsUrl;
    private String              organizationsUrl;
    private String              reposUrl;
    private String              eventsUrl;
    private String              receivedEventsUrl;
    private String              type;
    private boolean             siteAdmin;
    private String              company;
    private String              blog;
    private String              location;
    private String              email;
    private boolean             hireable;
    private String              bio;
    private String              twitterUsername;
    private int                 publicRepos;
    private int                 publicGists;
    private int                 followers;
    private int                 following;
    private Instant             createdAt;
    private Instant             updatedAt;
    private int                 privateGists;
    private int                 totalPrivateRepos;
    private int                 ownedPrivateRepos;
    private int                 diskUsage;
    private int                 collaborators;
    private boolean             twoFactorAuthentication;
    private Map<String, Object> plan;

    public GithubOAuth2User() {
        this.attributes  = new HashMap<>();
        this.authorities = new ArrayList<>();
        this.name        = "";
    }

    private static Object tryGetAttributeValue(OAuth2User oAuth2User, String attributeName) {
        try {
            return oAuth2User.getAttribute(attributeName);
        } catch (Exception e) {
            return null;
        }
    }

    private static String tryGetStringAttributeValue(OAuth2User oAuth2User, String attributeName) {
        return (String) tryGetAttributeValue(oAuth2User, attributeName);
    }

    private static int tryGetIntAttributeValue(OAuth2User oAuth2User, String attributeName) {
        return (int) tryGetAttributeValue(oAuth2User, attributeName);
    }

    private static boolean tryGetBooleanAttributeValue(OAuth2User oAuth2User, String attributeName) {
        Object val = tryGetAttributeValue(oAuth2User, attributeName);
        if (val == null) {
            return false;
        } else {
            return (boolean) val;
        }
    }

    public GithubOAuth2User(OAuth2User oAuth2User) {
        this.attributes              = oAuth2User.getAttributes();
        this.authorities             = oAuth2User.getAuthorities();
        this.name                    = tryGetStringAttributeValue(oAuth2User, "name");
        this.login                   = tryGetStringAttributeValue(oAuth2User, "login");
        this.id                      = tryGetIntAttributeValue(oAuth2User, "id");
        this.nodeId                  = tryGetStringAttributeValue(oAuth2User, "node_id");
        this.avatarUrl               = tryGetStringAttributeValue(oAuth2User, "avatar_url");
        this.gravatarId              = tryGetStringAttributeValue(oAuth2User, "gravatar_id");
        this.url                     = tryGetStringAttributeValue(oAuth2User, "url");
        this.htmlUrl                 = tryGetStringAttributeValue(oAuth2User, "html_url");
        this.followersUrl            = tryGetStringAttributeValue(oAuth2User,"followers_url");
        this.followingUrl            = tryGetStringAttributeValue(oAuth2User,"following_url");
        this.gistsUrl                = tryGetStringAttributeValue(oAuth2User,"gists_url");
        this.starredUrl              = tryGetStringAttributeValue(oAuth2User,"starred_url");
        this.subscriptionsUrl        = tryGetStringAttributeValue(oAuth2User,"subscriptions_url");
        this.organizationsUrl        = tryGetStringAttributeValue(oAuth2User,"organizations_url");
        this.reposUrl                = tryGetStringAttributeValue(oAuth2User,"repos_url");
        this.eventsUrl               = tryGetStringAttributeValue(oAuth2User,"events_url");
        this.receivedEventsUrl       = tryGetStringAttributeValue(oAuth2User,"received_events_url");
        this.type                    = tryGetStringAttributeValue(oAuth2User,"type");
        this.siteAdmin               = tryGetBooleanAttributeValue(oAuth2User,"site_admin");
        this.company                 = tryGetStringAttributeValue(oAuth2User,"company");
        this.blog                    = tryGetStringAttributeValue(oAuth2User,"blog");
        this.location                = tryGetStringAttributeValue(oAuth2User,"location");
        this.email                   = tryGetStringAttributeValue(oAuth2User,"email");
        this.hireable                = tryGetBooleanAttributeValue(oAuth2User,"hireable");
        this.bio                     = tryGetStringAttributeValue(oAuth2User,"bio");
        this.twitterUsername         = tryGetStringAttributeValue(oAuth2User,"twitter_username");
        this.publicRepos             = tryGetIntAttributeValue(oAuth2User,"public_repos");
        this.publicGists             = tryGetIntAttributeValue(oAuth2User,"public_gists");
        this.followers               = tryGetIntAttributeValue(oAuth2User,"followers");
        this.following               = tryGetIntAttributeValue(oAuth2User,"following");
        this.createdAt               = Instant.parse(tryGetStringAttributeValue(oAuth2User,"created_at"));
        this.updatedAt               = Instant.parse(tryGetStringAttributeValue(oAuth2User,"updated_at"));
        this.privateGists            = tryGetIntAttributeValue(oAuth2User,"private_gists");
        this.totalPrivateRepos       = tryGetIntAttributeValue(oAuth2User,"total_private_repos");
        this.ownedPrivateRepos       = tryGetIntAttributeValue(oAuth2User,"owned_private_repos");
        this.diskUsage               = tryGetIntAttributeValue(oAuth2User,"disk_usage");
        this.collaborators           = tryGetIntAttributeValue(oAuth2User,"collaborators");
        this.twoFactorAuthentication = tryGetBooleanAttributeValue(oAuth2User,"two_factor_authentication");
        this.plan                    = (Map<String, Object>) tryGetAttributeValue(oAuth2User, "plan");
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

    public int getId() {
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
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

}
