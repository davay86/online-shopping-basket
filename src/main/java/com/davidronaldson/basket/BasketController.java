package com.davidronaldson.basket;

import com.davidronaldson.requestBean.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BasketController {

    BigDecimal total = new BigDecimal(0);

    List<String> items = new ArrayList<>();

    @RequestMapping("/items")
    public String greeting(Model model){
        model.addAttribute("item",new Item());
        model.addAttribute("list",items);
        model.addAttribute("total",total);
        return "basket";
    }

    @PostMapping("/items")
    public String greetingSubmit(@ModelAttribute Item item, Model model) {
        addItem(item.getName());
        model.addAttribute("list", items);
        model.addAttribute("total",total.setScale(2,BigDecimal.ROUND_HALF_UP));
        return "basket";
    }

    private void addItem(String itemDesc){
        switch (itemDesc){
            case "Apple":
                items.add(itemDesc);
                total = total.add(new BigDecimal(0.99));
                break;
            case "Bread":
                items.add(itemDesc);
                total = total.add(new BigDecimal(1.49));
                break;
            case "Milk":
                items.add(itemDesc);
                total = total.add(new BigDecimal(0.89));
                break;
            case "Soup":
                items.add(itemDesc);
                total = total.add(new BigDecimal(1.25));
            default:
                break;
        }
    }
}
