package com.techelevator.product;

import java.math.BigDecimal;

public interface IDispensable {
    void dispense();
    String getID();
    String getDescription();
    BigDecimal getPrice();
}
