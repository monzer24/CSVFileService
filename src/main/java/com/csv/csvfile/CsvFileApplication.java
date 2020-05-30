package com.csv.csvfile;

import com.csv.csvfile.controller.CSVFileController;
import com.csv.csvfile.dao.CSVFileService;
import com.csv.csvfile.service.CSVFileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

@SpringBootApplication
public class CsvFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsvFileApplication.class, args);
    }

}
