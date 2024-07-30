package utilities;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelKayit extends Throwable {

    public void excelBilgiKayit(String filePath, int sheetNum, int rowNum, int cell, String item) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(ConfigReader.getProperty("filePath"));
        Workbook workbook = WorkbookFactory.create(fileInputStream);
        Sheet sheet = workbook.getSheetAt(sheetNum);

        sheet.getRow(rowNum).getCell(cell).setCellValue(item);

        FileOutputStream fileOutputStream = new FileOutputStream(ConfigReader.getProperty("filePath"));
        workbook.write(fileOutputStream);
        fileInputStream.close();
        fileOutputStream.close();
        workbook.close();
    }
    public void excelBilgiKayit(String filePath, int sheetNum, int rowNum, int cell, int item) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(ConfigReader.getProperty("filePath"));
        Workbook workbook = WorkbookFactory.create(fileInputStream);
        Sheet sheet = workbook.getSheetAt(sheetNum);

        sheet.getRow(rowNum).getCell(cell).setCellValue(item);

        FileOutputStream fileOutputStream = new FileOutputStream(ConfigReader.getProperty("filePath"));
        workbook.write(fileOutputStream);
        fileInputStream.close();
        fileOutputStream.close();
        workbook.close();
    }
}
