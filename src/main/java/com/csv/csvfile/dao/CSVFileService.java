package com.csv.csvfile.dao;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CSVFileService {

    Map<Integer, List<String>> getAll(String path, String sep, boolean withHeader);

    void deleteAll(String path);

    Map<Integer, List<String>> find(String path, String sep, boolean withHeader, String ... keyword);

    Map<Integer, List<String>> find(String path, String sep, boolean withHeader, int colNo, String ... keyword);

    Map<Integer, List<String>> find(String path, String sep, boolean withHeader, Map<Integer, List<String>> searchData);

    void delete(String path, String sep, boolean withHeader, String ... keyword);

    void delete(String path, String sep, boolean withHeader, int colNo, String ... keyword);

    void delete(String path, String sep, boolean withHeader, Map<Integer, List<String>> searchData);

}
