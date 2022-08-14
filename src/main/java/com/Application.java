package com;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    public CommandLineRunner run() {
//        return (args -> {
//            System.out.println("666");
//            System.out.println("777");
//        });
//    }

}

/*

    @Autowired
    private PropertiesBean propertiesBean;

    Map<String, Object> map = new HashMap<String, Object>(){{
        put("page", 3);
    }};
    System.out.println(getService.withParameters(propertiesBean.getPlaySportUrl(), map, 1200));

*/
