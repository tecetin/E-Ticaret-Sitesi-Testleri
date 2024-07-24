package tests.listeUzerindenSepeteEkleme.cClasses;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import pages.HepsiburadaPage;
import tests.listeUzerindenSepeteEkleme.bClasses.bC04_xPuanUzeriUrunSecimi;
import utilities.ConfigReader;
import utilities.DataProviders;
import utilities.Driver;
import utilities.ReusableMethods;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;

public class cC03_4_SepeteEkleIsimveFiyatDogrula {

    public boolean shouldCloseDriver3 = true;


    private String getXpathForUrunBilgiKutusu(int i)
    {
        return "//*[@id='i" + (i-1) + "']";
    }

    private String getXpathForSepeteEkleDinamik(int i) {
        return "//*[@id='i" + (i-1) + "']/div/a/div[2]/button";
    }

    private String getXpathForUrunFiyatDinamik(int i) {
        return "(//*[@data-test-id='price-current-price'])[" + i + "]";
    }

    private String getXpathForUrunIsimDinamik(int i) {
        return "(//h3[@type='comfort'][@data-test-id='product-card-name'])[" + i + "]";
    }

    @Test(dataProvider = "itemIndex", dataProviderClass = DataProviders.class)
    public void SepeteEkle(String item, int rowNum) throws IOException {

        int i = 6; //listede kaçıncı ürün ile işlem yapılacağını burada belirleyeceğiz
        HepsiburadaPage hb = new HepsiburadaPage();
        Actions actions = new Actions(Driver.getDriver());
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(1));
        SoftAssert sa = new SoftAssert();

        //excele bilgi işlemek için hazırlık
        FileInputStream fileInputStream = new FileInputStream(ConfigReader.getProperty("filePath"));
        Workbook workbook = WorkbookFactory.create(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);

        //-------------------------------------------------------------------------------------------------------------------------------------------
        //filtre yöntemi : Değerlendirme Puanı seçimi
        bC04_xPuanUzeriUrunSecimi test1ve4 = new bC04_xPuanUzeriUrunSecimi();

        test1ve4.puan = 4;
        test1ve4.shouldCloseDriver4 = false; //C04 çalıştıktan sonra driver kapanmaması için false
        test1ve4.DegerlendirmePuani(item, rowNum); // C04.DegerlendirmePuani testi ileyazılan puana göre filtreleme yapacaktır.
        //-------------------------------------------------------------------------------------------------------------------------------------------

        //filtre sonucu bulunan ürün adedini yaz ve excele yazdır
        int sonucAdedi = ReusableMethods.hbSonucAdedi(hb.foundItemAmount);
        Assert.assertTrue(sonucAdedi > 0, "Sonuc bulunamadi.");

        sheet.getRow(rowNum).getCell(0).setCellValue(item);
        sheet.getRow(rowNum).getCell(1).setCellValue(sonucAdedi);

        //ürünün listedeki isim ve fiyatını kaydet
        WebElement urunKutusu = null;

        //sepete ekle butonu çıkması için ürün üzerine mouse ile git bekle

        try {
            urunKutusu = Driver.getDriver().findElement(By.xpath(getXpathForUrunBilgiKutusu(i)));
            urunKutusu.isDisplayed();
        } catch (Exception e) {;
            i=1;
            urunKutusu = Driver.getDriver().findElement(By.xpath(getXpathForUrunBilgiKutusu(i)));
        }

        actions.scrollToElement(urunKutusu)
                .moveToElement(urunKutusu)
                .perform();

        String isim = Driver.getDriver()
                .findElement(By.xpath(getXpathForUrunIsimDinamik(i)))
                .getText();

        String fiyat = Driver.getDriver()
                .findElement(By.xpath(getXpathForUrunFiyatDinamik(i)))
                .getText();

        //sepete ekle butonu çıkana kadar bekle, mouse ile üzerine tıkla
        actions.moveToElement(Driver.getDriver().findElement(By.xpath(getXpathForSepeteEkleDinamik(i))))
                .click()
                .perform();

        //bazı ürünlerde seçenek seçmek gerekiyor, seçenekli ürünlerde sepete bu yöntemle gidilemeyeceği için try catch ile o ürünler atlanıyor
        try {
            wait.until(ExpectedConditions.elementToBeClickable(hb.eklendiMesaji));
            String expMS = "Ürün sepete eklendi";
            String actMsg = hb.eklendiMesaji.getText();
            sa.assertEquals(actMsg, expMS);
            hb.mesajiKapat.click();
            sheet.getRow(rowNum).getCell(3).setCellValue("Ürün tek çeşit.");
        } catch (Exception e) {
            System.out.println(item + " ürünü sepete ekleme sırasında açılan pencere nedeniyle sepete eklenemedi.");
            actions.sendKeys(Keys.ESCAPE).perform();
            sheet.getRow(rowNum).getCell(3).setCellValue("Üründe farklı çeşitler mevcut.");
        }

        wait.until(ExpectedConditions.elementToBeClickable(hb.sepeteGit))
                .click();

        boolean sepetBos = true;
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("basket_payedPrice"))); //sepet toplamının görünür olmasını bekle
            hb.sepetToplami.isDisplayed();
        } catch (Exception e) {
            sepetBos = false;
        }

        if (sepetBos) {
            String sepetToplami = hb.sepetToplami.getText();
            sa.assertEquals(fiyat, sepetToplami);
        } else {
            System.out.println("Sepette ürün bulunmamaktadır:" + item);
        }

        //Sepetin ekran görüntüsünü al
        ReusableMethods.pageScreenShot(Driver.getDriver(), item+" SepetSS");



        //excel yazıldıktan sonra kayıt ve kapatma işlemi
        FileOutputStream fileOutputStream = new FileOutputStream(ConfigReader.getProperty("filePath"));
        workbook.write(fileOutputStream);
        fileInputStream.close();
        fileOutputStream.close();
        workbook.close();
        //pencereyi kapat
        if (shouldCloseDriver3) {
            Driver.quitDriver();
        }
    }


}


