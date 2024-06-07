package com.techelevator;

import com.sun.tools.javac.Main;
import com.techelevator.exceptions.InventoryLoadException;
import com.techelevator.view.MainMenu;
import com.techelevator.view.PurchaseMenu;
import com.techelevator.view.VendingMenu;

import java.math.BigDecimal;

public class VendingMachineCLI {
	public VendingMachineCLI(VendingMenu menu) throws InventoryLoadException {
		this.menu = menu;
		this.vendingMachine = new VendingMachine(5, System.out,"vendingMachine.csv");
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
					PurchaseMenu.Options purchaseChoice = (PurchaseMenu.Options)menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);

					switch(purchaseChoice){
						case FeedMoney:
							//vendingMachine.addFunds(BigDecimal.valueOf(10.99));
							break;
						case SelectProduct:
							//vendingMachine.selectProduct("A1");
							break;
						case FinishTransaction:
							break;
					}

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

		try {
			VendingMachineCLI cli = new VendingMachineCLI(menu);
			cli.run();
		} catch (InventoryLoadException ex) {
			System.out.println(ex.getMessage());
		}
	}

	private static final MainMenu.Options[] MAIN_MENU_OPTIONS = { MainMenu.Options.DisplayItems, MainMenu.Options.Purchase, MainMenu.Options.Exit, MainMenu.Options.Secret };
	private static final PurchaseMenu.Options[] PURCHASE_MENU_OPTIONS = { PurchaseMenu.Options.FeedMoney, PurchaseMenu.Options.SelectProduct, PurchaseMenu.Options.FinishTransaction};

	private final VendingMenu menu;
	private final VendingMachine vendingMachine;
}
