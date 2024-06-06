package com.techelevator;

import java.math.BigDecimal;

public class Chip extends InventoryProduct{

    public Chip (String slotId, String product, BigDecimal price){
        super(slotId,product,price);
    }

    @Override
    public String toString(){
        return "Crunch Crunch, It's Yummy!";
    }
}
