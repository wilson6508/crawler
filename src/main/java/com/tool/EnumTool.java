package com.tool;

import lombok.Getter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class EnumTool {

    @Getter
    @AllArgsConstructor
    public enum ModuleType {
        TEST_TEST_ONE("test_test_one", "/test/testOne"),

        SPORT_CRAWL_ODDS("sport_crawl_odds", "/sport/crawlOdds"),
        SPORT_CRAWL_SPREADS("sport_crawl_spreads", "/sport/crawlSpreads"),

        STOCK_CRAWL_USA_TRADE_LOG("stock_crawl_usa_trade_log", "/stock/crawlUsaTradeLog"),
        STOCK_CRAWL_USA_PRICE_LOG("stock_crawl_usa_price_log", "/stock/crawlUsaPriceLog"),

        STOCK_CRAWL_TW_TRADE_LOG("stock_crawl_tw_trade_log", "/stock/crawlTwTradeLog"),
        STOCK_CRAWL_TW_NAME_MAPPING("stock_crawl_tw_name_mapping", "/stock/crawlTwNameMapping"),
        ;
        private String moduleName;
        private String modulePath;
    }

    public ModuleType findModuleType(String str) {
        for (ModuleType moduleType : ModuleType.values()) {
            if (moduleType.getModuleName().equalsIgnoreCase(str)) {
                return moduleType;
            }
        }
        return null;
    }

    public enum GamesEnum {

        MLB(1, "mlb", true),

        NBA(3, "nba", true),
        KBL(92, "kbl", false),
        CBA(94, "cba", false),

        ;


        private int urlId;
        private String name;
        private boolean usaTime;

        GamesEnum(int urlId, String name, boolean usaTime) {
            this.urlId = urlId;
            this.name = name;
            this.usaTime = usaTime;
        }

        public int getUrlId() {
            return urlId;
        }

        public void setUrlId(int urlId) {
            this.urlId = urlId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isUsaTime() {
            return usaTime;
        }

        public void setUsaTime(boolean usaTime) {
            this.usaTime = usaTime;
        }
    }

    public GamesEnum findGamesEnum(String str) {
        for (GamesEnum gamesEnum : GamesEnum.values()) {
            if (gamesEnum.getName().equalsIgnoreCase(str)) {
                return gamesEnum;
            }
        }
        return null;
    }

}
