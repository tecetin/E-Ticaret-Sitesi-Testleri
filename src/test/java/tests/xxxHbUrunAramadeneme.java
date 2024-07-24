package tests;

import org.apache.poi.ss.usermodel.*;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
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
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class xxxHbUrunAramadeneme {

    @DataProvider(name = "itemsDP")
    public static Object[][] itemsDP() throws IOException {

        FileInputStream fileInputStream = new FileInputStream("src/HB_Aranacak_UrunlerveSonuclar.xlsx");
        Workbook workbook = WorkbookFactory.create(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);

        int rowCount = sheet.getLastRowNum();
        List<Object[]> itemList = new ArrayList<>();

        for (int i = 1; i <=rowCount ; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(0);
                if (cell != null && cell.getCellType() != CellType.BLANK) { // Hücre boş değilse ve tipi boş değilse
                    itemList.add(new Object[]{cell.getStringCellValue()});
                }
            }
        }
        workbook.close();
        fileInputStream.close();

        Object[][] validItems = new Object[itemList.size()][1];
        itemList.toArray(validItems);

        return validItems;
    }

        @Test(dataProvider = "itemsDP")
    public void urunAramaTesti(String item) throws IOException {

            Driver.getDriver().get(ConfigReader.getProperty("hbUrl"));

            HepsiburadaPage hepsiburadaPage = new HepsiburadaPage();
            hepsiburadaPage.acceptCookies.click();
            ReusableMethods.wait(1);

            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(hepsiburadaPage.searchBox)).click();

            hepsiburadaPage.searchBox.sendKeys(item + Keys.ENTER);

            int sonucAdedi = ReusableMethods.hbSonucAdedi(hepsiburadaPage.foundItemAmount);
            Assert.assertTrue(sonucAdedi > 0, "Sonuc bulunamadi.");

            hepsiburadaPage.siralamaTik.click();

            Actions actions = new Actions(Driver.getDriver());
            actions.moveToElement(hepsiburadaPage.enDusukFiyat).click().perform();

            ReusableMethods.wait(5);




















            Driver.quitDriver();
    }
}



