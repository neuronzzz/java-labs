package org.example;

import lombok.Data;

@Data
public class ClassB {
    @ExcelColumn(label = "Attribute B1", group = "2.1")
    private String attributeB1;

    @ExcelColumn(label = "Attribute B2", group = "2.1")
    private String attributeB2;
}
