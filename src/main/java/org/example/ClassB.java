package org.example;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClassB {
    @ExcelField(label = "ClassB - attribute1")
    private String attribute1;

    @ExcelField(label = "ClassB - attribute2")
    private String attribute2;

    @ExcelField(label = "ClassB - classC with ClassC")
    private ClassC classC;

    @ExcelField(label = "ClassB - classCList with List<ClassC>")
    private List<ClassC> classCList;
}
