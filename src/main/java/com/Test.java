package com;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Test {

    public static void main(String[] args) {
        XSSFWorkbook xssfWorkbook = getExcelTemplate();
        if (xssfWorkbook != null) {
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            XSSFCell cell = sheet.getRow(0).getCell(0);
            cell.setCellValue("ABC-5566");
            exportExcelFile(xssfWorkbook);
            System.out.println("done");
        }
    }

    public static XSSFWorkbook getExcelTemplate() {
        InputStream inputFile = null;
        XSSFWorkbook xssfWorkbook = null;
        try {
            inputFile = Test.class.getResourceAsStream("/test0406.xlsx");
            // InputStream inputStream = getClass().getResourceAsStream("/autoCustomReportTemplate.pptx");
            if (inputFile != null) {
                xssfWorkbook = new XSSFWorkbook(inputFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputFile != null) {
                    inputFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return xssfWorkbook;
    }

    public static void exportExcelFile(XSSFWorkbook xssfWorkbook) {
        FileOutputStream outputFile = null;
        try {
            outputFile = new FileOutputStream("C:\\report\\apiResponse\\test0408.xlsx");
            xssfWorkbook.write(outputFile);
            xssfWorkbook.close();
            outputFile.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputFile != null) {
                    outputFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
