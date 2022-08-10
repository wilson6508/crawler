package com.service.test;

import com.pojo.bean.stock.UsaPrice;
import com.pojo.bean.stock.UsaTradeLog;
import com.pojo.dto.CrawlerApiResponseBean;
import com.service.fetch.PostService;
import com.tool.LocalDateTool;
import com.tool.ObjectTool;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TestOneService {

    @Autowired
    private ObjectTool objectTool;
    @Resource
    LocalDateTool localDateTool;
    @Autowired
    PostService postService;

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
        List<UsaTradeLog> list = new ArrayList<>();
        try {
            String excelPath = "C:\\Users\\wilsonhuang\\Desktop\\0809\\test0809.xlsx";
            int sheetPage = 1;
            XSSFWorkbook book = new XSSFWorkbook(excelPath);
            XSSFSheet sheet = book.getSheetAt(sheetPage);
            for (int row = 0; row < sheet.getLastRowNum() + 1; row++) {
                XSSFRow xssfRow = sheet.getRow(row);
                UsaTradeLog usaTradeLog = UsaTradeLog.builder()
                        .tradeDate(xssfRow.getCell(0).toString())
                        .stockId(xssfRow.getCell(1).toString())
                        .quantity((int) Double.parseDouble(xssfRow.getCell(2).toString()))
                        .amount(Double.parseDouble(xssfRow.getCell(3).toString()))
                        .build();
                list.add(usaTradeLog);
//                System.out.println(row);
//                for (int col = 0; col < xssfRow.getLastCellNum(); col++) {
//                    System.out.println(xssfRow.getCell(col).toString());
//                }
            }
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        postService.databaseApi("stock_create_usa_trade_log", list);
        System.out.println("done");
    }
}
