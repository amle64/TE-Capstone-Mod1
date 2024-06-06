package com.techelevator;

import java.math.BigDecimal;

public class Candy extends InventoryProduct{

    public Candy (String slotId, String product, BigDecimal price){
        super(slotId,product,price);
    }


    @Override
    public String toString(){
        return "Munch Munch, Mmm Mmm Good";
    }
}
