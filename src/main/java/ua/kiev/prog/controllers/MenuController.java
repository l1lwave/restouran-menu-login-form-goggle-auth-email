package ua.kiev.prog.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.kiev.prog.models.Dish;
import ua.kiev.prog.models.Order;
import ua.kiev.prog.services.DishService;
import ua.kiev.prog.services.OrderService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MenuController {
    static final int ITEMS_PER_PAGE = 6;

    private final DishService dishService;
    private final OrderService orderService;

    public MenuController(DishService dishService, OrderService orderService) {
        this.dishService = dishService;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(required = false, defaultValue = "0") Integer page) {
        if (page < 0) page = 0;

        List<Dish> dishes = dishService
                .findAll(PageRequest.of(page, ITEMS_PER_PAGE, Sort.Direction.DESC, "id"));

        model.addAttribute("dishes", dishes);
        model.addAttribute("allPages", getPageCount());
        model.addAttribute("count", orderService.countOrder());

        return "index";
    }

    @GetMapping("/discount_dishes")
    public String discount(Model model) {

        List<Dish> dishes = dishService.findAllDishesOnlyDiscount();

        model.addAttribute("dishes", dishes);
        model.addAttribute("count", orderService.countOrder());

        return "index";
    }

    @GetMapping("/order")
    public String orders(Model model) {
        List<Order> orders = orderService.findOrder();
        List<Dish> dishesInOrder = new ArrayList<>();
        for (Order order : orders) {
            dishesInOrder.add(order.getDish());
        }
        model.addAttribute("dishesOrder", dishesInOrder);

        return "order";
    }

    @GetMapping("/reset")
    public String reset() {
        dishService.reset();
        return "redirect:/";
    }

    @GetMapping("/add_dish")
    public String contactAddPage() {
        return "dish_add_page";
    }

    @PostMapping(value = "/search")
    public String search(@RequestParam String pattern, Model model) {
        model.addAttribute("dishes", dishService.findByPattern(pattern, null /* HW */));

        return "index";
    }

    @PostMapping(value = "/price")
    public String price(@RequestParam String priceFrom, String priceTo, Model model) {
        try{
           Double priceF = Double.parseDouble(priceFrom);
           Double priceT = Double.parseDouble(priceTo);

           model.addAttribute("dishes", dishService.findByPrice(priceF, priceT));

        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

        return "index";
    }

    @PostMapping(value="/dish/add")
    public String contactAdd(
                             @RequestParam String name,
                             @RequestParam Double price,
                             @RequestParam Integer weight,
                             @RequestParam Integer discount)
    {
        Dish dish = new Dish(name, price, weight, discount);
        dishService.addDish(dish);

        return "redirect:/";
    }

    @PostMapping(value = "/dish/order")
    public ResponseEntity<Void> toOrder(
            @RequestParam(value = "toOrder") long toOrder) {
            orderService.addOrder(toOrder);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "dish/orderDelete")
    public ResponseEntity<Void> fromOrder(
            @RequestParam(value = "toDelete[]", required = false) long[] toDelete) {
        if (toDelete != null && toDelete.length > 0)
            orderService.deleteFromOrder(toDelete);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private long getPageCount() {
        long totalCount = dishService.count();
        return (totalCount / ITEMS_PER_PAGE) + ((totalCount % ITEMS_PER_PAGE > 0) ? 1 : 0);
    }
}
