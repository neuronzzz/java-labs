package org.example;

import java.util.Comparator;

// 自定义比较器，用于比较 ExcelRowGroup 注解的 index
public class ExcelRowGroupComparator implements Comparator<ExcelRowGroup> {
    @Override
    public int compare(ExcelRowGroup o1, ExcelRowGroup o2) {
        return o1.index().compareTo(o2.index());  // 按照 index 字段进行排序
    }
}
