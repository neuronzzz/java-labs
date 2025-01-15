package org.example;

import lombok.Data;

import java.util.List;

@Data
public class ClassA {
    @ExcelColumn(label = "Attribute 1", group = "1")
    private String attribute1;

    @ExcelColumn(label = "String List", group = "2")
    private List<String> stringList;

    @ExcelColumn(label = "ClassB List", group = "2")
    private List<ClassB> classBList;

    @ExcelColumn(label = "Attribute 2", group = "1.1")
    private String attribute2;
}
