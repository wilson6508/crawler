package com.service.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pojo.bean.stock.UsaPrice;
import com.pojo.bean.stock.UsaTradeLog;
import com.pojo.dto.CrawlerApiResponseBean;
import com.pojo.dto.DatabaseApiResponseBean;
import com.pojo.prop.PropertiesBean;
import com.service.fetch.GetService;
import com.service.fetch.PostService;
import com.tool.LocalDateTool;
import com.tool.ObjectTool;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsaStockService {

    private final Gson gson = new Gson();
    @Autowired
    private ObjectTool objectTool;
    @Autowired
    private LocalDateTool localDateTool;
    @Autowired
    private PropertiesBean propertiesBean;
    @Autowired
    private GetService getService;
    @Autowired
    private PostService postService;

    public CrawlerApiResponseBean crawlUsaTradeLog() {
        CrawlerApiResponseBean responseBean = objectTool.getErrorRep();
        try {
            List<UsaTradeLog> reqList = getUsaTradeLogList("C:\\Users\\hcw\\Desktop\\證券\\TD\\trade.xlsx", 0);
            DatabaseApiResponseBean repBean = postService.databaseApi("stock_create_usa_trade_log", reqList);
            if (repBean.getSuccess()) {
                responseBean = objectTool.getSuccessRep();
                responseBean.setResult("success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseBean;
    }

    public CrawlerApiResponseBean crawlUsaPrice(Object parameter) {

        CrawlerApiResponseBean responseBean = objectTool.getErrorRep();
        List<UsaPrice> list = new ArrayList<>();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        List<String> stockIdList = gson.fromJson(gson.toJson(parameter), type);

        try {
            for (String stockId : stockIdList) {
                String url = String.format(propertiesBean.getChartsUrl(), stockId, "price");
                String rawData = getService.fakeExplorer(url);
                Document document = Jsoup.parse(rawData);
                Element tbody = document.getElementsByTag("tbody").get(0);
                Elements trs = tbody.getElementsByTag("tr");
                Element tr = trs.get(0);
                Elements tds = tr.getElementsByTag("td");

                String originDate = tds.get(0).text();
                String date = localDateTool.getDateByString(originDate);
                String yesterday = new LocalDate().plusDays(-1).toString();
                if (date.equals(yesterday)) {
                    UsaPrice usaPrice = new UsaPrice();
                    usaPrice.setDate(date);
                    usaPrice.setStockId(stockId);
                    usaPrice.setOpen(Double.parseDouble(tds.get(1).text()));
                    usaPrice.setHigh(Double.parseDouble(tds.get(2).text()));
                    usaPrice.setLow(Double.parseDouble(tds.get(3).text()));
                    usaPrice.setClose(Double.parseDouble(tds.get(4).text()));
                    usaPrice.setVolume(tds.get(5).text());
                    list.add(usaPrice);
                }
            }
            responseBean = objectTool.getSuccessRep();
            responseBean.setResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseBean;
    }

    private List<UsaTradeLog> getUsaTradeLogList(String excelPath, int sheetPage) {
        List<UsaTradeLog> result = new ArrayList<>();
        try {
            XSSFWorkbook book = new XSSFWorkbook(excelPath);
            XSSFSheet sheet = book.getSheetAt(sheetPage);
            int rows = sheet.getLastRowNum() + 1;
            for (int row = 0; row < rows; row ++) {
                UsaTradeLog usaTradeLog = new UsaTradeLog();
                XSSFRow xssfRow = sheet.getRow(row);

                String[] temp = xssfRow.getCell(0).toString().split("/");
                String tradeDate = temp[2] + "-" + temp[0] + "-" + temp[1];
                String boughtSold = xssfRow.getCell(1).toString();
                double tempQty = Double.parseDouble(xssfRow.getCell(3).toString());
                int qty = (int) Math.round(tempQty);
                double amount = Double.parseDouble(xssfRow.getCell(4).toString());

                usaTradeLog.setTradeDate(tradeDate);
                usaTradeLog.setStockId(xssfRow.getCell(2).toString());
                usaTradeLog.setQuantity(boughtSold.equals("Sold") ? -qty : qty);
                usaTradeLog.setAmount(boughtSold.equals("Sold") ? amount : -amount);
                result.add(usaTradeLog);
            }
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
