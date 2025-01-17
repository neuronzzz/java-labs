package org.example;

import lombok.Data;

import java.util.List;

@Data
public class ClassB {
    @ExcelColumn(label = "ClassB - attribute1")
    private String attribute1;

    @ExcelColumn(label = "ClassB - attribute2", group = @ExcelRowGroup(index = "1", label = "group label", labelNext = "group label next"))
    private String attribute2;

    @ExcelColumn(label = "ClassB - classC with ClassC")
    private ClassC classC;

    @ExcelColumn(label = "ClassB - classCList with List<ClassC>")
    private List<ClassC> classCList;
}
