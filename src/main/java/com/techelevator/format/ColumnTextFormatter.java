package com.techelevator.format;

import com.techelevator.exceptions.ColumnCountMismatchException;

import java.util.ArrayList;
import java.util.List;

public class ColumnTextFormatter {
    public ColumnTextFormatter(int numberOfColumns) {
        this.NUMBER_OF_COLUMNS = numberOfColumns;
        COLUMN_HEAPS = initHeaps();
    }

    public ColumnTextFormatter(int numberOfColumns, String[] columnTitles) throws ColumnCountMismatchException {
        if (columnTitles.length != numberOfColumns) throw new ColumnCountMismatchException();

        this.NUMBER_OF_COLUMNS = numberOfColumns;
        COLUMN_HEAPS = initHeaps();

        for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
            String columnTitle = columnTitles[i];
            addColumnWidth(columnTitle, i);
        }
    }

    public String format(String[] columnEntries, char separatorCharacter, int padding) throws ColumnCountMismatchException {
        StringBuilder formattedString = new StringBuilder();
        if (columnEntries.length > NUMBER_OF_COLUMNS) throw new ColumnCountMismatchException();

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
            formattedString.append(separatorCharacter);
        }

        return formattedString.toString();
    }

    public void addColumnWidth(String entryText, int column) {
        List<Integer> columnHeap = COLUMN_HEAPS[column];

        if (columnHeap.isEmpty()) {
            columnHeap.add(entryText.length());
            calculateRowWidth();
            return;
        }

        columnHeap.add(entryText.length());
        maxHeapify(columnHeap, columnHeap.size() - 1);
    }

    public int getMaxColumnWidth(int column) {
        if (column >= NUMBER_OF_COLUMNS) return -1;

        return COLUMN_HEAPS[column].get(0);
    }

    public int getRowWidth(int padding) {
        return padding * 2 * NUMBER_OF_COLUMNS + totalRowTextCount + NUMBER_OF_COLUMNS;
    }

    private List[] initHeaps() {
        List<Integer>[] heaps = new List[NUMBER_OF_COLUMNS];

        for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
            heaps[i] = new ArrayList<>();
        }

        return heaps;
    }

    private void calculateRowWidth() {
        totalRowTextCount = 0;
        for (List<Integer> columnHeap : COLUMN_HEAPS) {
            if (columnHeap.isEmpty()) continue;

            totalRowTextCount += columnHeap.get(0);
        }
    }

    private void maxHeapify(List<Integer> heap, int currentIndex) {
        if (currentIndex <= 0) {
            calculateRowWidth();
            return;
        }

        int parentIndex = (currentIndex - 1) / 2;
        Integer parentValue = heap.get(parentIndex);
        Integer childValue = heap.get(currentIndex);

        if (childValue > parentValue) {
            heap.set(parentIndex, childValue);
            heap.set(currentIndex, parentValue);

            maxHeapify(heap, parentIndex);
        }
    }

    private int totalRowTextCount;
    private final int NUMBER_OF_COLUMNS;
    private final List<Integer>[] COLUMN_HEAPS;
}
