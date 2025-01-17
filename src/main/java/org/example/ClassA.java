package org.example;

import lombok.Data;

import java.util.List;

@Data
public class ClassA {

    @ExcelColumn(label = "Attribute 1", group = @ExcelRowGroup(index = "1"))
    private String attribute1;
}
