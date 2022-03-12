package com.service.sport;

import com.google.gson.Gson;
import com.pojo.dto.CrawlerApiResponseBean;
import com.pojo.info.NbaGame;
import com.pojo.prop.PropertiesBean;
import com.service.fetch.GetService;
import com.tool.ListObjectTool;
import com.tool.ObjectTool;
import com.tool.StringTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class SportService {

    private final Gson gson = new Gson();
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
                return -Math.abs(one-two);
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



}
