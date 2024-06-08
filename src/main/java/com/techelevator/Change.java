package com.techelevator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Change {
    //Constructor
    public Change(BigDecimal total) {
        this.total = total;
        BigDecimal remainder = total;

        if(total.compareTo(BigDecimal.ZERO)==0){
            displayChangeMsg ="Your total is zero, so no change is provided.";
            return;
        }

        //Handles all whole numbers greater than $1
        if (total.compareTo(BigDecimal.ONE) > 0) {
            numberOfCoins[dollarIndex] = total.intValue();
            remainder = remainder.subtract(BigDecimal.valueOf(numberOfCoins[dollarIndex]));
        }

        //Ends method when change is zero
        if (remainder.compareTo(BigDecimal.ZERO) != 0) {

            //Repeats until change is zero
            for (int i = 0; i < orderArray.length; i++) {
                BigDecimal coinType = orderArray[i];
                BigDecimal coinMaxAmount = remainder.divide(coinType, 0, RoundingMode.FLOOR);
                remainder = remainder.subtract(coinMaxAmount.multiply(coinType));

                if (coinMaxAmount.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }

                switch (i) {
                    case 0:
                        numberOfCoins[quarterIndex] = coinMaxAmount.intValue();
                        break;
                    case 1:
                        numberOfCoins[dimeIndex] = coinMaxAmount.intValue();
                        break;
                    case 2:
                        numberOfCoins[nickelIndex] = coinMaxAmount.intValue();
                        break;
                    case 3:
                        numberOfCoins[pennyIndex] = coinMaxAmount.intValue();
                        break;
                }

            }

        }

        for (int i = 0; i < numberOfCoins.length; i++) {

            int coinCount = numberOfCoins[i];
            if (coinCount == 0) {
                continue;
            }

            String[] coinTexts = null;
            coinTexts = (coinCount > 1) ? coinTextPlural : coinTextSingular;

            if (i == numberOfCoins.length - 1) {
                displayChangeMsg = displayChangeMsg.concat(String.format("and %d %s", coinCount, coinTexts[i]));
            } else {
                displayChangeMsg = displayChangeMsg.concat(String.format("%d %s, ", coinCount, coinTexts[i]));
            }
        }

    }

    //Print string of change interaction
    @Override
    public String toString() { return displayChangeMsg; }

    public BigDecimal getTotal() { return total; }

    private final BigDecimal total;
    private final int[] numberOfCoins = new int[]{0, 0, 0, 0, 0};
    private String displayChangeMsg = "You have ";

    //private final static BigDecimal dollar = BigDecimal.valueOf(1.00);
    private final static BigDecimal quarter = BigDecimal.valueOf(0.25);
    private final static BigDecimal dime = BigDecimal.valueOf(0.10);
    private final static BigDecimal nickel = BigDecimal.valueOf(0.05);
    private final static BigDecimal penny = BigDecimal.valueOf(0.01);

    private final static BigDecimal[] orderArray = new BigDecimal[]{quarter, dime, nickel, penny};
    private final static int dollarIndex = 0;
    private final static int quarterIndex = 1;
    private final static int dimeIndex = 2;
    private final static int nickelIndex = 3;
    private final static int pennyIndex = 4;

    private final static String[] coinTextSingular = new String[]{"dollar", "quarter", "dime", "nickel", "penny"};
    private final static String[] coinTextPlural = new String[]{"dollars", "quarters", "dimes", "nickels", "pennies"};

}
