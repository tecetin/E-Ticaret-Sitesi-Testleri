package utilities;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;

public class DataProviderMth {

    public static String[][] itemsDP(String path) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(path);
        Workbook workbook = WorkbookFactory.create(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0); //ilk sayfayı alır

        int rowCount = sheet.getLastRowNum();
        String[][] items = new String[rowCount][2]; //Sadece 1. sütundaki verileri alır, her satır için 1 veri [1]

        for (int i = 1 ; i <= rowCount; i++) { // Başlık satırını atlamak için 1'den başla
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(0);
                if (cell != null) { // Hücre boş değilse
                    items[i - 1][0] = cell.getStringCellValue(); // Hücre değerini diziye ekle
                } else {
                    // Hücre boşsa, diziye boş bir değer ekle veya başka bir işlem yap
                    items[i - 1][0] = ""; // Boş bir değer eklendi, isteğe bağlı olarak başka bir değer de atanabilir
                }
            }
        }
        workbook.close();
        fileInputStream.close();

        return items;
    }
}