package com.techelevator.test;

import com.techelevator.Change;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class ChangeTest {

    @Test
    public void havingOneMoneyType_return_shortChangeMessageWithoutAnd(){
        //Arrange
        BigDecimal total = BigDecimal.valueOf(1.00);
        Change change = new Change(total);

        //Act then Assert

        String expected = "Your change is 1 dollar.";
        assertEquals(expected,change.toString());
    }

    @Test
    public void havingTwoMoneyType_return_shortChangeMessageWithAnd(){
        //Arrange
        BigDecimal total = BigDecimal.valueOf(1.01);
        Change change = new Change(total);

        //Act then Assert

        String expected = "Your change is 1 dollar and 1 penny.";
        assertEquals(expected,change.toString());
    }

    @Test
    public void havingThreeMoneyType_return_shortChangeMessageWithAnd(){
        //Arrange
        BigDecimal total = BigDecimal.valueOf(1.29);
        Change change = new Change(total);

        //Act then Assert

        String expected = "Your change is 1 dollar, 1 quarter, and 4 pennies.";
        assertEquals(expected,change.toString());
    }

    @Test
    public void havingFourMoneyType_return_shortChangeMessageWithAnd(){
        //Arrange
        BigDecimal total = BigDecimal.valueOf(1.39);
        Change change = new Change(total);

        //Act then Assert

        String expected = "Your change is 1 dollar, 1 quarter, 1 dime, and 4 pennies.";
        assertEquals(expected,change.toString());
    }

    @Test
    public void havingFiveMoneyType_return_shortChangeMessageWithAnd(){
        //Arrange
        BigDecimal total = BigDecimal.valueOf(20.99);
        Change change = new Change(total);

        //Act then Assert

        String expected = "Your change is 20 dollars, 3 quarters, 2 dimes, and 4 pennies.";
        assertEquals(expected,change.toString());
    }

    @Test
    public void havingSingularMoneyType_return_singularMoneyChangeMessage(){
        //Arrange
        BigDecimal oneDollar = BigDecimal.valueOf(1.00);
        BigDecimal oneQuarter = BigDecimal.valueOf(0.25);
        BigDecimal oneDime = BigDecimal.valueOf(0.10);
        BigDecimal oneNickel = BigDecimal.valueOf(0.05);
        BigDecimal onePenny = BigDecimal.valueOf(0.01);

        Change test1 = new Change(oneDollar);
        Change test2 = new Change(oneQuarter);
        Change test3 = new Change(oneDime);
        Change test4 = new Change(oneNickel);
        Change test5 = new Change(onePenny);

        //Act then Assert

        String expected1Dollar = "Your change is 1 dollar.";
        String expected1Quarter = "Your change is 1 quarter.";
        String expected1Dime = "Your change is 1 dime.";
        String expected1Nickel = "Your change is 1 nickel.";
        String expected1Penny = "Your change is 1 penny.";

        assertEquals(expected1Dollar,test1.toString());
        assertEquals(expected1Quarter,test2.toString());
        assertEquals(expected1Dime,test3.toString());
        assertEquals(expected1Nickel,test4.toString());
        assertEquals(expected1Penny,test5.toString());
    }

    @Test
    public void havingPluralMoneyType_return_pluralMoneyChangeMessage(){
        //Arrange
        BigDecimal twentyDollars = BigDecimal.valueOf(20.00);
        BigDecimal twoQuarters = BigDecimal.valueOf(0.50);
        BigDecimal twoDimes = BigDecimal.valueOf(0.20);
        BigDecimal twoPennies = BigDecimal.valueOf(0.02);

        Change test1 = new Change(twentyDollars);
        Change test2 = new Change(twoQuarters);
        Change test3 = new Change(twoDimes);
        Change test4 = new Change(twoPennies);


        //Act then Assert

        String expected20Dollars = "Your change is 20 dollars.";
        String expected2Quarters = "Your change is 2 quarters.";
        String expected2Dimes = "Your change is 2 dimes.";
        String expected2Pennies = "Your change is 2 pennies.";

        assertEquals(expected20Dollars,test1.toString());
        assertEquals(expected2Quarters,test2.toString());
        assertEquals(expected2Dimes,test3.toString());
        assertEquals(expected2Pennies,test4.toString());
    }

}
