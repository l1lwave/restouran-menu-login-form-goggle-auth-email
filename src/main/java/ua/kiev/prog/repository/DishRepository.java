package ua.kiev.prog.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.kiev.prog.models.Dish;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    @Query("SELECT d FROM Dish d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :pattern, '%'))")
    List<Dish> findByPattern(@Param("pattern") String pattern, Pageable pageable);

    @Query("SELECT d FROM Dish d WHERE d.discount > 0")
    List<Dish> findAllDiscounts();

    List<Dish> findByPriceBetween(Double priceAfter, Double priceBefore);
}
