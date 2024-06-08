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

    public String format(String[] input) {
        return "";
    }

    public void addColumnWidth(String entryText, int column) {
        List<Integer> columnHeap = COLUMN_HEAPS[column];

        if (columnHeap.isEmpty()) {
            columnHeap.add(entryText.length());
        }


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
