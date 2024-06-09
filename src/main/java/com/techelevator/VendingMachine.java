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
        INVENTORY_DISPLAY_BUFFER = new String[INVENTORY_LIST.size() + TITLE_LINE_COUNT];
        SALES_REPORT_BUFFER = new String[INVENTORY_LIST.size() + TITLE_LINE_COUNT + 1];

        buildInventoryTitle();
        buildSalesReportTitle();

        buildInventoryDisplay();
        buildSalesReport();
    }

    public void displayItems() {
        for (String productDisplay : INVENTORY_DISPLAY_BUFFER) {
            consoleOut.println(productDisplay);
        }

        consoleOut.flush();
    }

    public void addFunds(BigDecimal funds) {
        if (funds.compareTo(BigDecimal.ZERO) < 0) {
            consoleOut.printf("\nPlease re-enter your amount of $$ that is not negative! %s is not a valid deposit.\n", CURRENCY_INSTANCE.format(funds));
            consoleOut.flush();
            return;
        } else if (funds.compareTo(BigDecimal.ZERO)==0){
            consoleOut.printf("\nPlease re-enter your amount of $$ that is not zero! %s is not a valid deposit.\n", CURRENCY_INSTANCE.format(funds));
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

        buildInventoryDisplay(product.getDisplayIndex());
        buildSalesReport(product.getDisplayIndex());

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
        transaction = transaction.concat(String.format("| %s  %s\n", CURRENCY_INSTANCE.format(cost.doubleValue()), CURRENCY_INSTANCE.format(balance.doubleValue())));


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
        String[] dateTime = dateTimeArray("MM-dd-yyyy","kk-mm-ss");
        File salesFile = new File(String.format("%s_%s_sales.txt", dateTime[0], dateTime[1]));

        try(PrintWriter salesWriter = new PrintWriter(salesFile)) {
            for (String saleEntry : SALES_REPORT_BUFFER) {
                salesWriter.println(saleEntry);
                consoleOut.println(saleEntry);
            }

            salesWriter.flush();

        } catch (FileNotFoundException ex) {
            consoleOut.println("Error writing sales report to file!");
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

    private HashMap<String, Dispenser> loadInventory(List<String[]> tokenLines) throws InventoryLoadException {
        HashMap<String, Dispenser> loadedInv = new HashMap<>();
        inventoryFormatter = new ColumnTextFormatter(tokenLines.get(0).length, ITEM_DISPLAY_TITLE_TEXT);
        salesReportFormatter = new ColumnTextFormatter(2, SALES_REPORT_TITLE_TEXT);

        salesReportFormatter.addColumnWidth(SALES_TOTAL_TEXT, 0);
        inventoryFormatter.addColumnWidth(SOLD_OUT_TEXT, 3);

        for (String[] tokenLine : tokenLines) {
            if (loadedInv.containsKey(tokenLine[0])) continue;

            BigDecimal price;
            String slotId = tokenLine[0], productName = tokenLine[1];
            String priceString = tokenLine[2], productType = tokenLine[3];

            inventoryFormatter.addColumnWidth(slotId, 0);
            inventoryFormatter.addColumnWidth(productName, 1);
            salesReportFormatter.addColumnWidth(productName, 0);

            try {
                price = new BigDecimal(priceString);
                String currencyFormat = CURRENCY_INSTANCE.format(price.doubleValue());

                inventoryFormatter.addColumnWidth(currencyFormat, 2);
                salesReportFormatter.addColumnWidth(currencyFormat, 1);

            } catch (NumberFormatException ex) {
                throw new InventoryLoadException(String.format("%s, is not a valid number!", priceString));
            }

            Dispenser productDispenser = null;
            int productDisplayIndex = INVENTORY_LIST.size() + TITLE_LINE_COUNT;

            switch (productType) {
                case "Chip":
                    productDispenser = new Dispenser(new Chip(slotId, productName, price), inventoryFormatter, PRODUCTS_PER_SLOT, productDisplayIndex);

                    break;
                case "Candy":
                    productDispenser = new Dispenser(new Candy(slotId, productName, price), inventoryFormatter, PRODUCTS_PER_SLOT, productDisplayIndex);

                    break;
                case "Drink":
                    productDispenser = new Dispenser(new Drink(slotId, productName, price), inventoryFormatter, PRODUCTS_PER_SLOT, productDisplayIndex);

                    break;
                case "Gum":
                    productDispenser = new Dispenser(new Gum(slotId, productName, price), inventoryFormatter, PRODUCTS_PER_SLOT, productDisplayIndex);

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

    private void buildInventoryTitle() {
        INVENTORY_DISPLAY_BUFFER[0] = "=".repeat(inventoryFormatter.getRowWidth(2));
        INVENTORY_DISPLAY_BUFFER[1] = inventoryFormatter.format(ITEM_DISPLAY_TITLE_TEXT, '|', 2);
        INVENTORY_DISPLAY_BUFFER[2] = "=".repeat(inventoryFormatter.getRowWidth(2));
    }

    private void buildSalesReportTitle() {
        SALES_REPORT_BUFFER[0] = "=".repeat(salesReportFormatter.getRowWidth(2));
        SALES_REPORT_BUFFER[1] = salesReportFormatter.format(SALES_REPORT_TITLE_TEXT, '|', 2);
        SALES_REPORT_BUFFER[2] = "=".repeat(salesReportFormatter.getRowWidth(2));
    }

    private void buildInventoryDisplay() {
        for (int i = TITLE_LINE_COUNT; i < INVENTORY_DISPLAY_BUFFER.length; i++) {
            INVENTORY_DISPLAY_BUFFER[i] = INVENTORY_LIST.get(i - TITLE_LINE_COUNT).toString();
        }
    }

    private void buildInventoryDisplay(int entryIndex) {
        INVENTORY_DISPLAY_BUFFER[entryIndex] = INVENTORY_LIST.get(entryIndex - TITLE_LINE_COUNT).toString();
    }

    private void buildSalesReport() {
        totalSales = BigDecimal.ZERO;
        String totalMoney = CURRENCY_INSTANCE.format(totalSales.doubleValue());

        salesReportFormatter.addColumnWidth(totalMoney, 1);

        for (int i = TITLE_LINE_COUNT; i < SALES_REPORT_BUFFER.length - 1; i++) {
            Dispenser product = INVENTORY_LIST.get(i - TITLE_LINE_COUNT);

            SALES_REPORT_BUFFER[i] = salesReportFormatter.format(new String[] {
                    product.getDescription(), String.valueOf(PRODUCTS_PER_SLOT - product.getRemainingCount())
            }, '|', 2);
        }

        SALES_REPORT_BUFFER[INVENTORY_LIST.size() + TITLE_LINE_COUNT] = salesReportFormatter.format(new String[] {
                SALES_TOTAL_TEXT, totalMoney
        }, '|', 2);
    }

    private void buildSalesReport(int entryIndex) {
        Dispenser entry = INVENTORY_LIST.get(entryIndex - TITLE_LINE_COUNT);
        totalSales = totalSales.add(entry.getPrice());

        String totalMoney = CURRENCY_INSTANCE.format(totalSales.doubleValue());
        int previousMaxWidth = salesReportFormatter.getMaxColumnWidth(1);

        salesReportFormatter.addColumnWidth(totalMoney, 1);
        if (salesReportFormatter.getMaxColumnWidth(1) != previousMaxWidth) {
            /* If we have a new maxWidth for column 1 because of a new Total
             * go through all sales reports and update their buffers */
            for (int i = TITLE_LINE_COUNT; i < SALES_REPORT_BUFFER.length - 1; i++) {
                Dispenser product = INVENTORY_LIST.get(i - TITLE_LINE_COUNT);

                SALES_REPORT_BUFFER[i] = salesReportFormatter.format(new String[] {
                        product.getDescription(), String.valueOf(PRODUCTS_PER_SLOT - product.getRemainingCount())
                }, '|', 2);
            }
        }
        else {
            /* If maxWidth is still the same the format hasn't changed, and we
             * can just update the single buffer for this specific entry */
            SALES_REPORT_BUFFER[entryIndex] = salesReportFormatter.format(new String[] {
                    entry.getDescription(), String.valueOf(PRODUCTS_PER_SLOT - entry.getRemainingCount())
            }, '|', 2);
        }

        /* Update the buffer for the Total Sales entry row */
        SALES_REPORT_BUFFER[INVENTORY_LIST.size() + TITLE_LINE_COUNT] = salesReportFormatter.format(new String[] {
                SALES_TOTAL_TEXT, totalMoney
        }, '|', 2);

    }

    //private Scanner fileReader;
    private PrintWriter consoleOut;
    private FileWriter fileWriter;
    private BigDecimal currentBalance;
    private BigDecimal totalSales;
    private ColumnTextFormatter inventoryFormatter;
    private ColumnTextFormatter salesReportFormatter;

    private final int PRODUCTS_PER_SLOT;
    private final int TITLE_LINE_COUNT = 3;
    private final HashMap<String, Dispenser> INVENTORY_MAP;
    private final List<Dispenser> INVENTORY_LIST;
    private final String[] INVENTORY_DISPLAY_BUFFER;
    private final String[] SALES_REPORT_BUFFER;

    private static final String[] ITEM_DISPLAY_TITLE_TEXT = new String[] { "ID", "PRODUCT NAME", "COST", "AVAILABLE" };
    private static final String[] SALES_REPORT_TITLE_TEXT = new String[] { "PRODUCT NAME", "SOLD" };
    private static final String SALES_TOTAL_TEXT = "**TOTAL SALES**";
    private static final String ADD_FUNDS_LOG_TEXT = "FEED MONEY";
    private static final String CHANGE_LOG_TEXT = "GIVE CHANGE";
    private static final String SOLD_OUT_TEXT = "SOLD OUT";
    private static final NumberFormat CURRENCY_INSTANCE = NumberFormat.getCurrencyInstance(Locale.US);
}
