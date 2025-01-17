package org.example;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClassA {

    @ExcelColumn(label = "ClassA - attribute1", group = @ExcelRowGroup(index = "1", label = "group label", labelNext = "group label next"))
    private String attribute1;

    @ExcelColumn(label = "ClassA - attribute2")
    private String attribute2;

    @ExcelColumn(label = "ClassA - classB with ClassB", group = @ExcelRowGroup(index = "1.2", label = "group label", labelNext = "group label next"))
    private ClassB classB;

    @ExcelColumn(label = "ClassA - classBList with List<ClassB>", group = @ExcelRowGroup(index = "2.1", label = "group label", labelNext = "group label next"))
    private List<ClassB> classBList;

}
