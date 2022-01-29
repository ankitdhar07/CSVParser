package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.CSVModel;
@Repository
public interface ModelRepository extends JpaRepository<CSVModel,String > {
	
	 List<CSVModel> findByCodeContaining(String code);
}
	