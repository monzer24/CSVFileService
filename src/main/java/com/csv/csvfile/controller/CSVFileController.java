package com.csv.csvfile.controller;

import com.csv.csvfile.dao.CSVFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CSVFileController {

    @Autowired
    @Qualifier("csvFileServiceImpl")
    private CSVFileService csvFileService;

    public CSVFileController() {
    }

    public Map<Integer, List<String>> getAll(String path, String sep, Boolean withHeader){
        return csvFileService.getAll(path, sep, withHeader);
    }

    public void deleteAll(String path){
        csvFileService.deleteAll(path);
    }

    public Map<Integer, List<String>> find(String path, String sep, Boolean withHeader, String ... keyword){
        return csvFileService.find(path, sep, withHeader, keyword);
    }

    public Map<Integer, List<String>> find(String path, String sep, Boolean withHeader, int colNo, String ... keyword){
        return csvFileService.find(path, sep, withHeader, colNo, keyword);
    }

    public Map<Integer, List<String>> find(String path, String sep, Boolean withHeader, Map<Integer, List<String>> searchData){
        return csvFileService.find(path, sep, withHeader, searchData);
    }

    public void delete(String path, String sep, Boolean withHeader, String ... keyword){
        csvFileService.delete(path, sep, withHeader, keyword);
    }

    public void delete(String path, String sep, boolean withHeader, int colNo, String ... keyword){
        csvFileService.delete(path, sep ,withHeader, colNo, keyword);
    }

    public void delete(String path, String sep, Boolean withHeader, Map<Integer, List<String>> searchData){
        csvFileService.delete(path, sep, withHeader, searchData);
    }
}
