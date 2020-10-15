package edu.utdallas.cs6303.finalproject.services.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import edu.utdallas.cs6303.finalproject.model.database.Privilege;
import edu.utdallas.cs6303.finalproject.model.database.Role;
import edu.utdallas.cs6303.finalproject.model.database.User;
import edu.utdallas.cs6303.finalproject.model.database.repositories.PrivilegeRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.RoleRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.UserRepository;
import edu.utdallas.cs6303.finalproject.model.validation.CreateUserForm;

@Service
public class UserService implements UserServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Override
    public User createUserFromUserForm(CreateUserForm createUserForm) {
        User user = new User();
        user.setFirstName(createUserForm.getFirstName());
        user.setLastName(createUserForm.getLastName());
        user.setUserName(createUserForm.getUserName());
        user.setPassword(passwordEncoder.encode(createUserForm.getPassword()));
        user.setEmail(createUserForm.getEmail());
        user.Enable();
        user.unlockAccount();
        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER"));
        user.setRoles(roles);
        userRepository.save(user);
        return user;
    }

    @Override
    public CreateUserForm createUseFormFromUser(User user) {
        CreateUserForm createUserForm = new CreateUserForm();
        createUserForm.setFirstName(user.getFirstName());
        createUserForm.setLastName(user.getLastName());
        createUserForm.setEmail(user.getEmail());
        createUserForm.setUserName(user.getUserName());
        return createUserForm;
    }

    @Override
    public void addAuthenticationTokenToSession(HttpServletRequest request, User user, String password) {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(user.getUserName(), password);
        Authentication                      auth    = authManager.authenticate(authReq);
        auth.setAuthenticated(true);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> returnList = user
                .getRoles()
                .stream()
                .map(Role::getPrivileges)
                .flatMap(Collection::stream)
                .map(p -> new SimpleGrantedAuthority(p.getName()))
                .collect(Collectors.toList());
        returnList
            .addAll(user
                    .getRoles()
                    .stream()
                    .map(r -> new SimpleGrantedAuthority(r.getName()))
                    .collect(Collectors.toList())
                   );
        return returnList;
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        User user = userRepository.findByUserName(userName);
        return Optional.of(user);
    }

    @Override
    public boolean isPasswordValid(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    private List<Privilege> basicUserPrivileges = null;

    @Override
    public List<Privilege> getBasicUserPrivileges() {
        if (basicUserPrivileges == null) {
            basicUserPrivileges = new ArrayList<>();
            basicUserPrivileges.add(createPrivilegeIfNotFound("READ_OWN_ORDERS_PRIVILEGE"));
            basicUserPrivileges.add(createPrivilegeIfNotFound("WRITE_OWN_ORDERS_PRIVILEGE"));
            basicUserPrivileges.add(createPrivilegeIfNotFound("READ_STORE_ITEMS_PRIVILEGE"));
            basicUserPrivileges.add(createPrivilegeIfNotFound("READ_ALL_PROFILE_PICTURES_PRIVILEGE"));
            basicUserPrivileges.add(createPrivilegeIfNotFound("WRITE_OWN_PROFILE_PICTURE_PRIVILEGE"));
        }
        return basicUserPrivileges;
    }

    private List<Privilege> basicEmployeePrivileges = null;

    @Override
    public List<Privilege> getBasicEmployeePrivileges() {
        if (basicEmployeePrivileges == null) {
            basicEmployeePrivileges = new ArrayList<>();
            basicEmployeePrivileges.add(createPrivilegeIfNotFound("WRITE_STORE_ITEMS_PRIVILEGE"));
            basicEmployeePrivileges.add(createPrivilegeIfNotFound("READ_ALL_ORDERS_PRIVILEGE"));
            basicEmployeePrivileges.add(createPrivilegeIfNotFound("WRITE_ALL_ORDERS_PRIVILEGE"));
            basicEmployeePrivileges.add(createPrivilegeIfNotFound("WRITE_ALL_PICTURES_PRIVILEGE"));
            basicEmployeePrivileges.add(createPrivilegeIfNotFound("READ_ALL_PICTURES_PRIVILEGE"));
            basicEmployeePrivileges.add(createPrivilegeIfNotFound("READ_ALL_SALES_PRIVILEGE"));
        }
        return basicEmployeePrivileges;
    }

    private List<Privilege> adminPrivileges = null;

    @Override
    public List<Privilege> getAdminPrivileges() {
        if (adminPrivileges == null) {
            adminPrivileges = new ArrayList<>();
            adminPrivileges.add(createPrivilegeIfNotFound("ADMIN_PRIVILEGE"));
        }
        return adminPrivileges;
    }

    private Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }
}
