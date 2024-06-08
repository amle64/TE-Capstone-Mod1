package com.techelevator.product;

import java.math.BigDecimal;

public class Candy extends InventoryProduct{

    public Candy (String slotId, String product, BigDecimal price){
        super(slotId,product,price);
    }

    @Override
    public void useItem(){
        System.out.println("Munch Munch, Mmm Mmm Good");
    }
}
