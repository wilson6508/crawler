package com.service.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pojo.dto.CrawlerApiResponseBean;
import com.pojo.dto.DatabaseApiResponseBean;
import com.pojo.info.UsaStockPrice;
import com.pojo.info.UsaTradeLog;
import com.pojo.prop.PropertiesBean;
import com.service.fetch.GetService;
import com.service.fetch.PostService;
import com.tool.ListObjectTool;
import com.tool.LocalDateTool;
import com.tool.ObjectTool;
import org.joda.time.LocalDate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private ListObjectTool listObjectTool;
    @Autowired
    private PostService postService;

    public CrawlerApiResponseBean crawlUsaPrice(Object parameter) {

        CrawlerApiResponseBean responseBean = objectTool.getErrorRep();
        List<UsaStockPrice> list = new ArrayList<>();
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
                String date = localDateTool.getObjByString(originDate).toString();
                String yesterday = new LocalDate().plusDays(-1).toString();
                if (date.equals(yesterday)) {
                    UsaStockPrice usaStockPrice = new UsaStockPrice();
                    usaStockPrice.setDate(date);
                    usaStockPrice.setStockId(stockId);
                    usaStockPrice.setOpen(Double.parseDouble(tds.get(1).text()));
                    usaStockPrice.setHigh(Double.parseDouble(tds.get(2).text()));
                    usaStockPrice.setLow(Double.parseDouble(tds.get(3).text()));
                    usaStockPrice.setClose(Double.parseDouble(tds.get(4).text()));
                    usaStockPrice.setVolume(tds.get(5).text());
                    list.add(usaStockPrice);
                }
            }
            responseBean = objectTool.getSuccessRep();
            responseBean.setResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseBean;
    }

    public CrawlerApiResponseBean crawlUsaTradeLog() {
        CrawlerApiResponseBean responseBean = objectTool.getErrorRep();
        try {
//            List<String> list = ioService.getDataFromFile("C:\\Users\\hcw\\Desktop\\tempStock\\temp.txt");
//            List<UsaTradeLog> reqList = listObjectTool.getUsaTradeLogList(list);
//            DatabaseApiResponseBean repBean = postService.databaseApi("create_usa_stock_trade_log", reqList);
//            if (repBean.getSuccess()) {
//                responseBean = objectTool.getSuccessRep();
//                responseBean.setResult("success");
//            }
            List<UsaTradeLog> reqList = listObjectTool.getUsaTradeLogList("C:\\Users\\hcw\\Desktop\\證券\\TD\\trade.xlsx", 0);
            DatabaseApiResponseBean repBean = postService.databaseApi("create_usa_stock_trade_log", reqList);
            if (repBean.getSuccess()) {
                responseBean = objectTool.getSuccessRep();
                responseBean.setResult("success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseBean;
    }

}


