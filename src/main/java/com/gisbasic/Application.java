package com.gisbasic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @SpringBootApplication 用来标注一个主程序类，标识为SpringBoot应用
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        //启动spring应用
        SpringApplication.run(Application.class, args);
    }

}
