package org.example;

import java.util.Comparator;

public class ExcelRowCategoryComparator implements Comparator<ExcelRowCategory> {
    @Override
    public int compare(ExcelRowCategory o1, ExcelRowCategory o2) {
        return o1.index().compareTo(o2.index());
    }
}
