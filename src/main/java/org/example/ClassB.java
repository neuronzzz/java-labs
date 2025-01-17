package org.example;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClassB {
    @ExcelColumn(label = "ClassB - attribute1")
    private String attribute1;

    @ExcelColumn(label = "ClassB - attribute2")
    private String attribute2;

    @ExcelColumn(label = "ClassB - classC with ClassC")
    private ClassC classC;

    @ExcelColumn(label = "ClassB - classCList with List<ClassC>")
    private List<ClassC> classCList;
}
