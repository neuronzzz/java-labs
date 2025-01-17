package org.example;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ExcelExporter {
    private final String filePath;
    private final Workbook workbook;
    private final Sheet sheet;

    ExcelExporter(String filePath, String sheetName) {
        this.filePath = filePath;
        this.workbook = new XSSFWorkbook();
        this.sheet = this.workbook.createSheet(sheetName);
    }

    public void exportDataToExcel(Object dataObj) throws IOException, IllegalAccessException {
        int rowNum = rolloutDataToExcel(dataObj, 0);

        try (FileOutputStream fileOut = new FileOutputStream(this.filePath)) {
            this.workbook.write(fileOut);
        }

        System.out.printf("rowNum(生成了多少条数据): " + rowNum);

        this.workbook.close();
    }

    private int rolloutDataToExcel(Object dataObj, int rowNum) throws IllegalAccessException {
        Field[] fields = dataObj.getClass().getDeclaredFields();

        List<Field> noGroupFields = new ArrayList<>();
        TreeMap<ExcelRowGroup, List<Field>> groupMap = new TreeMap<>(new ExcelRowGroupComparator());

        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                ExcelRowGroup group = annotation.group();

                if (group.index().isEmpty()) {
                    noGroupFields.add(field);
                } else {
                    groupMap.putIfAbsent(group, new ArrayList<>());
                    groupMap.get(group).add(field);
                }
            }
        }

        for (Field field : noGroupFields) {
            rowNum = processField(dataObj, field, rowNum);
        }

        for (Map.Entry<ExcelRowGroup, List<Field>> entry : groupMap.entrySet()) {
            ExcelRowGroup group = entry.getKey();
            List<Field> groupFields = entry.getValue();

            Row groupRow = this.sheet.createRow(rowNum++);
            createCell(groupRow, 0, group.index());

            if (!group.labelNext().isEmpty()) {
                Row groupLabelNextRow = sheet.createRow(rowNum++);
                createCell(groupLabelNextRow, 1, group.label());
            }

            for (Field field : groupFields) {
                rowNum = processField(dataObj, field, rowNum);
            }
        }

        return rowNum;
    }

    private int processField(Object dataObj, Field field, int rowNum) throws IllegalAccessException {
        ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
        String label = annotation.label();
        Object value = field.get(dataObj);

        if (value != null) {
            if (value instanceof List) {
                if (!StringUtils.isBlank(label)) {
                    Row listLabelRow = this.sheet.createRow(rowNum++);
                    createCell(listLabelRow, 1, label);
                }

                List<?> list = (List<?>) value;
                for (Object item : list) {
                    rowNum = rolloutDataToExcel(item, rowNum);
                }
            } else if (!isPrimitiveOrWrapper(value.getClass()) && !value.getClass().equals(String.class)) {
                if (!StringUtils.isBlank(label)) {
                    Row listLabelRow = this.sheet.createRow(rowNum++);
                    createCell(listLabelRow, 1, label);
                }

                rowNum = rolloutDataToExcel(value, rowNum);
            } else {
                Row dataRow = sheet.createRow(rowNum++);
                createCell(dataRow, 1, label);
                createCell(dataRow, 2, value.toString());
            }
        }

        return rowNum;
    }

    private static void createCell(Row row, int cellNum, String value) {
        Cell cell = row.createCell(cellNum);
        cell.setCellValue(value);
    }

    private static boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() || type.equals(Boolean.class) || type.equals(Integer.class) || type.equals(Character.class) || type.equals(Byte.class) || type.equals(Short.class) || type.equals(Double.class) || type.equals(Long.class) || type.equals(Float.class);
    }

    private static String timeStamp() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss");
        return currentDateTime.format(formatter);
    }

    public static void main(String[] args) throws IOException, IllegalAccessException {
        // 创建示例数据
        ClassA classA = new ClassA();
        classA.setAttribute1("ClassA attribute1");

        // 导出到 Excel
        ExcelExporter ee = new ExcelExporter(String.format("output-" + timeStamp() + ".xlsx"), "sheet1");
        ee.exportDataToExcel(classA);
    }
}



