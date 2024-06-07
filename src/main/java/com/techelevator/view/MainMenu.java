package com.techelevator.view;

public class MainMenu {
    public enum Options implements IMenuOptions {
        DisplayItems(0),
        Purchase(1),
        Exit(2),
        Secret(3);

        public final int value;

        @Override
        public String getOptionText() {
            return OPTIONS_TEXT[value];
        }

        @Override
        public String toString() {
            return OPTIONS_TEXT[value];
        }

        private Options(int value) {
            this.value = value;
        }
    }

    private static final String[] OPTIONS_TEXT = new String[]{ "Display Vending Machine Items", "Purchase", "Exit", "*Sales Report" };
}
