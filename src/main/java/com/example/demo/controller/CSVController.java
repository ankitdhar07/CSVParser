package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.ParseCSV;
import com.example.demo.message.ResponseMessage;
import com.example.demo.model.CSVModel;
import com.example.demo.repository.ModelRepository;
import com.example.demo.service.CSVService;

@CrossOrigin("http://localhost:8081")
@RestControllerAdvice
@RequestMapping("/api/csv")
public class CSVController {

	@Autowired
	CSVService fileService;
	
	@Autowired
	ModelRepository modelRepository;

	  @GetMapping("/all")
	  public ResponseEntity<List<CSVModel>> getAllTutorials(@RequestParam(required = false) String code) {
	    try {
	      List<CSVModel> modelList = new ArrayList<>();

	      if (code == null)
	    	  modelRepository.findAll().forEach(modelList::add);
	      else
	    	  modelRepository.findByCodeContaining(code).forEach(modelList::add);

	      if (modelList.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	      }

	      return new ResponseEntity<>(modelList, HttpStatus.OK);
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }

	  @GetMapping("/modelList/{id}")
	  public ResponseEntity<CSVModel> getTutorialById(@PathVariable("code") String id) {
	    Optional<CSVModel> data = modelRepository.findById(id);

	    if (data.isPresent()) {
	      return new ResponseEntity<>(data.get(), HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	  }

	  @PostMapping("/upload")
	  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
	    String message = "";

	    if (ParseCSV.hasCSVFormat(file)) {
	      try {
	        fileService.save(file);

	        message = "Uploaded the file successfully: " + file.getOriginalFilename();
	        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	      } catch (Exception e) {
	        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
	        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	      }
	    }

	    message = "Please upload a csv file!";
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	  }

	  

	

	  @DeleteMapping("/all")
	  public ResponseEntity<HttpStatus> deleteAllData() {
	    try {
	    	modelRepository.deleteAll();
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
	    }

	  }

	 

}