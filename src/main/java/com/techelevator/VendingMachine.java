package com.techelevator;

import com.techelevator.exceptions.InvalidProductTypeException;
import com.techelevator.exceptions.InventoryLoadException;
import com.techelevator.format.ColumnTextFormatter;
import com.techelevator.product.*;

import java.io.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class VendingMachine {
    public VendingMachine(int productsPerSlot, OutputStream outputStream, String inventoryFilepath) throws InventoryLoadException {
        PRODUCTS_PER_SLOT = productsPerSlot;

        try {
            String [] dateTimeArray = dateTimeArray("MM-dd-yyyy","kk-mm-ss");
            String date = dateTimeArray[0];
            String time = dateTimeArray[1];

            File logFile = new File(String.format("%s_%s_log.txt",date,time));
            fileWriter = new FileWriter(logFile);
        } catch (IOException ex){
            consoleOut.println("An IOException error occurred");
            consoleOut.flush();
        }

        consoleOut = new PrintWriter(outputStream);
        currentBalance = BigDecimal.ZERO;

        INVENTORY_LIST = new ArrayList<>();
        INVENTORY_MAP = loadInventory(readInventoryFile(inventoryFilepath));
        INVENTORY_DISPLAY_BUFFER = new String[INVENTORY_LIST.size() + ITEM_DISPLAY_LINE_COUNT];

        buildInventoryTitle();
        buildInventoryDisplay();
    }

    public void displayItems() {
        for (String productDisplay : INVENTORY_DISPLAY_BUFFER) {
            consoleOut.println(productDisplay);
        }

        consoleOut.flush();
    }

    public void addFunds(BigDecimal funds) {
        if (funds.compareTo(BigDecimal.ZERO) < 0) {
            consoleOut.printf("\nPlease re-enter your amount of $$ that is not negative! %s is not a valid deposit.\n",currencyFormat.format(funds));
            consoleOut.flush();
            return;
        } else if (funds.compareTo(BigDecimal.ZERO)==0){
            consoleOut.printf("\nPlease re-enter your amount of $$ that is not zero! %s is not a valid deposit.\n",currencyFormat.format(funds));
            consoleOut.flush();
            return;
        }

        currentBalance = currentBalance.add(funds);
        printTransaction(ADD_FUNDS_LOG_TEXT, funds, currentBalance);
    }

    public void selectProduct(String slotNumber) {
        if (!INVENTORY_MAP.containsKey(slotNumber)) {
            consoleOut.println("\nInvalid Slot Number!");
            consoleOut.flush();
            return;
        }

        Dispenser product = INVENTORY_MAP.get(slotNumber);
        if (currentBalance.subtract(product.getPrice()).compareTo(BigDecimal.ZERO) < 0) {
            consoleOut.println("\nNot enough funds! Please add more!");
            consoleOut.flush();
            return;
        }

        if (product.getRemainingCount() == 0) {
            consoleOut.printf("\nSorry we're all out %s!\n", product.getDescription());
            consoleOut.flush();
            return;
        }

        currentBalance = currentBalance.subtract(product.getPrice());

        product.dispense();

        buildInventoryDisplay();
        printTransaction(String.format("%s %s", product.getDescription(), slotNumber), product.getPrice(), currentBalance);
    }

    public void finishTransaction() {
        Change changeTransaction = new Change(currentBalance);
        currentBalance = currentBalance.subtract(changeTransaction.getTotal());

        consoleOut.println(changeTransaction);
        consoleOut.flush();

        printTransaction(CHANGE_LOG_TEXT, changeTransaction.getTotal(), currentBalance);
    }

    private void printTransaction(String transactionName, BigDecimal cost, BigDecimal balance){
        String [] dateTimeArray = dateTimeArray("MM/dd/yyyy","hh:mm:ss a");

        String date = dateTimeArray[0];
        String time = dateTimeArray[1];

        String transaction = String.format("%s  %s  %s", date,time,transactionName);
        for (int i = 0; i < 14-transactionName.length(); i++) {
            transaction = transaction.concat(" ");
        }
        consoleOut.println("\n*******************Transaction Message*******************");
        transaction = transaction.concat(String.format("| %s  %s\n",currencyFormat.format(cost.doubleValue()), currencyFormat.format(balance.doubleValue())));


        consoleOut.print(transaction);
        consoleOut.flush();

        try {
            fileWriter.append(transaction);
            fileWriter.flush();
        }
        catch (IOException ex){
            consoleOut.println("You have an IOException error!");
        }

    }

    public void printSalesReport(){
        Dispenser product;
        for (int i = 0; i < INVENTORY_LIST.size(); i++){
            product = INVENTORY_LIST.get(i);
            consoleOut.printf("| %s|%d\n", product.getDescription(), PRODUCTS_PER_SLOT - product.getRemainingCount());
        }
        consoleOut.flush();
    }

    private String[] dateTimeArray(String datePattern, String timePattern){
        //Date
        LocalDate dateOfOperation = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(datePattern);
        String date = dateFormat.format(dateOfOperation);

        //Time
        LocalTime timeOfOperation = LocalTime.now();
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern(timePattern);
        String time = timeFormat.format(timeOfOperation);

        return new String[] {date,time};
    }

    private void buildInventoryTitle() {
        INVENTORY_DISPLAY_BUFFER[0] = "=".repeat(inventoryFormatter.getRowWidth(2));
        INVENTORY_DISPLAY_BUFFER[1] = inventoryFormatter.format(ITEM_DISPLAY_TITLE_TEXT, '|', 2);
        INVENTORY_DISPLAY_BUFFER[2] = "=".repeat(inventoryFormatter.getRowWidth(2));
    }

    private void buildInventoryDisplay() {
        for (int i = 3; i < INVENTORY_DISPLAY_BUFFER.length; i++) {
            INVENTORY_DISPLAY_BUFFER[i] = INVENTORY_LIST.get(i - 3).toString();
        }
    }

    private HashMap<String, Dispenser> loadInventory(List<String[]> tokenLines) throws InventoryLoadException {
        HashMap<String, Dispenser> loadedInv = new HashMap<>();
        inventoryFormatter = new ColumnTextFormatter(tokenLines.get(0).length, ITEM_DISPLAY_TITLE_TEXT);

        inventoryFormatter.addColumnWidth(SOLD_OUT_TEXT, 3);

        for (String[] tokenLine : tokenLines) {
            if (loadedInv.containsKey(tokenLine[0])) continue;

            BigDecimal price;
            String slotId = tokenLine[0], productName = tokenLine[1];
            String priceString = tokenLine[2], productType = tokenLine[3];

            inventoryFormatter.addColumnWidth(slotId, 0);
            inventoryFormatter.addColumnWidth(productName, 1);

            try {
                price = new BigDecimal(priceString);
                inventoryFormatter.addColumnWidth(currencyFormat.format(price.doubleValue()), 2);

            } catch (NumberFormatException ex) {
                throw new InventoryLoadException(String.format("%s, is not a valid number!", priceString));
            }

            Dispenser productDispenser = null;

            switch (productType) {
                case "Chip":
                    productDispenser = new Dispenser(new Chip(slotId, productName, price), inventoryFormatter, PRODUCTS_PER_SLOT);

                    break;
                case "Candy":
                    productDispenser = new Dispenser(new Candy(slotId, productName, price), inventoryFormatter, PRODUCTS_PER_SLOT);

                    break;
                case "Drink":
                    productDispenser = new Dispenser(new Drink(slotId, productName, price), inventoryFormatter, PRODUCTS_PER_SLOT);

                    break;
                case "Gum":
                    productDispenser = new Dispenser(new Gum(slotId, productName, price), inventoryFormatter, PRODUCTS_PER_SLOT);

                    break;
                default:
                    throw new InvalidProductTypeException();
            }

            INVENTORY_LIST.add(productDispenser);
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
            consoleOut.println("Inventory File not found!");
            consoleOut.flush();
        }

        return serializedInv;
    }

    //private Scanner fileReader;
    private PrintWriter consoleOut;
    private FileWriter fileWriter;
    private BigDecimal currentBalance;
    private ColumnTextFormatter inventoryFormatter;

    private final int PRODUCTS_PER_SLOT;
    private final int ITEM_DISPLAY_LINE_COUNT = 3;
    private final HashMap<String, Dispenser> INVENTORY_MAP;
    private final List<Dispenser> INVENTORY_LIST;
    private final String[] INVENTORY_DISPLAY_BUFFER;

    private static final String[] ITEM_DISPLAY_TITLE_TEXT = new String[] { "ID", "PRODUCT NAME", "COST", "AVAILABLE" };
    private static final String ADD_FUNDS_LOG_TEXT = "FEED MONEY";
    private static final String CHANGE_LOG_TEXT = "GIVE CHANGE";
    private static final String SOLD_OUT_TEXT = "SOLD OUT";
    private final static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
}
