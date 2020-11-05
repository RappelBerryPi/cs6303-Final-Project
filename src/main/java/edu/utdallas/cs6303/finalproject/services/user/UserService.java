package edu.utdallas.cs6303.finalproject.services.user;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import edu.utdallas.cs6303.finalproject.model.database.GoogleOidcUser;
import edu.utdallas.cs6303.finalproject.model.database.Privilege;
import edu.utdallas.cs6303.finalproject.model.database.Role;
import edu.utdallas.cs6303.finalproject.model.database.User;
import edu.utdallas.cs6303.finalproject.model.database.repositories.PrivilegeRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.RoleRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.UserRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.UserTOTPRepository;
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

    @Autowired
    private UserTOTPRepository userTOTPRepository;

    @Autowired
    private GoogleAuthenticator googleAuthenticator;

    @Override
    public User createUserFromUserForm(CreateUserForm createUserForm) {
        User user = new User();
        user.setFirstName(createUserForm.getFirstName());
        user.setLastName(createUserForm.getLastName());
        user.setUsername(createUserForm.getUserName());
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
        createUserForm.setUserName(user.getUsername());
        return createUserForm;
    }

    @Override
    public void addAuthenticationTokenToSession(HttpServletRequest request, User user, String password) {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(user.getUsername(), password);
        Authentication                      auth    = authManager.authenticate(authReq);
        auth.setAuthenticated(true);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> returnList =
                user.getRoles().stream().map(Role::getPrivileges).flatMap(Collection::stream).map(Privilege::getName).map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());

        returnList.addAll(user.getRoles().stream().map(Role::getName).map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList()));
        return returnList;
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        User user = userRepository.findByUsername(userName);
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

    @Override
    @Transactional
    public ResponseEntity<Resource> getQRCode(Authentication authentication) throws WriterException, IOException {
        String userName = authentication.getName();
        if (authentication.getPrincipal() instanceof GoogleOidcUser) {
            GoogleOidcUser user = (GoogleOidcUser) authentication.getPrincipal();
            userName = user.getEmail();
        }
        userTOTPRepository.deleteByUserName(userName);
        userTOTPRepository.flush();
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials(userName);
        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("Mom and Pop's Shop", userName, key);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 200, 200);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(MatrixToImageWriter.toBufferedImage(bitMatrix), "png", os);
        Resource resource = new InputStreamResource(new ByteArrayInputStream(os.toByteArray()));
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource);

    }
}
