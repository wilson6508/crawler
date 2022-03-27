package com.service.fetch;

import com.pojo.prop.PropertiesBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class IoService {

    @Autowired
    private PropertiesBean propertiesBean;

    public String getDataFromExcel(int excelPage, String excelUrl) {
        try {
            String api = String.format(propertiesBean.getReadExcelApi(), excelPage, excelUrl);
            InputStream inputStream = new URL(api).openStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String message;
            while ((message = bufferedReader.readLine()) != null) {
                stringBuilder.append(message);
            }
            inputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getDataFromFile(String filePath) {
        List<String> repList = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String str;
            while ((str = br.readLine()) != null) {
                repList.add(str);
            }
            br.close();
            return repList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
