package com.techelevator.product;

import java.math.BigDecimal;

public class Drink extends InventoryProduct{

    public Drink(String slotId, String product, BigDecimal price){
        super(slotId, product, price);
    }

    @Override
    public void useItem(){
        System.out.println("Glug Glug, Chug Chug!");
    }
}
