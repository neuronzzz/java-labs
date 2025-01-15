package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class ExcelExporter {

    public static void exportToExcel(Object data, String filePath) throws IOException, IllegalAccessException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        int rowNum = 0;

        // 获取所有字段并按 group 分组
        Field[] fields = data.getClass().getDeclaredFields();
        Map<String, List<Field>> groupMap = new TreeMap<>();

        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                String group = annotation.group();
                groupMap.putIfAbsent(group, new ArrayList<>());
                groupMap.get(group).add(field);
            }
        }

        // 遍历每个分组
        for (Map.Entry<String, List<Field>> entry : groupMap.entrySet()) {
            String groupKey = entry.getKey();
            List<Field> groupFields = entry.getValue();

            // 输出 Group 行
            Row groupRow = sheet.createRow(rowNum++);
            createCell(groupRow, 0, groupKey);

            // 输出 Group Name（如果存在）
            Optional<String> groupName = getGroupName(groupFields);
            if (groupName.isPresent()) {
                createCell(groupRow, 1, groupName.get());
            }

            // 遍历组内字段
            for (Field field : groupFields) {
                field.setAccessible(true);
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                String label = annotation.label();
                Object value = field.get(data);

                if (value instanceof List) {
                    // 处理 List 类型
                    Row listLabelRow = sheet.createRow(rowNum++);
                    createCell(listLabelRow, 1, label);

                    List<?> list = (List<?>) value;
                    for (Object item : list) {
                        if (item instanceof String) {
                            Row listItemRow = sheet.createRow(rowNum++);
                            createCell(listItemRow, 2, item.toString());
                        } else {
                            // 处理嵌套对象
                            processNestedObject(item, sheet, rowNum);
                        }
                    }
                } else {
                    // 处理普通字段
                    Row dataRow = sheet.createRow(rowNum++);
                    createCell(dataRow, 1, label);
                    createCell(dataRow, 2, value != null ? value.toString() : "");
                }
            }
        }

        // 写入 Excel 文件
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }

    private static Optional<String> getGroupName(List<Field> groupFields) {
        for (Field field : groupFields) {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (!annotation.groupName().isEmpty()) {
                return Optional.of(annotation.groupName());
            }
        }
        return Optional.empty();
    }

    private static void processNestedObject(Object item, Sheet sheet, int rowNum) throws IllegalAccessException {
        Field[] nestedFields = item.getClass().getDeclaredFields();
        for (Field nestedField : nestedFields) {
            if (nestedField.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn annotation = nestedField.getAnnotation(ExcelColumn.class);
                String nestedLabel = annotation.label();
                nestedField.setAccessible(true);
                Object nestedValue = nestedField.get(item);

                Row nestedRow = sheet.createRow(rowNum++);
                createCell(nestedRow, 1, nestedLabel);
                createCell(nestedRow, 2, nestedValue != null ? nestedValue.toString() : "");
            }
        }
    }

    private static void createCell(Row row, int cellNum, String value) {
        Cell cell = row.createCell(cellNum);
        cell.setCellValue(value);
    }

    public static void main(String[] args) throws IOException, IllegalAccessException {
        // 创建示例数据
        ClassB classB1 = new ClassB();
        classB1.setAttributeB1("Value B1-1");
        classB1.setAttributeB2("Value B2-1");

        ClassB classB2 = new ClassB();
        classB2.setAttributeB1("Value B1-2");
        classB2.setAttributeB2("Value B2-2");

        ClassA classA = new ClassA();
        classA.setAttribute1("Attribute 1 Value");
        classA.setAttribute2("Attribute 2 Value");
        classA.setStringList(Arrays.asList("Value 1", "Value 2", "Value 3"));
        classA.setClassBList(Arrays.asList(classB1, classB2));

        // 导出到 Excel
        exportToExcel(classA, "output.xlsx");
    }
}



