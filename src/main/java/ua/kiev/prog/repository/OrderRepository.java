package ua.kiev.prog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kiev.prog.models.CustomUser;
import ua.kiev.prog.models.Dish;
import ua.kiev.prog.models.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOrderByDish(Dish dish);
    List<Order> findByUser(CustomUser user);
    Optional<Order> findByUserAndDish(CustomUser user, Dish dish);
    long countByUser(CustomUser user);
}
