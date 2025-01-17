package org.example;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClassA {

    @ExcelField(label = "ClassA - attribute1", group = @ExcelRowCategory(index = "1", label = "group label", labelNext = "group label next"))
    private String attribute1;

    @ExcelField(label = "ClassA - attribute2")
    private String attribute2;

    @ExcelField(label = "ClassA - classB with ClassB", group = @ExcelRowCategory(index = "1.2", label = "group label", labelNext = "group label next"))
    private ClassB classB;

    @ExcelField(label = "ClassA - classBList with List<ClassB>", group = @ExcelRowCategory(index = "2.1", label = "group label", labelNext = "group label next"))
    private List<ClassB> classBList;

}
