package com.wellfargo.filleUploadUtility.controller;

import com.wellfargo.filleUploadUtility.repository.Dataset;
import com.wellfargo.filleUploadUtility.repository.FileUploadUtilRepository;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.record.Record;
import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.CsvParser;
import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FileUploadController {

    @Autowired
    FileUploadUtilRepository repository;
    @PostMapping("/api/upload")
    public ResponseEntity<String> saveFile(@RequestParam("file")MultipartFile file) throws Exception{
        List<Dataset> datasetList = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        CsvParserSettings csvParserSettings = new CsvParserSettings();
        csvParserSettings.setHeaderExtractionEnabled(true);
        CsvParser csvParser = new CsvParser(csvParserSettings);
        List<Record> parsedRecords =  csvParser.parseAllRecords(inputStream);
        parsedRecords.forEach(record -> {
            Dataset dataset = new Dataset();
            dataset.setX(Double.parseDouble(record.getString("x")));
            dataset.setY(Double.parseDouble(record.getString("y")));
            datasetList.add(dataset);
        });
        repository.saveAll(datasetList);
        return new ResponseEntity<String>("Dataset uploaded Successfully!", HttpStatus.CREATED);
    }

    @GetMapping("/api/retrieveData")
    public List<Dataset> retrieveData(){
        return repository.findAll();
    }
}
