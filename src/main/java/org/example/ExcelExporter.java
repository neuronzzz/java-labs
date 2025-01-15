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
        // 通过反射获取 ClassA 的所有字段
        Field[] fields = data.getClass().getDeclaredFields();

        // 使用 TreeMap 按照分组顺序排序字段
        Map<String, List<Field>> groupMap = new TreeMap<>();

        // 将字段按 group 分组
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                String group = annotation.group();
                groupMap.putIfAbsent(group, new ArrayList<>());
                groupMap.get(group).add(field);
            }
        }

        // 遍历每个分组，插入空行并输出分组标签
        for (Map.Entry<String, List<Field>> entry : groupMap.entrySet()) {
            // 输出分组标签（空行 + 标签）
            Row groupRow = sheet.createRow(rowNum++);
            createCell(groupRow, 0, "Group " + entry.getKey());

            // 输出该组的字段数据
            for (Field field : entry.getValue()) {
                if (field.isAnnotationPresent(ExcelColumn.class)) {
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    String label = annotation.label();
                    field.setAccessible(true);
                    Object fieldValue = field.get(data);

                    // 插入数据行
                    Row dataRow = sheet.createRow(rowNum++);
                    createCell(dataRow, 0, label);
                    createCell(dataRow, 1, fieldValue != null ? fieldValue.toString() : "");

                    // 如果是 List 类型，逐个元素处理
                    if (fieldValue instanceof List<?>) {
                        List<?> list = (List<?>) fieldValue;
                        for (Object item : list) {
                            Row listRow = sheet.createRow(rowNum++);
                            createCell(listRow, 0, "List Item");
                            createCell(listRow, 1, item != null ? item.toString() : "");
                        }
                    }
                }
            }
        }

        // 写入文件
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }

        workbook.close();
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
        classA.setStringList(Arrays.asList("Value 1", "Value 2", "Value 3"));
        classA.setClassBList(Arrays.asList(classB1, classB2));
        classA.setAttribute2("Attribute 2 Value");

        // 导出到 Excel
        exportToExcel(classA, "output_with_groups.xlsx");
    }
}
