package com.techelevator.product;

import com.techelevator.format.ColumnTextFormatter;

import java.math.BigDecimal;

public final class Dispenser implements IDispensable {
    public Dispenser(IDispensable item, ColumnTextFormatter textFormatter, int dispenseCount, int displayIndex) {
        this.dispensedItem = item;
        this.textFormatter = textFormatter;
        this.dispenseCount = dispenseCount;
        this.displayIndex = displayIndex;
    }

    @Override
    public void dispense() {
        if (dispenseCount < 1) return;

        dispensedItem.dispense();
        dispenseCount--;
    }

    @Override
    public String getID() { return dispensedItem.getID(); }

    @Override
    public String getDescription() { return dispensedItem.getDescription(); }

    @Override
    public BigDecimal getPrice() { return dispensedItem.getPrice(); }

    @Override
    public String toString() {
        String remainingText = (dispenseCount > 0) ? "   x" + dispenseCount : "SOLD OUT";

        return textFormatter.format(new String[]{
                dispensedItem.getID(),
                dispensedItem.getDescription(),
                "$" + dispensedItem.getPrice().toString(),
                remainingText
        }, '|', 2);
    }

    public int getRemainingCount() { return dispenseCount; }
    public int getDisplayIndex() { return displayIndex; }

    private final IDispensable dispensedItem;
    private final ColumnTextFormatter textFormatter;
    private int dispenseCount;
    private int displayIndex;
}
