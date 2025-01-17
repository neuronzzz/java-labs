package org.example;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClassC {
    @ExcelField(label = "ClassC - attribute1")
    private String attribute1;

    @ExcelField(label = "ClassC - attribute2")
    private String attribute2;

    @ExcelField(label = "ClassC - classD with ClassD")
    private ClassD classD;

    @ExcelField(label = "ClassC - classDList with List<ClassD>")
    private List<ClassD> classDList;
}
