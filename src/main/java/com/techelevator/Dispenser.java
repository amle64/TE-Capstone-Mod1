package com.techelevator;

public final class Dispenser {
    public Dispenser(IDispensable item, int dispenseCount) {
        this.dispensedItem = item;
        this.dispenseCount = dispenseCount;
    }

    public void Dispense() {
        if (dispenseCount < 1) return;

        dispensedItem.Dispense();
        dispenseCount--;
    }

    public int getRemainingCount() { return dispenseCount; }

    private final IDispensable dispensedItem;
    private int dispenseCount;
}
