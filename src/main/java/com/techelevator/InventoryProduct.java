package com.techelevator;

import java.math.BigDecimal;

public class InventoryProduct {

    //Variables for the products
    private String slotId;
    private String product;
    private BigDecimal price;

    private String message;

    //Constructor
    public InventoryProduct(String slotId, String product, BigDecimal price){
        this.slotId = slotId;
        this.product = product;
        this.price = price;


    }

    public String getSlotId() {
        return slotId;
    }

    public String getPrice(){
        return price.toString();
    }


}
