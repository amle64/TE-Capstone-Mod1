package com.techelevator.test;

import com.techelevator.format.ColumnTextFormatter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColumnTextFormatterTest {
    @Before
    public void Init() throws NoSuchFieldException, NoSuchMethodException {
        formatter = new ColumnTextFormatter(numColumns);
        internal_ColumnWidths = formatter.getClass().getDeclaredField("COLUMN_WIDTHS");
        internal_ColumnHeaps = formatter.getClass().getDeclaredField("COLUMN_HEAPS");
        internal_HeapifyMethod = formatter.getClass().getDeclaredMethod("maxHeapify", List.class, int.class);

        testTextInputs = new ArrayList<>();
        productInputs = new String[] { "A1|Potato Crisps|3.05", "A2|Stackers|1.45", "A3|Grain Waves|2.75", "A4|Cloud Popcorn|3.65", "B1|Moonpie|1.80" };
        for (String productInput : productInputs) {
            testTextInputs.add(productInput.split("\\|"));
        }

        internal_ColumnWidths.setAccessible(true);
        internal_ColumnHeaps.setAccessible(true);
        internal_HeapifyMethod.setAccessible(true);
    }

    @Test
    public void constructor_assigns_arrays_correctly() throws IllegalAccessException {
        int[] columnWidths = (int[])internal_ColumnWidths.get(formatter);
        List<Integer>[] columnHeaps = (List[])internal_ColumnHeaps.get(formatter);

        Assert.assertEquals(numColumns, columnHeaps.length);

        for (List<Integer> heap : columnHeaps) {
            Assert.assertNotNull(heap);
        }
    }

    @Test
    public void creates_formatted_string_given_column_tokens() {
        for (String[] testInput : testTextInputs) {
            for (int i = 0; i < testInput.length; i++) {
                String testToken = testInput[i];
                formatter.addColumnWidth(testToken, i);
            }
        }

        String result = formatter.format(new String[]{ "B1", "Moonpie", "1.80" }, '|', 1);
        Assert.assertEquals(" B1 | Moonpie       | 1.80 |", result);

        result = formatter.format(new String[]{ "B1", "Moonpie", "1.80" }, '|', 4);
        Assert.assertEquals("    B1    |    Moonpie          |    1.80    |", result);

        result = formatter.format(new String[]{ "A4", "Cloud Popcorn", "3.65" }, '|', 2);
        Assert.assertEquals("  A4  |  Cloud Popcorn  |  3.65  |", result);
    }

    @Test
    public void calculate_column_width_given_text_inputs() {
        for (String[] testInput : testTextInputs) {
            for (int i = 0; i < testInput.length; i++) {
                String testToken = testInput[i];
                formatter.addColumnWidth(testToken, i);
            }
        }

        Assert.assertEquals(2, formatter.getMaxColumnWidth(0));
        Assert.assertEquals(13, formatter.getMaxColumnWidth(1));
        Assert.assertEquals(4, formatter.getMaxColumnWidth(2));
        Assert.assertEquals(-1, formatter.getMaxColumnWidth(3));
    }

    @Test
    public void max_heapify_sets_0_index_max_value() throws IllegalAccessException, InvocationTargetException {

        List<Integer> testHeap = new ArrayList<>(Arrays.asList(2, 3));
        internal_HeapifyMethod.invoke(formatter, testHeap, testHeap.size() - 1);
        Assert.assertArrayEquals(new Integer[]{ 3, 2 }, testHeap.toArray());

        testHeap = new ArrayList<>(Arrays.asList(31, 18, 17, 10, 2, 15, 9, 5, 12));
        internal_HeapifyMethod.invoke(formatter, testHeap, testHeap.size() - 1);
        Assert.assertArrayEquals(new Integer[]{ 31, 18, 17, 12, 2, 15, 9, 5, 10 }, testHeap.toArray());

        testHeap = new ArrayList<>(Arrays.asList(31, 18, 17, 10, 2, 15, 9, 5, 40));
        internal_HeapifyMethod.invoke(formatter, testHeap, testHeap.size() - 1);
        Assert.assertArrayEquals(new Integer[]{ 40, 31, 17, 18, 2, 15, 9, 5, 10 }, testHeap.toArray());

        testHeap = new ArrayList<>(Arrays.asList(31, 18, 17, 10, 2, 15, 9, 5, 20));
        internal_HeapifyMethod.invoke(formatter, testHeap, testHeap.size() - 1);
        Assert.assertArrayEquals(new Integer[]{ 31, 20, 17, 18, 2, 15, 9, 5, 10 }, testHeap.toArray());
    }

    private ColumnTextFormatter formatter;
    private final int numColumns = 3;

    private String[] productInputs;
    private List<String[]> testTextInputs;

    private Field internal_ColumnWidths;
    private Field internal_ColumnHeaps;
    private Method internal_HeapifyMethod;
}
