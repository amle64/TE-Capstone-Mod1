package com.techelevator;

import java.math.BigDecimal;

public abstract class InventoryProduct implements IDispensable {
    //Constructor
    public InventoryProduct(String slotId, String product, BigDecimal price){
        this.slotId = slotId;
        this.product = product;
        this.price = price;
    }

    //Abstract method to be used by subclasses
    public abstract void useItem();

    @Override
    public void Dispense() { useItem(); }

    @Override
    public String toString(){
        return String.format("%s|%s|%s|",slotId,product,price);
    }

    public String getSlotId() {
        return slotId;
    }
    public String getPrice(){
        return price.toString();
    }

    //Variables for the products
    private final String slotId;
    private final String product;
    private final BigDecimal price;
}
