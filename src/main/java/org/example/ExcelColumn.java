package org.example;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
    String label() default "" ;

    ExcelRowGroup group() default @ExcelRowGroup;
}
