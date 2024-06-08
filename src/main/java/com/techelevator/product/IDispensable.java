package com.techelevator.product;

import java.math.BigDecimal;

public interface IDispensable {
    void dispense();
    String getDescription();
    BigDecimal getPrice();
}
