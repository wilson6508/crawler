package com.service.test;

import com.pojo.dto.CrawlerApiResponseBean;
import com.tool.LocalDateTool;
import com.tool.ObjectTool;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Service
public class TestOneService {

    @Autowired
    private ObjectTool objectTool;
    @Resource
    LocalDateTool localDateTool;

    public CrawlerApiResponseBean testOne(Object parameter) {
        CrawlerApiResponseBean responseBean = objectTool.getErrorRep();
        try {
            System.out.println(parameter.toString());
            responseBean = objectTool.getSuccessRep();
            responseBean.setResult("KKK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseBean;
    }

    public void demo() {
        try {
            String excelPath = "C:\\Users\\wilsonhuang\\Desktop\\test2.xlsx";
            int sheetPage = 0;
            XSSFWorkbook book = new XSSFWorkbook(excelPath);
            XSSFSheet sheet = book.getSheetAt(sheetPage);
            for (int row = 0; row < sheet.getLastRowNum() + 1; row++) {
                XSSFRow xssfRow = sheet.getRow(row);
                System.out.println(row);
                for (int col = 0; col < xssfRow.getLastCellNum(); col++) {
                    System.out.println(xssfRow.getCell(col).toString());
                }
            }
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
