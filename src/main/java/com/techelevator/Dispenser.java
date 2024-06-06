package com.techelevator;

import java.math.BigDecimal;

public final class Dispenser implements IDispensable {
    public Dispenser(IDispensable item, int dispenseCount) {
        this.dispensedItem = item;
        this.dispenseCount = dispenseCount;
    }

    @Override
    public void dispense() {
        if (dispenseCount < 1) return;

        dispensedItem.dispense();
        dispenseCount--;
    }

    @Override
    public String getDescription() { return dispensedItem.getDescription(); }

    @Override
    public BigDecimal getPrice() { return dispensedItem.getPrice(); }

    @Override
    public String toString() { return dispensedItem.toString(); }

    public int getRemainingCount() { return dispenseCount; }

    private final IDispensable dispensedItem;
    private int dispenseCount;
}
