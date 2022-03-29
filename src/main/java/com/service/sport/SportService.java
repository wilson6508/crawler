package com.service.sport;

import com.google.gson.Gson;
import com.pojo.bean.sport.GameOdds;
import com.pojo.bean.sport.NbaBean;
import com.pojo.dto.CrawlerApiResponseBean;
import com.service.fetch.GetService;
import com.tool.EnumTool;
import com.tool.ObjectTool;
import org.joda.time.LocalDate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class SportService {

    @Autowired
    private Gson gson = new Gson();
    @Autowired
    private ObjectTool objectTool;
    @Autowired
    private GetService getService;
    @Autowired
    private EnumTool enumTool;

    public CrawlerApiResponseBean crawlOdds(Object parameter) {
        CrawlerApiResponseBean responseBean = objectTool.getErrorRep();
        List<GameOdds> gameOddsList = new ArrayList<>();
        try {

            HashMap<String, String> hashMap = getParameter(parameter);
            String url = "https://www.playsport.cc/guess/" + hashMap.get("gameId");
            String str = getService.withoutParameters(url);
            int first = str.indexOf("var vueData") + 14;
            int second = str.indexOf(";", first);
            Object betGamesList = gson.fromJson(str.substring(first, second), HashMap.class).get("betGamesList");

            LocalDate target = new LocalDate().plusDays(hashMap.get("gameDay").equals("tomorrow") ? 1 : 0);
            String targetDate = target.toString().substring(5).replace("-", "/") + " (" + target.dayOfWeek().getAsText().substring(2) + ")";
            Object infoList = gson.fromJson(gson.toJson(betGamesList), HashMap.class).get(targetDate);
            List list = gson.fromJson(gson.toJson(infoList), List.class);

            if (list != null) {
                for (Object m : list) {
                    HashMap game = gson.fromJson(gson.toJson(m), HashMap.class);
                    HashMap gametypes = gson.fromJson(gson.toJson(game.get("gametypes")), HashMap.class);
                    for (Object n : gametypes.entrySet()) {

                        HashMap spreads = gson.fromJson(gson.toJson(n), HashMap.class);
                        HashMap bottom = gson.fromJson(gson.toJson(spreads.get("value")), HashMap.class);
                        String match = game.get("away").toString() + " vs " + game.get("home");

                        GameOdds away = gson.fromJson(gson.toJson(bottom.get("1")), GameOdds.class);
                        away.setMatch(match);
                        gameOddsList.add(away);
                        GameOdds home = gson.fromJson(gson.toJson(bottom.get("2")), GameOdds.class);
                        home.setMatch(match);
                        gameOddsList.add(home);
                    }
                }
            } else {
                System.out.println("list null");
            }
            responseBean = objectTool.getSuccessRep();
            responseBean.setResult(gameOddsList);
            responseBean.setExtraInfo(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseBean;
    }

    public CrawlerApiResponseBean crawlSpreads(Object parameter) {
        CrawlerApiResponseBean responseBean = objectTool.getErrorRep();
        try {
            String baseUrl = "https://www.playsport.cc/predictgame.php?allianceid=%s&gameday=%s";

            HashMap<String, String> hashMap = getParameter(parameter);
            String url = String.format(baseUrl, hashMap.get("gameId"), hashMap.get("gameDay"));
            List<String> list = getPredictSpreads(url);
            List<NbaBean> nbaBeans = getNbaBeans(list);

            List<NbaBean> spreads = nbaBeans.stream().sorted(Comparator.comparing(e -> {
                double one = Double.parseDouble(e.getBlueGuest());
                double two = Double.parseDouble(e.getGreenGuest());
                return -Math.abs(one - two);
            })).collect(toList());

            List<NbaBean> totals = nbaBeans.stream().sorted(Comparator.comparing(e -> {
                double one = Double.parseDouble(e.getBlueTotals());
                double two = Double.parseDouble(e.getGreenTotals());
                return -Math.abs(one - two);
            })).collect(toList());

            responseBean = objectTool.getSuccessRep();
            responseBean.setResult(spreads);
            responseBean.setExtraInfo(totals);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseBean;
    }

    private List<String> getPredictSpreads(String url) {
        List<String> list = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.getElementsByClass("predictgame-table");
            Elements trArray = elements.get(0).getElementsByTag("tr");
            for (int i = 0 ; i < trArray.size() ; i++) {
                if (i < 2) {
                    continue;
                }
                Element trTags = trArray.get(i);
                String team = trTags.getElementsByTag("h3").text();
                String data = trTags.getElementsByTag("strong").text();
                list.add(team + data);
            }
            list.remove("預測賽事請先登入");
            list.removeAll(list.stream().filter(e->e.equals("")).collect(toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<NbaBean> getNbaBeans(List<String> list) {
        List<NbaBean> nbaBeans = new ArrayList<>();
        for (int i = 0; i < list.size(); i += 2) {
            NbaBean nbaBean = new NbaBean();
            try {
                String guestInfo = list.get(i);
                String hostInfo = list.get(i + 1);
                String[] guest = guestInfo.split(" ");
                String[] host = hostInfo.split(" ");
                String guestTeam = guest[1].substring(0, guest[1].length() - 1);
                String hostTeam = host[0].substring(0, host[0].length() - 1);
                nbaBean.setGuest(guestTeam);
                nbaBean.setHost(hostTeam);
                if (guest[2].equals("大")) { // 主R
                    nbaBean.setGreenGuest("+" + getValue(host[1], 2));
                    nbaBean.setGreenHost("-" + getValue(host[1], 2));
                    nbaBean.setGreenTotals("" + getValue(guest[3], 1));
                    nbaBean.setBlueGuest(guest[5]);
                    nbaBean.setBlueHost(guest[5].replace("+", "-"));
                } else {
                    nbaBean.setGreenGuest("-" + getValue(guest[2], 2));
                    nbaBean.setGreenHost("+" + getValue(guest[2], 2));
                    nbaBean.setGreenTotals("" + getValue(guest[4], 1));
                    nbaBean.setBlueGuest(guest[6]);
                    nbaBean.setBlueHost(guest[6].replace("-", "+"));
                }
                nbaBean.setBlueTotals(guest[guest.length - 1]);
            } catch (Exception e) {
                continue;
            }
            nbaBeans.add(nbaBean);
        }
        return nbaBeans;
    }

    private double getValue(String str, int pos) {
        double value;
        String temp = str.substring(0, str.length() - pos);
        if (str.contains("贏")) {
            value = Double.parseDouble(temp) - 0.5;
        } else if (str.contains("輸")) {
            value = Double.parseDouble(temp) + 0.5;
        } else {
            value = Double.parseDouble(str);
        }
        return value;
    }

    private HashMap<String, String> getParameter(Object parameter) {
        String league = parameter.toString().split(";")[0];
        EnumTool.GamesEnum gamesEnum = enumTool.findGamesEnum(league);
        String gameId = String.valueOf(gamesEnum.getUrlId());           // 3(NBA)
        String gameDay = parameter.toString().split(";")[1];      // tomorrow
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("gameId", gameId);
        hashMap.put("gameDay", gameDay);
        return hashMap;
    }

}
