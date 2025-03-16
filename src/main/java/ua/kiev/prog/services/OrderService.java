package ua.kiev.prog.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kiev.prog.models.CustomUser;
import ua.kiev.prog.models.Dish;
import ua.kiev.prog.models.Order;
import ua.kiev.prog.repository.DishRepository;
import ua.kiev.prog.repository.OrderRepository;
import ua.kiev.prog.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final DishRepository dishRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public OrderService(DishRepository dishRepository, OrderRepository orderRepository,
                        UserRepository userRepository, UserService userService) {
        this.dishRepository = dishRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Transactional
    public void deleteFromOrder(long[] toDelete) {
        CustomUser currentUser = getCurrentCustomUser();

        for (long id : toDelete) {
            Optional<Dish> dishOptional = dishRepository.findById(id);
            if (dishOptional.isPresent()) {
                Dish dish = dishOptional.get();
                Optional<Order> order = orderRepository.findByUserAndDish(currentUser, dish);
                order.ifPresent(orderRepository::delete);
            }
        }
    }

    @Transactional
    public void addOrder(long dishId) {
        CustomUser currentUser = getCurrentCustomUser();
        Optional<Dish> dishOptional = dishRepository.findById(dishId);

        if (dishOptional.isPresent()) {
            Dish dish = dishOptional.get();

            Optional<Order> existingOrder = orderRepository.findByUserAndDish(currentUser, dish);
            if (existingOrder.isPresent()) {
                return;
            }

            Order order = new Order(dish, currentUser);
            orderRepository.save(order);
        }
    }


    @Transactional(readOnly = true)
    public List<Order> findOrder() {
        CustomUser currentUser = getCurrentCustomUser();
        return orderRepository.findByUser(currentUser);
    }

    @Transactional(readOnly = true)
    public long countOrder() {
        CustomUser currentUser = getCurrentCustomUser();
        return orderRepository.countByUser(currentUser);
    }

    private CustomUser getCurrentCustomUser() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (principal instanceof User) {
            User user = (User) principal;
            return userService.findByEmail(user.getUsername());
        } else if (principal instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
            String email = (String) oidcUser.getAttributes().getOrDefault("email", "");
            return userService.findByEmail(email);
        } else {
            throw new IllegalStateException();
        }
    }

}
