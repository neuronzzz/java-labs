package org.example;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
    String label();      // 属性的标签
    String group();      // 属性所属的分组
    String groupName() default "";  // 可选的 Group Name，默认为空
}
