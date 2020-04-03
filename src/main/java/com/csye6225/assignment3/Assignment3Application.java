package com.csye6225.assignment3;

import com.csye6225.assignment3.mbg.AutoGenerator;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.sql.SQLException;

@SpringBootApplication
@EnableScheduling
public class Assignment3Application {

    public static void main(String[] args) {

        AutoGenerator auto =  new AutoGenerator();
        auto.createDatabaseTable();
        //auto.createDatabaseTableConnection();
        //auto.createDir();
        SpringApplication.run(Assignment3Application.class, args);
    }

}
