package com.techelevator.exceptions;

public class InvalidProductTypeException extends InventoryLoadException {
    public InvalidProductTypeException() {
        super("Input Product type is invalid!");
    }
}
