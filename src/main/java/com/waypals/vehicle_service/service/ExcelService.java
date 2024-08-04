package com.waypals.vehicle_service.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {

    public List<String> getVehicleNumbers(MultipartFile file) throws IOException {
        List<String> vehicleNumbers = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            if (sheet == null) {
                throw new IllegalArgumentException("The Excel sheet is empty or not found");
            }

            Row headerRow = sheet.getRow(1);
            if (headerRow == null) {
                throw new IllegalArgumentException("The header row is missing in the Excel sheet");
            }

            int vehicleNumberColumnIndex = -1;

            for (Cell cell : headerRow) {
                if ("Vehicle Number".equalsIgnoreCase(cell.getStringCellValue().trim())) {
                    vehicleNumberColumnIndex = cell.getColumnIndex();
                    break;
                }
            }

            if (vehicleNumberColumnIndex == -1) {
                throw new IllegalArgumentException("Column 'Vehicle Number' not found in the Excel sheet");
            }

            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell cell = row.getCell(vehicleNumberColumnIndex);
                    if (cell != null) {
                        vehicleNumbers.add(cell.getStringCellValue());
                    }
                }
            }
        }

        return vehicleNumbers;
    }
}
