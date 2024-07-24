package utilities;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelListesiOkuArrayDondur {

    public static Object[][] readExcelData(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        Workbook workbook = WorkbookFactory.create(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);

        int rowCount = sheet.getLastRowNum();
        List<Object[]> itemList = new ArrayList<>();

        for (int i = 1; i <= rowCount; i++) { // Başlık satırını atlamak için 1'den başla
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(0);
                if (cell != null && cell.getCellType() != CellType.BLANK) { // Hücreler boş değilse ve tipleri boş değilse
                    itemList.add(new Object[]{cell.getStringCellValue(), i}); //item ismi + satır numarası
                }
            }
        }
        workbook.close();
        fileInputStream.close();

        // List'i diziye dönüştür
        Object[][] validItems = new Object[itemList.size()][2];
        itemList.toArray(validItems);

        return validItems;
    }


}
