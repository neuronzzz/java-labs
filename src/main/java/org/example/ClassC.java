package org.example;

import lombok.Data;

import java.util.List;

@Data
public class ClassC {
    @ExcelColumn(label = "ClassC - attribute1")
    private String attribute1;

    @ExcelColumn(label = "ClassC - attribute2")
    private String attribute2;

    @ExcelColumn(label = "ClassC - classD with ClassD")
    private ClassD classD;

    @ExcelColumn(label = "ClassC - classDList with List<ClassD>")
    private List<ClassD> classDList;
}
