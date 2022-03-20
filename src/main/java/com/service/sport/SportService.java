package com.service.sport;

import com.google.gson.Gson;
import com.pojo.dto.CrawlerApiResponseBean;
import com.pojo.info.GameOdds;
import com.pojo.info.NbaGame;
import com.pojo.prop.PropertiesBean;
import com.service.fetch.GetService;
import com.tool.EnumTool;
import com.tool.ListObjectTool;
import com.tool.ObjectTool;
import com.tool.StringTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class SportService {

    private final Gson gson = new Gson();
    @Autowired
    private EnumTool enumTool;
    @Autowired
    private ListObjectTool listObjectTool;
    @Autowired
    private ObjectTool objectTool;
    @Autowired
    private StringTool stringTool;
    @Autowired
    private GetService getService;
    @Autowired
    private PropertiesBean propertiesBean;

    public CrawlerApiResponseBean createInfo(Object parameter) {
        CrawlerApiResponseBean responseBean = objectTool.getErrorRep();
        try {
            responseBean = objectTool.getSuccessRep();
            String url = String.format(propertiesBean.getPlaySportUrl(), 3);
            String temp = getService.withoutParameters(url);
            responseBean.setResult(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseBean;
    }

    public CrawlerApiResponseBean crawlMainSpreads(Object parameter) {
        CrawlerApiResponseBean responseBean = objectTool.getErrorRep();
        try {

            String baseUrl = "https://www.playsport.cc/predictgame.php?allianceid=%s&gameday=%s";
            String kind = parameter.toString().split(";")[0];
            String day = parameter.toString().split(";")[1];
            String url = String.format(baseUrl, kind, day);

            List<String> list = stringTool.getPlaySport(url);
            List<NbaGame> nbaGames = listObjectTool.getNbaGames(list);

            List<NbaGame> spreads = nbaGames.stream().sorted(Comparator.comparing(e -> {
                double one = Double.parseDouble(e.getBlueGuest());
                double two = Double.parseDouble(e.getGreenGuest());
                return -Math.abs(one - two);
            })).collect(toList());

            List<NbaGame> totals = nbaGames.stream().sorted(Comparator.comparing(e -> {
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

    public CrawlerApiResponseBean crawlVueData() {
        CrawlerApiResponseBean responseBean = objectTool.getErrorRep();
        List<GameOdds> gameOddsList = new ArrayList<>();
        try {

            EnumTool.GamesEnum gamesEnum = enumTool.findGamesEnum("NBA");
            String url = "https://www.playsport.cc/guess/" + gamesEnum.getUrlId();
            String str = getService.withoutParameters(url);
            int first = str.indexOf("var vueData") + 14;
            int second = str.indexOf(";", first);
            Object betGamesList = gson.fromJson(str.substring(first, second), HashMap.class).get("betGamesList");

            String targetDate = stringTool.getSportDate(gamesEnum.isUsaTime());
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


}
