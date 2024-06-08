package com.techelevator;

import com.sun.tools.javac.Main;
import com.techelevator.exceptions.InventoryLoadException;
import com.techelevator.view.MainMenu;
import com.techelevator.view.PurchaseMenu;
import com.techelevator.view.VendingMenu;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Scanner;

public class VendingMachineCLI {
	public VendingMachineCLI(VendingMenu menu) throws InventoryLoadException {
		this.menu = menu;
		vendingMachine = new VendingMachine(5, System.out,"vendingMachine.csv");
	}

	public void run() {
		boolean running = true;
		while (running) {
			MainMenu.Options choice = (MainMenu.Options)menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			switch (choice) {
				case DisplayItems:
					vendingMachine.displayItems();
					break;
				case Purchase:
					handleTransactions();
					break;
				case Exit:
					running = false;
					continue;
				case Secret:
					vendingMachine.printSalesReport();
					break;
			}
		}
	}

	public static <InputStream> void main(String[] args) {
		VendingMenu menu = new VendingMenu(System.in, System.out);

		try {
			VendingMachineCLI cli = new VendingMachineCLI(menu);
			cli.run();
		} catch (InventoryLoadException ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void handleTransactions() {
		boolean inTransaction = true;
		while (inTransaction) {
			PurchaseMenu.Options purchaseChoice = (PurchaseMenu.Options)menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);

			switch(purchaseChoice){
				case FeedMoney:
					System.out.print("Please type enter the amount of $$ you would like to add to your balance: ");
					vendingMachine.addFunds(menu.getMoneyInput());
					break;
				case SelectProduct:
					vendingMachine.displayItems();
					System.out.printf("\nPlease enter the slot ID you would like to purchase today: ");
					vendingMachine.selectProduct(menu.getStringInput());
					break;
				case FinishTransaction:
					inTransaction = false;
					vendingMachine.finishTransaction();
					break;
			}
		}
	}

	private static final MainMenu.Options[] MAIN_MENU_OPTIONS = { MainMenu.Options.DisplayItems, MainMenu.Options.Purchase, MainMenu.Options.Exit, MainMenu.Options.Secret };
	private static final PurchaseMenu.Options[] PURCHASE_MENU_OPTIONS = { PurchaseMenu.Options.FeedMoney, PurchaseMenu.Options.SelectProduct, PurchaseMenu.Options.FinishTransaction};

	private final VendingMenu menu;
	private final VendingMachine vendingMachine;
}
