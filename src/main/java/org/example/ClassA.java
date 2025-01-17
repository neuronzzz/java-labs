package org.example;

import lombok.Data;

import java.util.List;

@Data
public class ClassA {

    @ExcelColumn(label = "ClassA - attribute1", group = @ExcelRowGroup(index = "1", label = "group label", labelNext = "group label next"))
    private String attribute1;

    @ExcelColumn(label = "ClassA - attribute2", group = @ExcelRowGroup(index = "1", label = "group label", labelNext = "group label next"))
    private String attribute2;

    @ExcelColumn(label = "ClassA - classB with ClassB")
    private ClassB classB;

    @ExcelColumn(label = "ClassA - classBList with List<ClassB>")
    private List<ClassB> classBList;

}
