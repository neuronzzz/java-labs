package org.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
    String label() default ""; // 对应的列名标签

    String group() default ""; // 分组标签，值为 1, 2, 1.1, 2.2 等
}