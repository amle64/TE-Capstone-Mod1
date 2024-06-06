package com.techelevator;

import java.math.BigDecimal;

public class Gum extends InventoryProduct{

    public Gum(String slotId, String product, BigDecimal price){
        super(slotId, product, price);
    }

    @Override
    public void useItem(){
        System.out.println("Chew Chew, Pop!");
    }
}
