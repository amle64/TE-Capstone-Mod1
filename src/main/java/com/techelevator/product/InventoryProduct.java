package com.techelevator.product;

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
    public void dispense() { useItem(); }

    @Override
    public String toString(){
        return String.format("%s|%s|%s|",slotId,product,price);
    }

    @Override
    public String getDescription() { return product; };
    @Override
    public BigDecimal getPrice(){
        return price;
    }
    public String getSlotId() {
        return slotId;
    }

    //Variables for the products
    private final String slotId;
    private final String product;
    private final BigDecimal price;
}
