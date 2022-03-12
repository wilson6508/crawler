package com.tool;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pojo.bean.TwStockName;
import com.pojo.dto.DatabaseApiResponseBean;
import com.pojo.info.NbaGame;
import com.pojo.info.TwTradeLog;
import com.pojo.info.UsaStockPrice;
import com.pojo.info.UsaTradeLog;
import com.pojo.prop.PropertiesBean;
import com.service.fetch.GetService;
import com.service.fetch.IoService;
import com.service.fetch.PostService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.util.stream.Collectors;

@Service
public class ListObjectTool {

    private final Gson gson = new Gson();
    @Autowired
    private PropertiesBean propertiesBean;
    @Autowired
    private DoubleTool doubleTool;
    @Autowired
    private IntTool intTool;
    @Autowired
    private LocalDateTool localDateTool;
    @Autowired
    private StringTool stringTool;
    @Autowired
    private GetService getService;
    @Autowired
    private PostService postService;

    public List<TwTradeLog> skStockLog(List<String> list) {
        DatabaseApiResponseBean repBean = postService.databaseApi("read_tw_name_mapping", null);
        Type type = new TypeToken<ArrayList<TwStockName>>() {}.getType();
        List<TwStockName> nameList = gson.fromJson(gson.toJson(repBean.getResult()), type);
        List<TwTradeLog> repList = new ArrayList<>();
        for (String str : list) {
            TwTradeLog log = new TwTradeLog();
            for (TwStockName name : nameList) {
                if (str.contains(name.getStockName())) {
                    str = str.replace(name.getStockName(), name.getStockId());
                    break;
                }
            }
            String[] tempArray = str.split(" ");
            log.setTradeDate(stringTool.transDate(tempArray[0]));
            log.setStockId(tempArray[1]);
            log.setPayment(intTool.transPayment(tempArray[tempArray.length - 1]));
            int absQuantity = Integer.parseInt(tempArray[2].replace(",", ""));
            int quantity = log.getPayment() < 0 ? absQuantity : -absQuantity;
            log.setQuantity(quantity);
            repList.add(log);
        }
        return repList;
    }

    public List<TwTradeLog> tsStockLog(List<String> list) {
        list = list.stream().filter(e -> !e.startsWith("應收付")).collect(Collectors.toList());
        List<TwTradeLog> repList = new ArrayList<>();
        for (int i = 0; i < list.size(); i += 2) {
            TwTradeLog log = new TwTradeLog();

            String[] row2Array = list.get(i + 1).split("\t");
            String stockId = row2Array[1].substring(row2Array[1].indexOf("(") + 1, row2Array[1].indexOf(")"));
            log.setStockId(stockId);
            log.setPayment(Integer.parseInt(row2Array[row2Array.length - 1].replace(",", "")));

            String[] row1Array = list.get(i).split("\t");
            log.setTradeDate(stringTool.transDate(row1Array[0]));
            int absQuantity = Integer.parseInt(row1Array[3].replace(",", ""));
            int quantity = log.getPayment() < 0 ? absQuantity : -absQuantity;
            log.setQuantity(quantity);

            repList.add(log);
        }
        return repList;
    }

    public List<UsaStockPrice> getUsaStockPriceList(String stockId) {
        String url = String.format(propertiesBean.getChartsUrl(), stockId, "price");
        String rawData = getService.fakeExplorer(url);
        Document document = Jsoup.parse(rawData);
        Element tbody = document.getElementsByTag("tbody").get(0);
        Elements trs = tbody.getElementsByTag("tr");
        List<UsaStockPrice> repList = new ArrayList<>();
        for (Element tr : trs) {
            UsaStockPrice usaStockPrice = new UsaStockPrice();
            Elements tds = tr.getElementsByTag("td");
            String date = tds.get(0).text();
            usaStockPrice.setDate(localDateTool.getObjByString(date).toString());
            usaStockPrice.setStockId(stockId);
            usaStockPrice.setOpen(Double.parseDouble(tds.get(1).text()));
            usaStockPrice.setHigh(Double.parseDouble(tds.get(2).text()));
            usaStockPrice.setLow(Double.parseDouble(tds.get(3).text()));
            usaStockPrice.setClose(Double.parseDouble(tds.get(4).text()));
            usaStockPrice.setVolume(tds.get(5).text());
            repList.add(usaStockPrice);
        }
        return repList;
    }

    public List<UsaTradeLog> getUsaTradeLogList(List<String> list) {
        List<UsaTradeLog> repList = new ArrayList<>();
        for (int i = 0 ; i < list.size() ; i+=4) {
            String rowOne = list.get(i);
            String[] rowOneArray = rowOne.split(" ");
            String[] tradeDateArray = rowOneArray[0].split("/");
            String tradeDate = tradeDateArray[2] + "-" + tradeDateArray[0] + "-" + tradeDateArray[1];
            String action = rowOneArray[2].contains("Sold") ? "Sold" : "Bought";
            String stockId = rowOneArray[4];
            int absQuantity = Integer.parseInt(rowOneArray[3]);
            int quantity = action.equals("Sold") ? -absQuantity : absQuantity;
            String rowTwo = list.get(i+2);
            String[] rowTwoArray = rowTwo.split("\t\t");
            String amount = rowTwoArray[0];

            UsaTradeLog usaTradeLog = new UsaTradeLog();
            usaTradeLog.setTradeDate(tradeDate);
            usaTradeLog.setStockId(stockId);
            usaTradeLog.setQuantity(quantity);
            usaTradeLog.setAmount(Double.parseDouble(amount.replace(",", "")));
            repList.add(usaTradeLog);
        }
        return repList;
    }

    public List<UsaTradeLog> getUsaTradeLogList(String excelPath, int sheetPage) {
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

    public List<NbaGame> getNbaGames(List<String> list) {
        List<NbaGame> nbaGames = new ArrayList<>();
        for (int i = 0; i < list.size(); i += 2) {
            NbaGame nbaGame = new NbaGame();
            try {
                String guestInfo = list.get(i);
                String hostInfo = list.get(i + 1);
                String[] guest = guestInfo.split(" ");
                String[] host = hostInfo.split(" ");
                String guestTeam = guest[1].substring(0, guest[1].length() - 1);
                String hostTeam = host[0].substring(0, host[0].length() - 1);
                nbaGame.setGuest(guestTeam);
                nbaGame.setHost(hostTeam);
                if (guest[2].equals("大")) { // 主R
                    nbaGame.setGreenGuest("+" + doubleTool.getValue(host[1], 2));
                    nbaGame.setGreenHost("-" + doubleTool.getValue(host[1], 2));
                    nbaGame.setGreenTotals("" + doubleTool.getValue(guest[3], 1));
                    nbaGame.setBlueGuest(guest[5]);
                    nbaGame.setBlueHost(guest[5].replace("+", "-"));
                } else {
                    nbaGame.setGreenGuest("-" + doubleTool.getValue(guest[2], 2));
                    nbaGame.setGreenHost("+" + doubleTool.getValue(guest[2], 2));
                    nbaGame.setGreenTotals("" + doubleTool.getValue(guest[4], 1));
                    nbaGame.setBlueGuest(guest[6]);
                    nbaGame.setBlueHost(guest[6].replace("-", "+"));
                }
                nbaGame.setBlueTotals(guest[guest.length - 1]);
            } catch (Exception e) {
                continue;
            }
            nbaGames.add(nbaGame);
        }
        return nbaGames;
    }

}
