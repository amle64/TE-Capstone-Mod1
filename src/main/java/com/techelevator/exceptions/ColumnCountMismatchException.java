package com.techelevator.exceptions;

public class ColumnCountMismatchException extends RuntimeException {
    public ColumnCountMismatchException() {
        super("Column entries exceed maximum column count of this formatter!");
    }
}
