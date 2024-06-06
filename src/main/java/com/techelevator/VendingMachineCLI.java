package com.techelevator;

import com.sun.tools.javac.Main;
import com.techelevator.exceptions.InvalidProductTypeException;
import com.techelevator.exceptions.InventoryLoadException;
import com.techelevator.view.MainMenu;
import com.techelevator.view.PurchaseMenu;
import com.techelevator.view.VendingMenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class VendingMachineCLI {
	public VendingMachineCLI(VendingMenu menu) {
		this.menu = menu;
	}

	public void run() {
		if (!Init()) return;

		boolean running = true;
		while (running) {
			MainMenu.Options choice = (MainMenu.Options)menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			switch (choice) {
				case DisplayItems:
					break;
				case Purchase:
					PurchaseMenu.Options purchaseChoice = (PurchaseMenu.Options)menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);

					break;
				case Exit:
					running = false;
					continue;
				case Secret:
					break;
			}
		}
	}

	public static void main(String[] args) {
		VendingMenu menu = new VendingMenu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}

	public boolean Init() {
		try {
			inventory = loadInventory(readInventoryFile("vendingmachine.csv"));
		} catch (InventoryLoadException ex) {
			return false;
		}

		return true;
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
				System.out.printf("%s, is not a valid number!\n", priceString);
				throw new InventoryLoadException("NaN");
			}

			Dispenser products = null;

            switch (productType) {
                case "Chip":
                    products = new Dispenser(new Chip(slotId, productName, price), PRODUCTS_PER_SLOT);

                    break;
                case "Candy":
					products = new Dispenser(new Candy(slotId, productName, price), PRODUCTS_PER_SLOT);

                    break;
                case "Drink":
					products = new Dispenser(new Drink(slotId, productName, price), PRODUCTS_PER_SLOT);

                    break;
                case "Gum":
					products = new Dispenser(new Gum(slotId, productName, price), PRODUCTS_PER_SLOT);

                    break;
                default:
                    throw new InvalidProductTypeException();
            }

			loadedInv.put(slotId, products);
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
	//private FileWriter fileWriter;
	private BigDecimal currentBalance;
	private LocalDate dateOfOperation;
	private String selectedProduct;
	private HashMap<String, Dispenser> inventory;

	private static final int PRODUCTS_PER_SLOT = 5;
	private static final MainMenu.Options[] MAIN_MENU_OPTIONS = { MainMenu.Options.DisplayItems, MainMenu.Options.Purchase, MainMenu.Options.Exit, MainMenu.Options.Secret };
	private static final PurchaseMenu.Options[] PURCHASE_MENU_OPTIONS = { PurchaseMenu.Options.FeedMoney, PurchaseMenu.Options.SelectProduct, PurchaseMenu.Options.FinishTransaction};

	private final VendingMenu menu;
}
