package ua.kiev.prog.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.kiev.prog.mail.EmailService;
import ua.kiev.prog.models.CustomUser;
import ua.kiev.prog.models.UserRole;
import ua.kiev.prog.models.UserRegisterType;
import ua.kiev.prog.services.UserService;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LoginController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public LoginController(UserService userService, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof User) {
            User user = (User) principal;
            String email = user.getUsername();
            CustomUser dbUser = userService.findByEmail(email);

            if (dbUser != null) {
                model.addAttribute("roles", user.getAuthorities());
                model.addAttribute("email", dbUser.getEmail());
                model.addAttribute("phone", dbUser.getPhone());
                model.addAttribute("address", dbUser.getAddress());
                model.addAttribute("admin", isAdmin(user));
            } else {
                throw new IllegalStateException();
            }
        } else if (principal instanceof DefaultOidcUser) {
            DefaultOidcUser oAuth2User = (DefaultOidcUser) principal;
            String email = (String) oAuth2User.getAttributes().get("email");
            CustomUser dbUser = userService.findByEmail(email);

            List<GrantedAuthority> roles = oAuth2User.getAuthorities().stream()
                    .filter(authority -> authority.getAuthority().startsWith("ROLE_"))
                    .collect(Collectors.toList());

            if (dbUser != null) {
                model.addAttribute("roles", roles);
                model.addAttribute("email", dbUser.getEmail());
                model.addAttribute("phone", dbUser.getPhone());
                model.addAttribute("address", dbUser.getAddress());
                model.addAttribute("admin", isAdmin(oAuth2User));
            } else {
                throw new IllegalStateException();
            }
        } else {
            throw new IllegalStateException();
        }

        return "profile";
    }

    @PostMapping(value = "/update")
    public String update(@RequestParam(required = false) String phone,
                         @RequestParam(required = false) String address) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;

            String userEmail = user.getUsername();
            userService.updateUser(userEmail, phone, address);
        } else if (principal instanceof DefaultOidcUser) {
            DefaultOidcUser oAuth2User = (DefaultOidcUser) principal;
            String oAuthEmail = (String) oAuth2User.getAttributes().get("email");

            userService.updateUser(oAuthEmail, phone, address);
        }


        return "redirect:/";
    }

    @PostMapping(value = "/newuser")
    public String newuser(@RequestParam String email,
                         @RequestParam String password,
                         @RequestParam(required = false) String phone,
                         @RequestParam(required = false) String address,
                          Model model) {
        String passHash = passwordEncoder.encode(password);
        String code = generateCode();

        if(userService.existByEmail(email)) {
            model.addAttribute("exists", true);
            model.addAttribute("email", email);
            return "register";
        }

        emailService.sendMessage(email, code);
        model.addAttribute("email", email);
        model.addAttribute("password", passHash);
        model.addAttribute("phone", phone);
        model.addAttribute("address", address);
        model.addAttribute("trueCode", code);
        return "code";
    }

    @PostMapping(value = "/code")
    public String code(@RequestParam String inputCode,
                       @RequestParam String email,
                       @RequestParam String password,
                       @RequestParam(required = false) String phone,
                       @RequestParam(required = false) String address,
                       @RequestParam String trueCode,
                       Model model) {


        if (!inputCode.equals(trueCode)) {
            model.addAttribute("errorCode", true);
            model.addAttribute("email", email);
            model.addAttribute("password", password);
            model.addAttribute("phone", phone);
            model.addAttribute("address", address);
            model.addAttribute("trueCode", trueCode);
            return "code";
        }

        CustomUser dbUser = CustomUser.of(password, email, UserRole.USER, UserRegisterType.FORM, phone, address);
        userService.addUser(dbUser);

        return "redirect:/login";
    }

    @PostMapping(value = "/delete")
    public String delete(@RequestParam(name = "toDelete[]", required = false) List<Long> ids,
                         Model model) {
        userService.deleteUsers(ids);
        model.addAttribute("users", userService.getAllUsers());

        return "admin";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String admin(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private boolean isAdmin(User user) {
        Collection<GrantedAuthority> roles = user.getAuthorities();

        for (GrantedAuthority auth : roles) {
            if ("ROLE_ADMIN".equals(auth.getAuthority()))
                return true;
        }

        return false;
    }

    private boolean isAdmin(DefaultOidcUser user) {
        Collection<GrantedAuthority> roles = (Collection<GrantedAuthority>) user.getAuthorities();

        for (GrantedAuthority auth : roles) {
            if ("ROLE_ADMIN".equals(auth.getAuthority()))
                return true;
        }

        return false;
    }

    public static String generateCode() {
        final String CHARACTERS = "0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final int CODE_LENGTH = 10;

        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }
}
