package tests;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HepsiburadaPage;
import utilities.ConfigReader;
import utilities.DataProviderMth;
import utilities.Driver;
import utilities.ReusableMethods;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;

public class HbUrunArama {

    @DataProvider
    public static Object[][] itemsDP() throws IOException {
        String[][] products = DataProviderMth.itemsDP(ConfigReader.getPropert("fileUrl"));

        // String[][] -> Object[][]
        Object[][] productData = new Object[products.length][2];
        for (int i = 0; i < products.length; i++) {
            productData[i][0] = products [i][0]; //Ürün adı
            productData[i][1] = i+1; // Satır numarası (1'den başlıyor)
        }

        return productData; //alınan verinini satır numarasını veriyor, test metoduna int verisi ekleyerek kullanılabilir
    }

    @Test(dataProvider = "itemsDP")
    public void urunAramaTesti(String item, int rowNum) throws IOException {

            Driver.getDriver().get(ConfigReader.getPropert("hbUrl"));

            HepsiburadaPage hepsiburadaPage = new HepsiburadaPage();
            hepsiburadaPage.acceptCookies.click();
            ReusableMethods.wait(1);

            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(hepsiburadaPage.searchBox)).click();

            hepsiburadaPage.searchBoxText.sendKeys(item + Keys.ENTER);

            int sonucAdedi = ReusableMethods.hbSonucAdedi(hepsiburadaPage.foundItemAmount);
            Assert.assertTrue(sonucAdedi > 0, "Sonuc bulunamadi.");

            FileInputStream fileInputStream = new FileInputStream(ConfigReader.getPropert("fileUrl"));
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);


            sheet.getRow(rowNum).getCell(0).setCellValue(item);
            sheet.getRow(rowNum).getCell(1).setCellValue(sonucAdedi);

            FileOutputStream fileOutputStream = new FileOutputStream(ConfigReader.getPropert("fileUrl"));
            workbook.write(fileOutputStream);
            fileInputStream.close();
            fileOutputStream.close();
            workbook.close();

            Driver.quitDriver();

        System.out.println(item);
        System.out.println(rowNum);

    }
}



