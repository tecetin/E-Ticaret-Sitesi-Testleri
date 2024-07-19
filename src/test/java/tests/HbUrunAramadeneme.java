package tests;

import org.apache.poi.ss.usermodel.*;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HepsiburadaPage;
import utilities.ConfigReader;
import utilities.Driver;
import utilities.ReusableMethods;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HbUrunAramadeneme {

    @DataProvider(name = "itemsDP")
    public static Object[][] itemsDP() throws IOException {
    FileInputStream fileInputStream = new FileInputStream("src/HB_Aranacak_UrunlerveSonuclar.xlsx");
    Workbook workbook = WorkbookFactory.create(fileInputStream);
    Sheet sheet = workbook.getSheetAt(0);

    int rowCount = sheet.getLastRowNum();
    List<Object[]> itemList = new ArrayList<>();

    for (int i = 1; i <= rowCount; i++) { // Başlık satırını atlamak için 1'den başla
        Row row = sheet.getRow(i);
        if (row != null) {
            Cell cell = row.getCell(0);
            if (cell != null && cell.getCellType() != CellType.BLANK) { // Hücre boş değilse ve tipi boş değilse
                itemList.add(new Object[]{cell.getStringCellValue(), String.valueOf(i)});
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

        @Test(dataProvider = "itemsDP")
    public void urunAramaTesti(String item, String rowNum) throws IOException {

            Driver.getDriver().get(ConfigReader.getProperty("hbUrl"));

            HepsiburadaPage hepsiburadaPage = new HepsiburadaPage();
            hepsiburadaPage.acceptCookies.click();
            ReusableMethods.wait(1);

            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(hepsiburadaPage.searchBox)).click();


            hepsiburadaPage.searchBoxText.sendKeys(item + Keys.ENTER);

            int sonucAdedi = ReusableMethods.hbSonucAdedi(hepsiburadaPage.foundItemAmount);
            Assert.assertTrue(sonucAdedi > 0, "Sonuc bulunamadi.");

            FileInputStream fileInputStream = new FileInputStream(ConfigReader.getProperty("fileUrl"));
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            int rowNumm = Integer.parseInt(rowNum);
            sheet.getRow(rowNumm).getCell(0).setCellValue(item);
            sheet.getRow(rowNumm).getCell(1).setCellValue(sonucAdedi);

            FileOutputStream fileOutputStream = new FileOutputStream(ConfigReader.getProperty("fileUrl"));
            workbook.write(fileOutputStream);
            fileInputStream.close();
            fileOutputStream.close();
            workbook.close();

            Driver.quitDriver();
    }
}



