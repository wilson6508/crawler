package com;

import com.tool.LocalDateTool;

public class Test {
    public static void main(String[] args) {
        LocalDateTool localDateTool = new LocalDateTool();
//        System.out.println(localDateTool.getDate("01/01"));
        System.out.println(localDateTool.getCurrentTime("dayUsShort").split(" ")[1].equals("Sun"));
    }
}
