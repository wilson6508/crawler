package com.service.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pojo.bean.stock.TwName;
import com.pojo.bean.stock.TwTradeLog;
import com.pojo.dto.CrawlerApiResponseBean;
import com.pojo.dto.DatabaseApiResponseBean;
import com.pojo.prop.PropertiesBean;
import com.service.fetch.IoService;
import com.service.fetch.PostService;
import com.tool.LocalDateTool;
import com.tool.ObjectTool;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TwStockService {

    @Autowired
    private Gson gson = new Gson();
    @Autowired
    private PropertiesBean propertiesBean;
    @Autowired
    private ObjectTool objectTool;
    @Autowired
    private IoService ioService;
    @Autowired
    private PostService postService;
    @Autowired
    private LocalDateTool localDateTool;

    public CrawlerApiResponseBean crawlTwTradeLog() {
        CrawlerApiResponseBean responseBean = objectTool.getErrorRep();
        LocalDate localDate = new LocalDate().plusMonths(-1);
        String lastMonth = localDate.getYear() + "-" + localDate.getMonthOfYear();
        try {
            String skFile = String.format("C:\\Users\\hcw\\Desktop\\證券\\新光證券\\對帳單\\sk-%s.txt", lastMonth);
            List<String> skData =ioService.getDataFromFile(skFile);
            List<TwTradeLog> skList = skStockLog(skData);
            List<TwTradeLog> reqList = new ArrayList<>(skList);

            String tsFile = String.format( "C:\\Users\\hcw\\Desktop\\證券\\台新證券\\對帳單\\ts-%s.txt", lastMonth);
            List<String> tsData =ioService.getDataFromFile(tsFile);
            List<TwTradeLog> tsList = tsStockLog(tsData);
            reqList.addAll(tsList);

            DatabaseApiResponseBean repBean = postService.databaseApi("create_tw_stock_trade_log", reqList);
            if (repBean.getSuccess()) {
                responseBean = objectTool.getSuccessRep();
                responseBean.setResult("success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseBean;
    }

    public CrawlerApiResponseBean crawlTwPrice() {
        CrawlerApiResponseBean responseBean = objectTool.getErrorRep();
        try {
            String str = ioService.getDataFromExcel(8, propertiesBean.getFirstExcel());
            DatabaseApiResponseBean repBean = postService.databaseApi("update_tw_name_mapping", gson.fromJson(str, ArrayList.class));
            if (repBean.getSuccess()) {
                responseBean = objectTool.getSuccessRep();
                responseBean.setResult(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseBean;
    }

    private List<TwTradeLog> skStockLog(List<String> list) {
        DatabaseApiResponseBean repBean = postService.databaseApi("read_tw_name_mapping", null);
        Type type = new TypeToken<ArrayList<TwName>>() {
        }.getType();
        List<TwName> nameList = gson.fromJson(gson.toJson(repBean.getResult()), type);

        List<TwTradeLog> repList = new ArrayList<>();
        for (String str : list) {
            TwTradeLog log = new TwTradeLog();
            for (TwName name : nameList) {
                if (str.contains(name.getStockName())) {
                    str = str.replace(name.getStockName(), name.getStockId());
                    break;
                }
            }
            String[] tempArray = str.split(" ");
            log.setTradeDate(localDateTool.getDate(tempArray[0]));
            log.setStockId(tempArray[1]);
            log.setPayment(transPayment(tempArray[tempArray.length - 1]));
            int absQuantity = Integer.parseInt(tempArray[2].replace(",", ""));
            int quantity = log.getPayment() < 0 ? absQuantity : -absQuantity;
            log.setQuantity(quantity);
            repList.add(log);
        }
        return repList;
    }

    private List<TwTradeLog> tsStockLog(List<String> list) {
        list = list.stream().filter(e -> !e.startsWith("應收付")).collect(Collectors.toList());
        List<TwTradeLog> repList = new ArrayList<>();
        for (int i = 0; i < list.size(); i += 2) {
            TwTradeLog log = new TwTradeLog();

            String[] row2Array = list.get(i + 1).split("\t");
            String stockId = row2Array[1].substring(row2Array[1].indexOf("(") + 1, row2Array[1].indexOf(")"));
            log.setStockId(stockId);
            log.setPayment(Integer.parseInt(row2Array[row2Array.length - 1].replace(",", "")));

            String[] row1Array = list.get(i).split("\t");
            log.setTradeDate(localDateTool.getDate(row1Array[0]));
            int absQuantity = Integer.parseInt(row1Array[3].replace(",", ""));
            int quantity = log.getPayment() < 0 ? absQuantity : -absQuantity;
            log.setQuantity(quantity);

            repList.add(log);
        }
        return repList;
    }

    // payment = 15,492(付) , return -15942
    // payment = 15,492(收) , return 15942
    public int transPayment(String payment) {
        String[] stringArray = payment.split("\\(");
        int absPayment = Integer.parseInt(stringArray[0].replace(",", ""));
        return stringArray[1].startsWith("付") ? -absPayment : absPayment;
    }

}
