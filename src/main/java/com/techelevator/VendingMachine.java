package com.techelevator;

import com.techelevator.exceptions.InvalidProductTypeException;
import com.techelevator.exceptions.InventoryLoadException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public final class VendingMachine {
    public VendingMachine(int productsPerSlot, String inventoryFilepath) throws InventoryLoadException {
        this.PRODUCTS_PER_SLOT = productsPerSlot;

        inventoryList = new ArrayList<>();
        inventoryMap = loadInventory(readInventoryFile(inventoryFilepath));
    }

    public void displayItems() {
        for (Dispenser product : inventoryList) {
            System.out.println(product);
        }
    }

    public void addFunds(BigDecimal funds) {
        if (funds.compareTo(BigDecimal.ZERO) == -1) return;

        currentBalance = currentBalance.add(funds);
        //logTransaction(ADD_FUNDS_LOG_TEXT, funds, currentBalance);
    }

    public void selectProduct(String slotNumber) {
        if (!inventoryMap.containsKey(slotNumber)) {
            System.out.println("Invalid Slot Number!");
            return;
        }

        Dispenser product = inventoryMap.get(slotNumber);
        if (product.getRemainingCount() == 0) {
            System.out.printf("Sorry we're all out %s!", product.getDescription());
            return;
        }

        product.dispense();
        currentBalance = currentBalance.subtract(product.getPrice());
        //logTransaction(String.format("%s %s", product.getDescription(), slotNumber), product.getPrice(), currentBalance);
    }

    public void finishTransaction() {


        //logTransaction()
    }

    private HashMap<String, Dispenser> loadInventory(List<String[]> tokenLines) throws InventoryLoadException {
        HashMap<String, Dispenser> loadedInv = new HashMap<>();

        for (String[] tokenLine : tokenLines) {
            if (loadedInv.containsKey(tokenLine[0])) continue;

            BigDecimal price = null;
            String slotId = tokenLine[0], productName = tokenLine[1];
            String priceString = tokenLine[2], productType = tokenLine[3];

            try {
                price = new BigDecimal(priceString);
            } catch (NumberFormatException ex) {
                throw new InventoryLoadException(String.format("%s, is not a valid number!", priceString));
            }

            Dispenser productDispenser = null;

            switch (productType) {
                case "Chip":
                    productDispenser = new Dispenser(new Chip(slotId, productName, price), PRODUCTS_PER_SLOT);

                    break;
                case "Candy":
                    productDispenser = new Dispenser(new Candy(slotId, productName, price), PRODUCTS_PER_SLOT);

                    break;
                case "Drink":
                    productDispenser = new Dispenser(new Drink(slotId, productName, price), PRODUCTS_PER_SLOT);

                    break;
                case "Gum":
                    productDispenser = new Dispenser(new Gum(slotId, productName, price), PRODUCTS_PER_SLOT);

                    break;
                default:
                    throw new InvalidProductTypeException();
            }

            inventoryList.add(productDispenser);
            loadedInv.put(slotId, productDispenser);
        }

        return loadedInv;
    }

    private List<String[]> readInventoryFile(String filePath) {
        List<String[]> serializedInv = new ArrayList<>();

        try {
            File invFile = new File(filePath);
            Scanner fileReader = new Scanner(invFile);

            while (fileReader.hasNextLine()) {
                serializedInv.add(fileReader.nextLine().split("\\|"));
            }

        } catch (FileNotFoundException ex) {
            System.out.println("Inventory File not found!");
        }

        return serializedInv;
    }

    //private Scanner fileReader;
    private FileWriter fileWriter;
    private BigDecimal currentBalance;
    private LocalDate dateOfOperation;
    private String selectedProduct;
    private HashMap<String, Dispenser> inventoryMap;
    private List<Dispenser> inventoryList;

    private final int PRODUCTS_PER_SLOT;
    private static final String ADD_FUNDS_LOG_TEXT = "FEED MONEY";
    private static final String CHANGE_LOG_TEXT = "GIVE CHANGE";
}
