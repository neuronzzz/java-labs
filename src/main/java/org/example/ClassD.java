package org.example;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassD {
    @ExcelField(label = "ClassD - attribute1")
    private String attribute1;

    @ExcelField(label = "ClassD - attribute2")
    private String attribute2;
}
