package com.techelevator.format;

import java.util.ArrayList;
import java.util.List;

public class ColumnTextFormatter {
    public ColumnTextFormatter(int numberOfColumns) {
        this.NUMBER_OF_COLUMNS = numberOfColumns;

        COLUMN_WIDTHS = new int[NUMBER_OF_COLUMNS];
        COLUMN_HEAPS = new List[NUMBER_OF_COLUMNS];

        for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
            COLUMN_HEAPS[i] = new ArrayList<>();
        }
    }

    public String format(String[] columnEntries, char separatorCharacter, int padding) {
        StringBuilder formattedString = new StringBuilder();
        if (columnEntries.length > NUMBER_OF_COLUMNS) return formattedString.toString();

        String token;
        int printedLength, columnWidth;

        for (int i = 0; i < columnEntries.length; i++) {
            token = columnEntries[i];

            printedLength = token.length();
            columnWidth = COLUMN_HEAPS[i].get(0);

            formattedString.append(" ".repeat(Math.max(0, padding)));
            formattedString.append(token);
            formattedString.append(" ".repeat(Math.max(0, padding)));

            formattedString.append(" ".repeat(Math.max(0, columnWidth - printedLength)));
            formattedString.append("|");
        }

        return formattedString.toString();
    }

    public void addColumnWidth(String entryText, int column) {
        List<Integer> columnHeap = COLUMN_HEAPS[column];

        if (columnHeap.isEmpty()) {
            columnHeap.add(entryText.length());
        }

        columnHeap.add(entryText.length());
        maxHeapify(columnHeap, columnHeap.size() - 1);
    }

    public int getMaxColumnWidth(int column) {
        if (column >= NUMBER_OF_COLUMNS) return -1;

        return COLUMN_HEAPS[column].get(0);
    }

    private void maxHeapify(List<Integer> heap, int currentIndex) {
        if (currentIndex <= 0) return;

        int parentIndex = (currentIndex - 1) / 2;
        Integer parentValue = heap.get(parentIndex);
        Integer childValue = heap.get(currentIndex);

        if (childValue > parentValue) {
            heap.set(parentIndex, childValue);
            heap.set(currentIndex, parentValue);

            maxHeapify(heap, parentIndex);
        }
    }

    private final int NUMBER_OF_COLUMNS;
    private final int[] COLUMN_WIDTHS;
    private final List<Integer>[] COLUMN_HEAPS;
}
