package com.tool;

import org.springframework.stereotype.Service;

@Service
public class DoubleTool {

    public double getValue(String str, int pos) {
        double value;
        String temp = str.substring(0, str.length() - pos);
        if (str.contains("贏")) {
            value = Double.parseDouble(temp) - 0.5;
        } else if (str.contains("輸")) {
            value = Double.parseDouble(temp) + 0.5;
        } else {
            value = Double.parseDouble(str);
        }
        if (Math.abs(value) == 0.5) {
            return 0;
        }
        return value;
    }

}
