package com.tool;

import org.springframework.stereotype.Service;

@Service
public class EnumTool {

    public enum ModuleType {

        CRAWL_MAIN_SPREADS("crawl_main_spreads", "/sport/crawlMainSpreads", false),
        CRAWL_VUE_DATA("crawl_vue_data", "/sport/crawlVueData", false),

        CRAWL_TW_STOCK_NOW_PRICE("crawl_tw_stock_now_price", "/stock/crawlTwNowPrice", false),
        CRAWL_TW_STOCK_TRADE_LOG("crawl_tw_stock_trade_log", "/stock/crawlTwTradeLog", false),

        CRAWL_USA_STOCK_TRADE_LOG("crawl_usa_stock_trade_log", "/stock/crawlUsaTradeLog", false),

        ;

        private String moduleName;
        private String modulePath;
        private boolean needLog;

        ModuleType(String moduleName, String modulePath, boolean needLog) {
            this.moduleName = moduleName;
            this.modulePath = modulePath;
            this.needLog = needLog;
        }

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }

        public String getModulePath() {
            return modulePath;
        }

        public void setModulePath(String modulePath) {
            this.modulePath = modulePath;
        }

        public boolean isNeedLog() {
            return needLog;
        }

        public void setNeedLog(boolean needLog) {
            this.needLog = needLog;
        }
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
