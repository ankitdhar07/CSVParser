package com.example.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.CSVModel;

public class ParseCSV {
	public static final String TYPE = "text/csv";
	static String[] HEADERS = { "source", "codeListCode", "code", "displayValue", "longDescription", "fromDate",
			"toDate", "sortingPriority" };

	public static boolean hasCSVFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	public static List<CSVModel> csvToCsvModels(InputStream is) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			List<CSVModel> CSVModelList = new ArrayList<>();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			for (CSVRecord csvRecord : csvRecords) {
				CSVModel csvModel = new CSVModel(csvRecord.get("source"), csvRecord.get("codeListCode"),
						csvRecord.get("code"), csvRecord.get("displayValue"), csvRecord.get("longDescription"),
						csvRecord.get("fromDate"), csvRecord.get("toDate"), csvRecord.get("sortingPriority"));

				CSVModelList.add(csvModel);
			}

			return CSVModelList;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		}
	}

}