package com.incture.cpm.helper;

import java.util.ArrayList;

import com.incture.cpm.Entity.Assessment;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

public class AssessmentHelp{

    public static List<Assessment> convertExcelToAssessmentRecord(InputStream inpStream){
        List<Assessment> list = new ArrayList<>();

        try{
            XSSFWorkbook workbook = new XSSFWorkbook(inpStream);
            XSSFSheet sheet = workbook.getSheet("Sheet1");

            Iterator<Row> iterator = sheet.iterator();


            while (iterator.hasNext()) {
                Row row = iterator.next();

                if (row.getRowNum() == 0)
                    continue;

                Iterator<Cell> cells = row.cellIterator();
                Assessment assessment = new Assessment();

                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    int columnIndex = cell.getColumnIndex();

                    switch (columnIndex) {
                        case 0:
                        assessment.setTalentId((long) cell.getNumericCellValue());
                        break;
                        
                        case 1:
                        assessment.setAssessmentId((long) cell.getNumericCellValue());
                        break;

                        case 2:
                        assessment.setAssessmentDate(cell.getStringCellValue());
                        break;

                        case 3:
                        assessment.setAssessmentType(cell.getStringCellValue());
                        break;

                        case 4:
                        assessment.setAssessmentSkill(cell.getStringCellValue());
                        break;

                        case 5:
                        assessment.setLocation(String.valueOf(cell.getStringCellValue()));
                        break;

                        case 6:
                        assessment.setAttempts((int) cell.getNumericCellValue());
                        break;

                        case 7:
                        double score = cell.getNumericCellValue();
                        assessment.setScore(score);
                        break;

                        default:
                        break;
                    }
                }
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = now.format(formatter);
                assessment.setComments(formattedDateTime+"-> Assessment Score Declared");
                list.add(assessment);
            }
            workbook.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return list;
    }
}