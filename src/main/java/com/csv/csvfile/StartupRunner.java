package com.csv.csvfile;

import com.csv.csvfile.controller.CSVFileController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * the csv file
 * firstName,lastName,avg,
 * monzer,alhusaini,85,
 * ali,ahmad,82,
 * omar,hussain,87,
 * **/

@Component
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private CSVFileController controller;

    @Override
    public void run(String... args) throws Exception {
        Map<Integer, List<String>> searchData = new HashMap<>();
        List row1 = new ArrayList();
        row1.add("monzer");
        row1.add("85");
        List row2 = new ArrayList();
        row2.add("ali");
        searchData.put(2, row1);
        searchData.put(3, row2);

//        System.out.println(controller.getAll("C:\\Users\\Monzer\\OneDrive\\Desktop\\csv.txt", ",", true));

//        controller.deleteAll("C:\\Users\\Monzer\\OneDrive\\Desktop\\csv.txt");

//        System.out.println(controller.find("C:\\Users\\Monzer\\OneDrive\\Desktop\\csv.txt", ",", true, "monzer", "87"));

//        System.out.println(controller.find("C:\\Users\\Monzer\\OneDrive\\Desktop\\csv.txt", ",", true, 2, "hussain"));

//        System.out.println(controller.find("C:\\Users\\Monzer\\OneDrive\\Desktop\\csv.txt", ",", true, searchData));

//        controller.delete("C:\\Users\\Monzer\\OneDrive\\Desktop\\csv.txt", ",", true, "omar");

//        controller.delete("C:\\Users\\Monzer\\OneDrive\\Desktop\\csv.txt", ",", true, 1, "ali" );

        controller.delete("C:\\Users\\Monzer\\OneDrive\\Desktop\\csv.txt", ",", true, searchData);
    }

}
