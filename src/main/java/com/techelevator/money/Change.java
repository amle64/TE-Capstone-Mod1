package com.techelevator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Change {
    //Constructor
    public Change(BigDecimal total) {
        this.TOTAL_CHANGE_VALUE = total;
        BigDecimal remainder = total;
        List<MoneyValue> numberOfCoins = new ArrayList<>();

        if(total.compareTo(BigDecimal.ZERO)==0){
            displayChangeMsg ="Your total is zero, so no change is provided.";
            return;
        }

        //Handles all whole numbers greater than $1
        if (total.compareTo(BigDecimal.ONE) >= 0) {
            numberOfCoins.add(new MoneyValue(DOLLAR_INDEX,total.intValue()));
            remainder = remainder.subtract(BigDecimal.valueOf(numberOfCoins.get(DOLLAR_INDEX).value));
        }

        //Ends method when change is zero
        if (remainder.compareTo(BigDecimal.ZERO) != 0) {

            //Repeats until change is zero
            for (int i = 0; i < COIN_VALUES.length; i++) {
                BigDecimal coinValue = COIN_VALUES[i];
                BigDecimal coinMaxAmount = remainder.divide(coinValue, 0, RoundingMode.FLOOR);
                remainder = remainder.subtract(coinMaxAmount.multiply(coinValue));

                if (coinMaxAmount.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }

                switch (i) {
                    case 0:
                        numberOfCoins.add(new MoneyValue(QUARTER_INDEX,coinMaxAmount.intValue()));
                        break;
                    case 1:
                        numberOfCoins.add(new MoneyValue(DIME_INDEX,coinMaxAmount.intValue()));
                        break;
                    case 2:
                        numberOfCoins.add(new MoneyValue(NICKEL_INDEX, coinMaxAmount.intValue()));
                        break;
                    case 3:
                        numberOfCoins.add(new MoneyValue(PENNY_INDEX, coinMaxAmount.intValue()));
                        break;
                }

            }

        }

        // Assign proper change message //
        for (int i = 0;i<numberOfCoins.size();i++){
            MoneyValue moneyValue = numberOfCoins.get(i);
            String[] coinTexts = (moneyValue.value > 1) ? MONEY_TEXT_PLURAL : MONEY_TEXT_SINGULAR;

            if(moneyValue.value == 0){
                continue;
            }

            if(numberOfCoins.size()==1){
                displayChangeMsg = displayChangeMsg.concat(String.format("%d %s.",moneyValue.value,coinTexts[moneyValue.textIndex]));
                continue;
            } else if(i==numberOfCoins.size()-1){
                displayChangeMsg = displayChangeMsg.concat(String.format("and %d %s.",moneyValue.value, coinTexts[moneyValue.textIndex]));
                continue;
            }

            if(numberOfCoins.size()==2){
                displayChangeMsg = displayChangeMsg.concat(String.format("%d %s ",moneyValue.value, coinTexts[moneyValue.textIndex]));
            } else {
                displayChangeMsg = displayChangeMsg.concat(String.format("%d %s, ",moneyValue.value, coinTexts[moneyValue.textIndex]));

            }

        }

    }

    //Print string of change interaction
    @Override
    public String toString() { return displayChangeMsg; }

    public BigDecimal getTotal() { return TOTAL_CHANGE_VALUE; }

    private String displayChangeMsg = "Your change is ";
    private final BigDecimal TOTAL_CHANGE_VALUE;

    private final static BigDecimal[] COIN_VALUES = new BigDecimal[]{ BigDecimal.valueOf(0.25), BigDecimal.valueOf(0.10), BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.01) };
    private final static int DOLLAR_INDEX = 0;
    private final static int QUARTER_INDEX = 1;
    private final static int DIME_INDEX = 2;
    private final static int NICKEL_INDEX = 3;
    private final static int PENNY_INDEX = 4;

    private final static String[] MONEY_TEXT_SINGULAR = new String[]{"dollar", "quarter", "dime", "nickel", "penny"};
    private final static String[] MONEY_TEXT_PLURAL = new String[]{"dollars", "quarters", "dimes", "nickels", "pennies"};

}
