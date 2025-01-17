package org.example;

import lombok.Data;

import java.util.List;

@Data
public class ClassD {
    @ExcelColumn(label = "ClassD - attribute1")
    private String attribute1;

    @ExcelColumn(label = "ClassD - attribute2")
    private String attribute2;
}
