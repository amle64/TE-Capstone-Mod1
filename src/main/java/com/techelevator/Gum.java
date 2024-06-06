package com.techelevator;

import java.math.BigDecimal;

public class Gum extends InventoryProduct{

    public Gum(String slotId, String product, BigDecimal price){
        super(slotId, product, price);
    }

    @Override
    public String toString(){
        return "Chew Chew, Pop!";
    }
}
