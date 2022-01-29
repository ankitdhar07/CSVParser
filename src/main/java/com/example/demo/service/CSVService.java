package com.example.demo.service;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.ParseCSV;
import com.example.demo.model.CSVModel;
import com.example.demo.repository.ModelRepository;


@Service
public class CSVService {
  @Autowired
  ModelRepository modelRepository;

  public void save(MultipartFile file) {
    try {
      List<CSVModel> listOfModel = ParseCSV.csvToCsvModels(file.getInputStream());
      modelRepository.saveAll(listOfModel);
    } catch (IOException e) {
      throw new RuntimeException("fail to store csv data: " + e.getMessage());
    }
  }

  public List<CSVModel> getAll() {
    return (List<CSVModel>) modelRepository.findAll();
  }
}
