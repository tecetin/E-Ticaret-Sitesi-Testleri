package tests.Case1_listeUzerindenSepeteEkle.cClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HepsiburadaPage;
import tests.Case1_listeUzerindenSepeteEkle.bClasses.bC04_xPuanUzeriUrunSecimi;
import utilities.*;

import java.io.IOException;
import java.time.Duration;

public class cC03_4_SepeteEkleFiyatKaydet {

    public boolean shouldCloseDriver3 = true;


    private String getXpathForUrunBilgiKutusu(int i) {
        return "//*[@id='i" + (i - 1) + "']";
    }

    private String getXpathForSepeteEkleDinamik(int i) {
        return "//*[@id='i" + (i - 1) + "']/div/a/div[2]/button";
    }

    private String getXpathForUrunFiyatDinamik(int i) {
        return "(//*[@data-test-id='price-current-price'])[" + i + "]";
    }

    @Test(dataProvider = "itemIndex", dataProviderClass = DataProviders.class)
    public void SepeteEkle(String item, int rowNum) throws IOException {

        int i = 11; //listede kaçıncı ürün ile işlem yapılacağını burada belirleyeceğiz
        HepsiburadaPage hb = new HepsiburadaPage();
        Actions actions = new Actions(Driver.getDriver());
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        SoftAssert sa = new SoftAssert();

        //-------------------------------------------------------------------------------------------------------------------------------------------
        //filtre yöntemi : Değerlendirme Puanı seçimi
        bC04_xPuanUzeriUrunSecimi test1ve4 = new bC04_xPuanUzeriUrunSecimi();

        bC04_xPuanUzeriUrunSecimi.puan = 4;
        test1ve4.shouldCloseDriver4 = false; //C04 çalıştıktan sonra driver kapanmaması için false
        test1ve4.DegerlendirmePuani(item, rowNum); // C04.DegerlendirmePuani testi ileyazılan puana göre filtreleme yapacaktır.
        //-------------------------------------------------------------------------------------------------------------------------------------------

        //filtre sonucu bulunan ürün adedini yaz ve excele yazdır
        int sonucAdedi = ReusableMethods.hbSonucAdedi(hb.foundItemAmount);
        Assert.assertTrue(sonucAdedi > 0, "Sonuc bulunamadi.");

        ExcelKayit excelKayit = new ExcelKayit();
        excelKayit.excelBilgiKayit(ConfigReader.getProperty("filePath"),
                0, rowNum, 0, item);
        excelKayit.excelBilgiKayit(ConfigReader.getProperty("filePath"),
                0, rowNum, 1, sonucAdedi);

        //ürünün listedeki isim ve fiyatını kaydet
        WebElement urunKutusu = null;

        //sepete ekle butonu çıkması için ürün üzerine mouse ile git bekle

        try {
            urunKutusu = Driver.getDriver().findElement(By.xpath(getXpathForUrunBilgiKutusu(i)));
            actions.scrollToElement(urunKutusu)
                    .moveToElement(urunKutusu)
                    .perform();
        } catch (Exception e) {
            i = 1;
            urunKutusu = Driver.getDriver().findElement(By.xpath(getXpathForUrunBilgiKutusu(i)));
            actions.scrollToElement(urunKutusu)
                    .moveToElement(urunKutusu)
                    .perform();
        }

        //fiyatını excele kaydet
        String fiyat = Driver.getDriver()
                .findElement(By.xpath(getXpathForUrunFiyatDinamik(i)))
                .getText();
        excelKayit.excelBilgiKayit(ConfigReader.getProperty("filePath"),
                0, rowNum, 4, fiyat);

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
            excelKayit.excelBilgiKayit(ConfigReader.getProperty("filePath"),
                    0, rowNum, 3, "Ürün tek çeşit.");

            wait.until(ExpectedConditions.elementToBeClickable(hb.sepeteGit))
                    .click();

            //sepete eklenenin aranan item kelimesini içerdiğini doğrula
            String urunAdi = hb.urunAdi.getText();
            sa.assertTrue(urunAdi.contains(item));

            //Sepetin ekran görüntüsünü al
            ReusableMethods.pageScreenShot(Driver.getDriver(), item + " SepetSS");

        } catch (Exception e) {
            System.out.println(item + " ürünü sepete ekleme sırasında açılan pencere nedeniyle sepete eklenemedi.");
            excelKayit.excelBilgiKayit(ConfigReader.getProperty("filePath"),
                    0, rowNum, 3, "Üründe farklı çeşitler mevcut.");
        }

        //pencereyi kapat
        if (shouldCloseDriver3) {
            Driver.quitDriver();
        }
    }


}


