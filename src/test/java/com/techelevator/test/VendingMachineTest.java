package com.techelevator.test;

import com.techelevator.Dispenser;
import com.techelevator.VendingMachine;
import com.techelevator.exceptions.InventoryLoadException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VendingMachineTest {

    @Before
    public void Init() throws InventoryLoadException, NoSuchMethodException {
        output = new ByteArrayOutputStream();
        vendingMachine = new VendingMachine(5, output,"vendingmachine.csv");
        printTransactionMethod = vendingMachine.getClass().getDeclaredMethod("printTransaction", String.class, BigDecimal.class, BigDecimal.class);
        loadInventoryMethod = vendingMachine.getClass().getDeclaredMethod("loadInventory", List.class);

        printTransactionMethod.setAccessible(true);
        loadInventoryMethod.setAccessible(true);

        testTokens = new ArrayList<>();
        testTokens.add(new String[]{ "A1", "Potato Chips", "3.05", "Chip" });
        testTokens.add(new String[]{ "A2", "Stackers", "1.45", "Chip" });
        testTokens.add(new String[]{ "A3", "Grain Waves", "2.75", "Chip" });
    }

    @Test
    public void prints_transaction_in_proper_format() throws IllegalAccessException, InvocationTargetException {
        LocalDate today = LocalDate.now();
        String todayDate = DateTimeFormatter.ofPattern("MM/dd/yyyy").format(today);

        printTransactionMethod.invoke(vendingMachine, "CHARGEBACK", new BigDecimal("5.00"), new BigDecimal("25.00"));

        String transactionText = output.toString();
        String transactionTextNoTime = transactionText.replace(transactionText.substring(10, 23), "");
        output.reset();

        Assert.assertEquals(String.format("%s  CHARGEBACK    | $5.00  $25.00\r\n", todayDate), transactionTextNoTime);

        printTransactionMethod.invoke(vendingMachine, "FEED MONEY", new BigDecimal("23.50"), new BigDecimal("1.50"));

        transactionText = output.toString();
        transactionTextNoTime = transactionText.replace(transactionText.substring(10, 23), "");
        output.reset();

        Assert.assertEquals(String.format("%s  FEED MONEY    | $23.50  $1.50\r\n", todayDate), transactionTextNoTime);

        printTransactionMethod.invoke(vendingMachine, "CHANGE", new BigDecimal("5.25"), new BigDecimal("6.75"));

        transactionText = output.toString();
        transactionTextNoTime = transactionText.replace(transactionText.substring(10, 23), "");
        output.reset();

        Assert.assertEquals(String.format("%s  CHANGE        | $5.25  $6.75\r\n", todayDate), transactionTextNoTime);
    }

    @Test
    public void loads_inventory_from_inventory_tokens() throws IllegalAccessException, InvocationTargetException {
        HashMap<String, Dispenser> map = (HashMap<String, Dispenser>)loadInventoryMethod.invoke(vendingMachine, testTokens);
        Assert.assertTrue(map.containsKey("A1"));
        Assert.assertTrue(map.containsKey("A2"));
        Assert.assertTrue(map.containsKey("A3"));
        Assert.assertFalse(map.containsKey("B1"));
        Assert.assertFalse(map.containsKey("D1"));

        Dispenser entry = map.get("A1");
        Assert.assertEquals(entry.getDescription(), "Potato Chips");
        Assert.assertEquals(entry.getPrice(), new BigDecimal("3.05"));
        Assert.assertEquals(entry.getRemainingCount(), 5);

        entry = map.get("A3");
        Assert.assertEquals(entry.getDescription(), "Grain Waves");
        Assert.assertEquals(entry.getPrice(), new BigDecimal("2.75"));
        Assert.assertEquals(entry.getRemainingCount(), 5);
    }

    private ByteArrayOutputStream output;
    private VendingMachine vendingMachine;
    private Method printTransactionMethod;
    private Method loadInventoryMethod;
    private List<String[]> testTokens;
}
