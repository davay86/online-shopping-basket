package com.davidronaldson.basket;

import com.davidronaldson.requestBean.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@SessionScope
public class BasketController {

    BigDecimal subTotal;
    BigDecimal total;
    BigDecimal appleSaving;
    BigDecimal breadSaving;

    List<Item> items;
    List<Item> availableItems;

    @RequestMapping("/items")
    public ModelAndView greeting(){
        items = new ArrayList<>();
        availableItems = new ArrayList<>();
        availableItems.add(new Item("Apple",new BigDecimal(.99)));
        availableItems.add(new Item("Bread", new BigDecimal(1.49)));
        availableItems.add(new Item("Milk", new BigDecimal(0.89)));
        availableItems.add(new Item("Soup", new BigDecimal(1.25)));
        subTotal = new BigDecimal(0);
        total = new BigDecimal(0);
        appleSaving = new BigDecimal(0);
        breadSaving = new BigDecimal(0);

        ModelAndView modelAndView = new ModelAndView("basket");
        modelAndView.addObject("item",new Item());
        modelAndView.addObject("list",items);
        modelAndView.addObject("subTotal",subTotal);
        modelAndView.addObject("availableItems",availableItems);
        return modelAndView;
    }

    @PostMapping("/items")
    public ModelAndView greetingSubmit(@ModelAttribute Item item, Model model) {
        addItem(item.getName());
        ModelAndView modelAndView = new ModelAndView("basket");
        modelAndView.addObject("list",items);
        modelAndView.addObject("subTotal",subTotal.setScale(2,BigDecimal.ROUND_HALF_UP));
        return modelAndView;
    }

    @PostMapping("/checkout")
    public ModelAndView checkout(Model model){
        calculateOffers(items);
        ModelAndView modelAndView = new ModelAndView("checkout");
        modelAndView.addObject("list",items);
        modelAndView.addObject("subTotal",subTotal.setScale(2,BigDecimal.ROUND_HALF_UP));
        modelAndView.addObject("total",total.setScale(2,BigDecimal.ROUND_HALF_UP));
        modelAndView.addObject("appleSaving",appleSaving.setScale(2,BigDecimal.ROUND_HALF_UP));
        modelAndView.addObject("breadSaving",breadSaving.setScale(2,BigDecimal.ROUND_HALF_UP));

        return modelAndView;
    }

    private void addItem(String itemDesc){
        switch (itemDesc){
            case "Apple":
                items.add(new Item("Apple",new BigDecimal(0.99)));
                subTotal = subTotal.add(new BigDecimal(0.99));
                break;
            case "Bread":
                items.add(new Item("Bread", new BigDecimal(1.49)));
                subTotal = subTotal.add(new BigDecimal(1.49));
                break;
            case "Milk":
                items.add(new Item("Milk", new BigDecimal(0.89)));
                subTotal = subTotal.add(new BigDecimal(0.89));
                break;
            case "Soup":
                items.add(new Item("Soup", new BigDecimal(1.25)));
                subTotal = subTotal.add(new BigDecimal(1.25));
            default:
                break;
        }
    }

    private void calculateOffers(List<Item> items){
        total = subTotal;
        calculateAppleOffer(items.stream().filter(e -> e.getName().equals("Apple")).collect(Collectors.toList()));
        calculateBreadSoupOffer(items);
    }

    private void calculateAppleOffer(List<Item> items){
        Double appleSaving = items.stream().mapToDouble(e -> {
            BigDecimal saving = e.getPrice().multiply(new BigDecimal(0.10));
            total = total.subtract(saving);
            return saving.doubleValue();
        }).sum();

        this.appleSaving = new BigDecimal(appleSaving);
    }

    private void calculateBreadSoupOffer(List<Item> items){
        long numberOfBread = items.stream().filter(e -> e.getName().equals("Bread")).count();
        long numberOfSoups = items.stream().filter(e -> e.getName().equals("Soup")).count();

        double breadSaving = 0;
        if(numberOfSoups !=0) {

            if (numberOfBread != 0) {
                long availableOffers = numberOfSoups / 2;
                breadSaving = items.stream().filter(e -> e.getName().equals("Bread")).limit(availableOffers).mapToDouble(e -> {
                    BigDecimal saving = e.getPrice().divide(new BigDecimal(2));
                    total = total.subtract(saving);
                    return saving.doubleValue();
                }).sum();
            }
        }
        this.breadSaving = new BigDecimal(breadSaving);
    }
}
