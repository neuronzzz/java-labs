package org.example;

import java.util.Comparator;

public class ExcelRowGroupComparator implements Comparator<ExcelRowGroup> {
    @Override
    public int compare(ExcelRowGroup o1, ExcelRowGroup o2) {
        return o1.index().compareTo(o2.index());
    }
}
