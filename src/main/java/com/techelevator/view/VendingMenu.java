package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Scanner;

public class VendingMenu {

	private final PrintWriter out;
	private final Scanner in;

	public VendingMenu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}

	public IMenuOptions getChoiceFromOptions(IMenuOptions[] options) {
		IMenuOptions choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}

	public String getStringInput() {
		return in.nextLine();
	}

	public int getIntInput() {
		int input = 0;

		try {
			input = Integer.parseInt(in.nextLine());
		} catch (NumberFormatException ex) {
			out.println("Input is not a number!");
			out.flush();
		}

		return input;
	}

	public BigDecimal getMoneyInput() {
		BigDecimal input = BigDecimal.ZERO;

		try {
			input = new BigDecimal(in.nextLine());
		} catch(NumberFormatException ex) {
			out.println("Input is not a number!");
			out.flush();
		}

		return input;
	}

	private IMenuOptions getChoiceFromUserInput(IMenuOptions[] options) {
		IMenuOptions choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	private void displayMenuOptions(IMenuOptions[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i].getOptionText());
		}
		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}
}
