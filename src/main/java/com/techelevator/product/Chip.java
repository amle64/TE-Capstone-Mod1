package com.techelevator.product;

import java.math.BigDecimal;

public class Chip extends InventoryProduct{

    public Chip (String slotId, String product, BigDecimal price){
        super(slotId,product,price);
    }

    @Override
    public void useItem(){
        System.out.println("Crunch Crunch, It's Yummy!");
    }
}
