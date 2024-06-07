package com.techelevator.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MenuTest {

	private ByteArrayOutputStream output;

	@Before
	public void setup() {
		output = new ByteArrayOutputStream();
	}

	@Test
	public void displays_a_list_of_menu_options_and_prompts_user_to_make_a_choice() {
		MainMenu.Options[] options = new MainMenu.Options[] { MainMenu.Options.DisplayItems, MainMenu.Options.Purchase };
		VendingMenu menu = getMenuForTesting();

		menu.getChoiceFromOptions(options);

		String expected = System.lineSeparator() + "1) " + options[0].toString() + System.lineSeparator() + "2) " + options[1].toString() +
				System.lineSeparator() + System.lineSeparator() + "Please choose an option >>> ";
		Assert.assertEquals(expected, output.toString());
	}

	@Test
	public void returns_object_corresponding_to_user_choice() {
		PurchaseMenu.Options expected = PurchaseMenu.Options.FinishTransaction;
		PurchaseMenu.Options[] options = new PurchaseMenu.Options[] { PurchaseMenu.Options.FeedMoney, expected, PurchaseMenu.Options.FinishTransaction };
		VendingMenu menu = getMenuForTestingWithUserInput("2" + System.lineSeparator());

		PurchaseMenu.Options result = (PurchaseMenu.Options) menu.getChoiceFromOptions(options);

		Assert.assertEquals(expected, result);
	}

	@Test
	public void redisplays_menu_if_user_does_not_choose_valid_option() {
		MainMenu.Options[] options = new MainMenu.Options[] { MainMenu.Options.DisplayItems, MainMenu.Options.Purchase, MainMenu.Options.Exit };
		VendingMenu menu = getMenuForTestingWithUserInput("4" + System.lineSeparator() + "1" + System.lineSeparator());

		menu.getChoiceFromOptions(options);

		String menuDisplay = System.lineSeparator() + "1) " + options[0].toString() + System.lineSeparator() + "2) " + options[1].toString() + System.lineSeparator() + "3) "
				+ options[2].toString() + System.lineSeparator() + System.lineSeparator() + "Please choose an option >>> ";

		String expected = menuDisplay + System.lineSeparator() + "*** 4 is not a valid option ***" + System.lineSeparator() + System.lineSeparator() + menuDisplay;

		Assert.assertEquals(expected, output.toString());
	}

	@Test
	public void redisplays_menu_if_user_chooses_option_less_than_1() {
		PurchaseMenu.Options[] options = new PurchaseMenu.Options[] { PurchaseMenu.Options.FinishTransaction, PurchaseMenu.Options.SelectProduct, PurchaseMenu.Options.FeedMoney };
		VendingMenu menu = getMenuForTestingWithUserInput("0" + System.lineSeparator() + "1" + System.lineSeparator());

		menu.getChoiceFromOptions(options);

		String menuDisplay = System.lineSeparator() + "1) " + options[0].toString() + System.lineSeparator() + "2) " + options[1].toString() + System.lineSeparator() + "3) "
				+ options[2].toString() + System.lineSeparator() + System.lineSeparator() + "Please choose an option >>> ";

		String expected = menuDisplay + System.lineSeparator() + "*** 0 is not a valid option ***" + System.lineSeparator() + System.lineSeparator() + menuDisplay;

		Assert.assertEquals(expected, output.toString());
	}

	@Test
	public void redisplays_menu_if_user_enters_garbage() {
		MainMenu.Options[] options = new MainMenu.Options[] { MainMenu.Options.Secret, MainMenu.Options.DisplayItems, MainMenu.Options.Exit };
		VendingMenu menu = getMenuForTestingWithUserInput("Mickey Mouse" + System.lineSeparator() + "1" + System.lineSeparator());

		menu.getChoiceFromOptions(options);

		String menuDisplay = System.lineSeparator() + "1) " + options[0].toString() + System.lineSeparator() + "2) " + options[1].toString() + System.lineSeparator() + "3) "
				+ options[2].toString() + System.lineSeparator() + System.lineSeparator() + "Please choose an option >>> ";

		String expected = menuDisplay + System.lineSeparator() + "*** Mickey Mouse is not a valid option ***" + System.lineSeparator() + System.lineSeparator() + menuDisplay;

		Assert.assertEquals(expected, output.toString());
	}

	private VendingMenu getMenuForTestingWithUserInput(String userInput) {
		ByteArrayInputStream input = new ByteArrayInputStream(String.valueOf(userInput).getBytes());
		return new VendingMenu(input, output);
	}

	private VendingMenu getMenuForTesting() {
		return getMenuForTestingWithUserInput("1" + System.lineSeparator());
	}
}
