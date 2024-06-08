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
    public void max_heapify_sets_0_index_max_value() throws IllegalAccessException, InvocationTargetException {

        List<Integer> testHeap = new ArrayList<>(Arrays.asList(2, 3));
        internal_HeapifyMethod.invoke(formatter, testHeap, testHeap.size() - 1);
        Assert.assertArrayEquals(new Integer[]{ 3, 2 }, testHeap.toArray());
    }

    private ColumnTextFormatter formatter;
    private final int numColumns = 3;

    private Field internal_ColumnWidths;
    private Field internal_ColumnHeaps;
    private Method internal_HeapifyMethod;
}
