package ua.kiev.prog.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kiev.prog.config.AppConfig;
import ua.kiev.prog.models.CustomUser;
import ua.kiev.prog.models.UserRole;
import ua.kiev.prog.models.UserRegisterType;
import ua.kiev.prog.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUsers(List<Long> ids) {
        ids.forEach(id -> {
            Optional<CustomUser> user = userRepository.findById(id);
            user.ifPresent(u -> {
                if ( ! AppConfig.ADMIN_LOGIN.equals(u.getEmail())) {
                    userRepository.deleteById(u.getId());
                }
            });
        });
    }

    @Transactional
    public boolean addUser(String passHash,
                           UserRole role,
                           UserRegisterType registerType,
                           String email,
                           String phone,
                           String address) {
        if (userRepository.existsByEmail(email))
            return false;

        CustomUser user = new CustomUser(passHash, role, registerType, email, phone, address);
        userRepository.save(user);

        return true;
    }

    @Transactional
    public void updateUser(String email, String phone, String address) {
        CustomUser user = userRepository.findByEmail(email);
        if (user == null)
            return;

        user.setAddress(address);
        user.setPhone(phone);

        userRepository.save(user);
    }

    @Transactional
    public void addUser(CustomUser customUser) {
        userRepository.save(customUser);
    }

    @Transactional
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    @Transactional(readOnly = true)
    public CustomUser findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
