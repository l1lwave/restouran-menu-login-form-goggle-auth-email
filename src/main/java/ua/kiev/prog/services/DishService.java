package ua.kiev.prog.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kiev.prog.models.Dish;
import ua.kiev.prog.repository.DishRepository;
import ua.kiev.prog.repository.OrderRepository;
import java.util.List;

@Service
public class DishService {
    private final DishRepository dishRepository;
    private final OrderRepository orderRepository;

    public DishService(DishRepository dishRepository, OrderRepository orderRepository) {
        this.dishRepository = dishRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void addDish(Dish dish) {
        this.dishRepository.save(dish);
    }

    @Transactional(readOnly = true)
    public List<Dish> findAll(Pageable pageable) {
        return dishRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    public List<Dish> findAllDishesOnlyDiscount() {
        return this.dishRepository.findAllDiscounts();
    }

    @Transactional(readOnly = true)
    public List<Dish> findByPrice(Double priceFrom, Double priceTo) {
        return this.dishRepository.findByPriceBetween(priceFrom, priceTo);
    }

    @Transactional(readOnly=true)
    public List<Dish> findByPattern(String pattern, Pageable pageable) {
        return this.dishRepository.findByPattern(pattern, pageable);
    }

    @Transactional
    public long count() {
        return this.dishRepository.count();
    }

    @Transactional
    public void reset(){
        this.dishRepository.deleteAll();
        this.orderRepository.deleteAll();

        Dish dish;
        for(int i = 1; i <= 5; i++) {
            dish = new Dish("Dish Name" + i, Double.valueOf(i), i, i);
            addDish(dish);
        }
        for(int i = 1; i <= 5; i++) {
            dish = new Dish("Dish Name" + i, Double.valueOf(i), i, 0);
            addDish(dish);
        }
    }
}
