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
        ClassA classA = new ClassA();
        // 为 attribute1 和 attribute2 赋值
        classA.setAttribute1("Value for attribute1");
        classA.setAttribute2("Value for attribute2");

        // 初始化 ClassB 对象
        ClassB classB = new ClassB();
        classB.setAttribute1("Value for ClassB attribute1");
        classB.setAttribute2("Value for ClassB attribute2");

        ClassC classC = new ClassC();
        classB.setAttribute1("Value for ClassC attribute1");
        classB.setAttribute2("Value for ClassC attribute2");
        classB.setClassC(classC);

        List<ClassC> classCList = new ArrayList<>();
        ClassC classC2 = new ClassC();
        classC2.setAttribute1("Value for ClassC2 attribute1");
        classC2.setAttribute2("Value for ClassC2 attribute2");
        classCList.add(classC2);
        classB.setClassCList(classCList);

        // 初始化 ClassBList
        List<ClassB> classBList = new ArrayList<>();

        ClassB classB2 = new ClassB();
        classB2.setAttribute1("Value for ClassB2 attribute1");
        classB2.setAttribute2("Value for ClassB2 attribute2");

        ClassC classC3 = new ClassC();
        classC3.setAttribute1("Value for ClassC3 attribute1");
        classC3.setAttribute2("Value for ClassC3 attribute2");

        classB2.setClassC(classC3);
        List<ClassC> classCList2 = new ArrayList<>();
        ClassC classC4 = new ClassC();
        classC4.setAttribute1("Value for ClassC4 attribute1");
        classC4.setAttribute2("Value for ClassC4 attribute2");
        classCList2.add(classC4);
        classB2.setClassCList(classCList2);

        // 导出到 Excel
        ExcelExporter ee = new ExcelExporter(String.format("output-" + timeStamp() + ".xlsx"), "sheet1");
        ee.exportDataToExcel(classA);
    }
}



