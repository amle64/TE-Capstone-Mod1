package com.techelevator.view;

public class PurchaseMenu {
    public enum Options implements IMenuOptions {
        FeedMoney(0),
        SelectProduct(1),
        FinishTransaction(2);

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

    private static final String[] OPTIONS_TEXT = new String[]{ "Feed Money", "Select Product", "Finish Transaction" };
}
