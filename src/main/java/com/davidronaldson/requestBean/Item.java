package com.davidronaldson.requestBean;

import java.math.BigDecimal;

public class Item {
    String name;
    BigDecimal price;

    public Item(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Item() {
    }

    public BigDecimal getPrice() {
        return price.setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
               return name + "  : - - - - - -  £" + price.setScale(2,BigDecimal.ROUND_HALF_UP);
    }
}
