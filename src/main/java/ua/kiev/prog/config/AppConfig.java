package ua.kiev.prog.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.kiev.prog.models.Dish;
import ua.kiev.prog.models.UserRole;
import ua.kiev.prog.models.UserRegisterType;
import ua.kiev.prog.services.DishService;
import ua.kiev.prog.services.UserService;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppConfig extends GlobalMethodSecurityConfiguration {

    public static final String ADMIN_LOGIN = "admin";

    @Bean
    public CommandLineRunner demo(final UserService userService,
                                  final PasswordEncoder encoder,
                                  final DishService dishService) {
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
                Dish dish;
                for(int i = 1; i <= 5; i++) {
                    dish = new Dish("Dish Name" + i, Double.valueOf(i), i, i);
                    dishService.addDish(dish);
                }
                for(int i = 1; i <= 5; i++) {
                    dish = new Dish("Dish Name" + i, Double.valueOf(i), i, 0);
                    dishService.addDish(dish);
                }


                userService.addUser(
                        encoder.encode("password"),
                        UserRole.ADMIN, UserRegisterType.FORM, ADMIN_LOGIN, "", "");
                userService.addUser(
                        encoder.encode("password"),
                        UserRole.USER, UserRegisterType.FORM, "user", "", "");
            }
        };
    }
}
