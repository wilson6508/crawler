package com.tool;

import org.springframework.stereotype.Service;

@Service
public class EnumTool {

    public enum ModuleType {

        CRAWL_MAIN_SPREADS("crawl_main_spreads", "/sport/crawlMainSpreads", false),

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

}
