package org.example;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {
    String label() default "";

    ExcelRowCategory group() default @ExcelRowCategory;
}
