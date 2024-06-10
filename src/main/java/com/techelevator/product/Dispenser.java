package com.techelevator.product;

import com.techelevator.format.ColumnTextFormatter;

import java.math.BigDecimal;

public final class Dispenser implements IDispensable {
    public Dispenser(IDispensable item, ColumnTextFormatter textFormatter, int dispenseCount, int displayIndex) {
        this.ITEM = item;
        this.TEXT_FORMATTER = textFormatter;
        this.dispenseCount = dispenseCount;
        this.DISPLAY_INDEX = displayIndex;
    }

    @Override
    public void dispense() {
        if (dispenseCount < 1) return;

        ITEM.dispense();
        dispenseCount--;
    }

    @Override
    public String getID() { return ITEM.getID(); }

    @Override
    public String getDescription() { return ITEM.getDescription(); }

    @Override
    public BigDecimal getPrice() { return ITEM.getPrice(); }

    @Override
    public String toString() {
        String remainingText = (dispenseCount > 0) ? "   x" + dispenseCount : "SOLD OUT";

        return TEXT_FORMATTER.format(new String[]{
                ITEM.getID(),
                ITEM.getDescription(),
                "$" + ITEM.getPrice().toString(),
                remainingText
        }, '|', 2);
    }

    public int getRemainingCount() { return dispenseCount; }
    public int getDisplayIndex() { return DISPLAY_INDEX; }

    private int dispenseCount;

    private final ColumnTextFormatter TEXT_FORMATTER;
    private final IDispensable ITEM;
    private final int DISPLAY_INDEX;
}
