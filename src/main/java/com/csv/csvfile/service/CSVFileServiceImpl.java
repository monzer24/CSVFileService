package com.csv.csvfile.service;

import com.csv.csvfile.dao.CSVFileService;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Component("csvFileServiceImpl")
public class CSVFileServiceImpl implements CSVFileService {

    private Map<Integer, List<String>> readFile(String path, String sep, boolean withHeader) throws IOException {
        File file = new File(path);
        FileReader reader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(reader);
        int rowNo;
        if (withHeader) {
            rowNo = 0;
        } else {
            rowNo = 1;
        }
        Map<Integer, List<String>> dataFromFile = new HashMap<>();
        String line = "";

        while ((line = bufferedReader.readLine()) != null) {
            List<String> data = Arrays.asList(line.split(sep));
            dataFromFile.put(rowNo, data);
            rowNo++;
        }
        if (withHeader) {
            dataFromFile.remove(0);
        }
        bufferedReader.close();
        return dataFromFile;
    }

    private void writeAFile(String path, String sep, Map<Integer, List<String>> data, int count) throws IOException {
        File file = new File(path);
        FileWriter writer = new FileWriter(file);
        BufferedWriter fileWriter = new BufferedWriter(writer);
        for (int i = 1; i <= count; i++) {
            if(data.keySet().contains(i)) {
                List<String> dataList = data.get(i);
                for (int j = 0; j < dataList.size(); j++) {
                    fileWriter.write(dataList.get(j) + sep);
                    fileWriter.flush();
                }
                fileWriter.write("\n");
            }
        }
        writer.close();
    }

    @Override
    public Map<Integer, List<String>> getAll(String path, String sep, boolean withHeader) {
        Map<Integer, List<String>> allData = null;
        try {
            allData = readFile(path, sep, withHeader);
            System.out.println(allData);
        } catch (FileNotFoundException notFound) {
            notFound.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return allData;
    }

    @Override
    public void deleteAll(String path) {
        File file = new File(path);
        FileWriter fileWriter = null;
        BufferedWriter writer = null;
        try {
            fileWriter = new FileWriter(file);
            writer = new BufferedWriter(fileWriter);
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Map<Integer, List<String>> find(String path, String sep, boolean withHeader, String... keyword) {
        List<String> keywords = Arrays.asList(keyword);
        Map<Integer, List<String>> dataFromFile = new HashMap<>();
        Map<Integer, List<String>> foundData = new HashMap<>();
        try {
            dataFromFile = this.readFile(path, sep, withHeader);
            for (int i = 0; i < keywords.size(); i++) {
                String value = keywords.get(i);
                for (int j = 1; j < dataFromFile.size() + 1; j++) {
                    if (dataFromFile.get(j).contains(value)) {
                        System.out.println(dataFromFile.get(j));
                        foundData.put(j, dataFromFile.get(j));
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return foundData;
    }

    @Override
    public Map<Integer, List<String>> find(String path, String sep, boolean withHeader, int colNo, String... keyword) {
        List<String> keywords = Arrays.asList(keyword);
        Map<Integer, List<String>> dataFromFile = new HashMap<>();
        Map<Integer, List<String>> foundData = new HashMap<>();
        try {
            dataFromFile = this.readFile(path, sep, withHeader);
            for (int i = 0; i < keywords.size(); i++) {
                String value = keywords.get(i);
                for (int j = 1; j < dataFromFile.size() + 1; j++) {
                    if (dataFromFile.get(j).get(colNo-1).equals(value)) {
                        System.out.println(dataFromFile.get(j));
                        foundData.put(j, dataFromFile.get(j));
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return foundData;
    }

    @Override
    public Map<Integer, List<String>> find(String path, String sep, boolean withHeader, Map<Integer, List<String>> searchData) {
        Map<Integer, List<String>> dataFromFile = new HashMap<>();
        Map<Integer, List<String>> foundData = new HashMap<>();
        boolean flag = false;
        List<Integer> keys = new ArrayList<>();
        try {
            dataFromFile = this.readFile(path, sep, withHeader);
            keys.addAll(searchData.keySet());
            for(int i=0; i<keys.size(); i++){
                int key = keys.get(i);
                for (int k = 0; k < searchData.get(key).size(); k++) {
                    String value = searchData.get(key).get(k);
                    if(dataFromFile.get(key).contains(value)){
                        foundData.put(key, dataFromFile.get(key));
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return foundData;
    }

    @Override
    public void delete(String path, String sep, boolean withHeader, String... keyword) {
        List<String> keywords = Arrays.asList(keyword);
        Map<Integer, List<String>> dataFromFile = new HashMap<>();
        try {
            dataFromFile = this.readFile(path, sep, false);
            int count = dataFromFile.size();
            for (int i = 0; i < keywords.size(); i++) {
                String value = keywords.get(i);
                for (int j = 1; j <= count; j++) {
                    if(dataFromFile.keySet().contains(j)){
                        if (dataFromFile.get(j).contains(value)) {
                            System.out.println(dataFromFile.get(j));
                            dataFromFile.remove(j);
                        }
                    }
                }
            }
            this.writeAFile(path, sep, dataFromFile, count);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void delete(String path, String sep, boolean withHeader, int colNo, String... keyword) {
        List<String> keywords = Arrays.asList(keyword);
        Map<Integer, List<String>> dataFromFile = new HashMap<>();
        try {
            dataFromFile = this.readFile(path, sep, false);
            int count = dataFromFile.size();
            for (int i = 0; i < keywords.size(); i++) {
                String value = keywords.get(i);
                for (int j = 1; j <= count; j++) {
                    if(dataFromFile.keySet().contains(j)){
                        if (dataFromFile.get(j).get(colNo-1).equals(value)) {
                            System.out.println(dataFromFile.get(j));
                            dataFromFile.remove(j);
                        }
                    }
                }
            }
            this.writeAFile(path, sep, dataFromFile, count);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void delete(String path, String sep, boolean withHeader, Map<Integer, List<String>> searchData) {
        Map<Integer, List<String>> dataFromFile = new HashMap<>();
        List<Integer> keys = new ArrayList<>();
        try {
            dataFromFile = this.readFile(path, sep, false);
            int count = dataFromFile.size();
            keys.addAll(searchData.keySet());
            for(int i=0; i<keys.size(); i++) {
                int key = keys.get(i);
                for (int k = 0; k < searchData.get(key).size(); k++) {
                    String value = searchData.get(key).get(k);
                    if (dataFromFile.keySet().contains(key)) {
                        if (dataFromFile.get(key).contains(value)) {
                            dataFromFile.remove(key);
                        }
                    }
                }
            }
            this.writeAFile(path, sep, dataFromFile, count);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
