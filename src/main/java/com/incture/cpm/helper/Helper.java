package com.incture.cpm.helper;

import com.incture.cpm.Entity.Candidate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Helper {


    //check that file is of excel type or not
    public static boolean checkExcelFormat(MultipartFile file) {

        String contentType = file.getContentType();

        if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            return true;
        } else {
            return false;
        }

    }


    //convert excel to list of products

    @SuppressWarnings("deprecation")
    public static List<Candidate> convertExcelToListOfProduct(InputStream is) {
        List<Candidate> list = new ArrayList<>();

        try {


            XSSFWorkbook workbook = new XSSFWorkbook(is);

            XSSFSheet sheet = workbook.getSheet("Sheet1");

            int rowNumber = 0;
            Iterator<Row> iterator = sheet.iterator();

            while (iterator.hasNext()) {
                Row row = iterator.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cells = row.iterator();

                int cid = 0;
                // long ct=1;
                Candidate p = new Candidate();

                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    p.setStatus("Interview Pending");
                    switch (cid) {
                        case 0:
                            p.setCandidateId((long) cell.getNumericCellValue());
                            System.out.println(cell.getCellType());
                            break;
                        case 1:
                            p.setCandidateName(cell.getStringCellValue());
                            System.out.println(cell.getCellType());
                            break;
                        case 2:
                            p.setCandidateCollege(cell.getStringCellValue());
                            System.out.println(cell.getCellType());
                            break;
                        case 3:
                        p.setDepartment(cell.getStringCellValue());
                            System.out.println(cell.getCellType());
                            break;
                            case 4:
                            p.setEmail(cell.getStringCellValue());
                            System.out.println(cell.getCellType());
                            break;
                            case 5:
                            p.setPhoneNumber(Integer.toString((int)cell.getNumericCellValue()));
                            
                            System.out.println(cell.getCellType());
                            break;
                            case 6:
                            p.setAlternateNumber(Integer.toString((int)cell.getNumericCellValue()));
                            
                            System.out.println(cell.getCellType());
                            break;
                            case 7:
                            p.setTenthPercent((double)cell.getNumericCellValue());
                            System.out.println(cell.getCellType());
                            break;
                            case 8:
                            p.setTwelthPercent((double)cell.getNumericCellValue());
                            System.out.println(cell.getCellType());
                            break;
                            case 9:
                            p.setMarksheetsSemwise(cell.getStringCellValue());
                            System.out.println(cell.getCellType());
                            break;
                            case 10:
                            p.setCurrentLocation(cell.getStringCellValue());
                            System.out.println(cell.getCellType());
                            break;
                            case 11:
                            p.setPermanentAddress(cell.getStringCellValue());
                            System.out.println(cell.getCellType());
                            break;

                            case 12:
                            p.setPanNumber(cell.getStringCellValue());
                            System.out.println(cell.getCellType());
                            break;
                            case 13:
                            p.setAadhaarNumber(String.valueOf(cell.getNumericCellValue()));
                            System.out.println(cell.getCellType());
                            break;
                            case 14:
                            p.setFatherName(cell.getStringCellValue());
                            System.out.println(cell.getCellType());
                            break;
                            case 15:
                            p.setMotherName(cell.getStringCellValue());
                            System.out.println(cell.getCellType());
                            break;
                            case 16:
                            p.setDOB(cell.getStringCellValue());
                            System.out.println(cell.getCellType());
                            break;
                            case 17:
                            p.setCgpaUndergrad((double)cell.getNumericCellValue());
                            System.out.println(cell.getCellType());
                            break;
                            case 18:
                            p.setCgpaMasters((double)cell.getNumericCellValue());
                            System.out.println(cell.getCellType());
                            break;
                            
                        default:
                            break;
                    }
                    cid++;

                }

                list.add(p);


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }


}
