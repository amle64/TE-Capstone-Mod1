package com.techelevator;

import java.math.BigDecimal;

public class Drink extends InventoryProduct{

    public Drink(String slotId, String product, BigDecimal price){
        super(slotId, product, price);
    }

    @Override
    public String toString(){
        return "Glug Glug, Chug Chug!";
    }
}
