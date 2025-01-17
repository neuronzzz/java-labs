package org.example;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelRowCategory {
    String index() default StringUtils.EMPTY;

    String label() default StringUtils.EMPTY;

    String labelNext() default StringUtils.EMPTY;
}

