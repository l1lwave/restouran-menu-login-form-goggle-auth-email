package ua.kiev.prog.config;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ua.kiev.prog.models.CustomUser;
import ua.kiev.prog.models.UserRole;
import ua.kiev.prog.models.UserRegisterType;
import ua.kiev.prog.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Component
public class AuthHandler implements AuthenticationSuccessHandler {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Lazy
    public AuthHandler(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.getOrDefault("email", "");

        CustomUser user = userService.findByEmail(email);

        if (user == null) {
            String randomPassword = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(randomPassword);

            CustomUser customUser = CustomUser.of(
                    encodedPassword,
                    email,
                    UserRole.USER,
                    UserRegisterType.GOOGLE,
                    "",
                    ""
            );
            userService.addUser(customUser);
            httpServletResponse.sendRedirect("/");
        } else {
            if (user.getType().equals(UserRegisterType.GOOGLE)) {
                httpServletResponse.sendRedirect("/");
            } else {
                httpServletResponse.sendRedirect("/login?errorEmail");
            }
        }
    }
}
