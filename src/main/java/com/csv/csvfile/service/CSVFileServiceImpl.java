package com.csv.csvfile.service;

import com.csv.csvfile.dao.CSVFileService;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.function.Function;
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
        try {
            data.forEach((key, value) -> {
                value.forEach(word -> {
                    try {
                        fileWriter.write(word + sep);
                        fileWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                try {
                    fileWriter.write("\n");
                    fileWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } finally {
            writer.close();
        }
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
        final List<String> keywords = Arrays.asList(keyword);
        Map<Integer, List<String>> dataFromFile = new HashMap<>();
        Map<Integer, List<String>> foundData = new HashMap<>();
        try {
            dataFromFile = this.readFile(path, sep, withHeader);
            foundData = dataFromFile.entrySet().stream()
                    .filter(data -> data.getValue()
                            .stream()
                            .anyMatch(value -> keywords
                                    .stream()
                                    .anyMatch(k -> k.equals(value))))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
            foundData = dataFromFile.entrySet().stream()
                    .filter(data -> keywords
                                    .stream()
                                    .anyMatch(k -> colNo <= data.getValue().size() && k.equals(data.getValue().get(colNo-1))))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return foundData;
    }

    @Override
    public Map<Integer, List<String>> find(String path, String sep, boolean withHeader, Map<Integer, List<String>> searchData) {
        Map<Integer, List<String>> dataFromFile;
        Map<Integer, List<String>> foundData = new HashMap<>();
        boolean flag = false;
        try {

            dataFromFile = this.readFile(path, sep, withHeader);
            List<Integer> keys = new ArrayList<>(searchData.keySet());
            System.out.println(keys);

            for(Integer key : keys){
                Map<Integer, List<String>> finalDataFromFile = dataFromFile;
                List<String> list = finalDataFromFile.get(key).stream()
                        .filter(data -> key <= finalDataFromFile.size() && finalDataFromFile.get(key).contains(searchData.get(key).iterator().next()))
                        .collect(Collectors.toList());
                if (list.size() != 0) {
                    foundData.put(key, list);
                }
            }

            System.out.println(foundData);

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
            Map<Integer, List<String>> foundData = dataFromFile.entrySet().stream()
                    .filter(data -> data.getValue()
                            .stream()
                            .anyMatch(value -> keywords
                                    .stream()
                                    .anyMatch(k -> k.equals(value))))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            System.out.println(foundData);

            for(Map.Entry<Integer, List<String>> data : foundData.entrySet()){
                dataFromFile.remove(data.getKey());
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
            for (String value : keywords) {
                int j = 1;
                while (j <= count) {
                    if (dataFromFile.containsKey(j)) {
                        if (dataFromFile.get(j).get(colNo - 1).equals(value)) {
                            System.out.println(dataFromFile.get(j));
                            dataFromFile.remove(j);
                        }
                    }
                    j++;
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
        try {
            dataFromFile = this.readFile(path, sep, false);
            int count = dataFromFile.size();
            List<Integer> keys = new ArrayList<>(searchData.keySet());

            for (int key : keys) {
                int k = 0;
                while (k < searchData.get(key).size()) {
                    String value = searchData.get(key).get(k);
                    if (dataFromFile.containsKey(key)) {
                        if (dataFromFile.get(key).contains(value)) {
                            dataFromFile.remove(key);
                        }
                    }
                    k++;
                }
            }
            System.out.println(dataFromFile);
            this.writeAFile(path, sep, dataFromFile, count);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
