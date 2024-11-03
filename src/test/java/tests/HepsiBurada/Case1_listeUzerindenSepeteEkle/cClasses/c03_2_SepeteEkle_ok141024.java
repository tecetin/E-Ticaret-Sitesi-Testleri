package tests.HepsiBurada.Case1_listeUzerindenSepeteEkle.cClasses;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HepsiburadaPage;
import tests.HepsiBurada.Case1_listeUzerindenSepeteEkle.bClasses.b01_KonumFiltresi_ok141024;
import utilities.*;

import java.io.IOException;
import java.time.Duration;

public class c03_2_SepeteEkle_ok141024 extends ExtentReporthb {

    private String getXpathForUrunBilgiKutusu(int i) {
        return "//*[@id='i" + i + "']/div/a/div[2]";
    }

    private String getXpathForSepeteEkleDinamik(int i) {
        return "//*[@id='i" + i + "']/div/a/div[2]/button";
    }


    @Test(dataProvider = "itemIndex", dataProviderClass = DataProviders.class, priority = 1)
    public void SepeteEkle(String item, int rowNum) throws IOException, InterruptedException {

        int i = 6; //listede kaçıncı ürün ile işlem yapılacağını burada belirleyeceğiz, indeks 0'dan başlıyor 0'dan başlıyor
        HepsiburadaPage hb = new HepsiburadaPage();
        Actions actions = new Actions(Driver.getDriver());
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        SoftAssert sa = new SoftAssert();

        //-------------------------------------------------------------------------------------------------------------------------------------------
        //filtre yöntemi : Adres seçimi ve Yarın Kapında
        b01_KonumFiltresi_ok141024 test = new b01_KonumFiltresi_ok141024();

        test.yarinKapinda(item, rowNum); //C02 konum işareti ve yarın kapıda testi çalışıp ürün aratma ve yarın kapındayı işaretleyecek
        //-------------------------------------------------------------------------------------------------------------------------------------------

        //filtre sonucu bulunan ürün adedini yaz ve excele yazdır
        int sonucAdedi = ReusableMethods.hbSonucAdedi(hb.foundItemAmount);
        Assert.assertTrue(sonucAdedi > 0, "Sonuc bulunamadi.");
        extentTest.pass("Konum filtresi sonrası ürün bulunduğu doğrulandı: " + sonucAdedi + " adet.");

        //Excel dosyasına sonuçları kaydet
        ExcelKayit excelKayit = new ExcelKayit();
        excelKayit.excelBilgiKayit(ConfigReader.getProperty("filePath"),
                0, rowNum, 0, item);
        excelKayit.excelBilgiKayit(ConfigReader.getProperty("filePath"),
                0, rowNum, 1, sonucAdedi);
        excelKayit.excelBilgiKayit(ConfigReader.getProperty("filePath"),
                0, rowNum, 2, b01_KonumFiltresi_ok141024.kargoBilgi);
        extentTest.info("Ürün bilgileri excele kaydedilmiştir.");

        //sepete ekle butonu çıkması için ürün üzerine mouse ile git bekle
        WebElement urunKutusu = Driver.getDriver().findElement(By.xpath(getXpathForUrunBilgiKutusu(i)));
        actions.scrollToElement(urunKutusu)
                .moveToElement(urunKutusu)
                .perform();

        //sepete ekle butonu çıkana kadar bekle, mouse ile üzerine tıkla
        WebElement sepeteEkle = Driver.getDriver().findElement(By.xpath(getXpathForSepeteEkleDinamik(i)));
        wait.until(ExpectedConditions.elementToBeClickable(sepeteEkle));
        actions.moveToElement(sepeteEkle)
                .click()
                .perform();
        extentTest.info("Kullanıcı ürün üzerine mouse ile gelerek Sepete Ekle butonunun görünür olmasını sağlar ve tıklar.",
                MediaEntityBuilder.createScreenCaptureFromBase64String(ReusableMethods.WEResmiBase64(urunKutusu)).build());

        //bazı ürünlerde seçenek seçmek gerekiyor, seçenekli ürünlerde sepete bu yöntemle gidilemeyeceği için try catch ile o ürünler atlanıyor
        try {
            wait.until(ExpectedConditions.elementToBeClickable(hb.eklendiMesaji));
            String expMS = "Ürün sepete eklendi";
            String actMsg = hb.eklendiMesaji.getText();
            sa.assertEquals(actMsg, expMS);
            extentTest.pass("Kullanıcı ürünü başarılı bir şekilde sepete eklemiştir.",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(ReusableMethods.WEResmiBase64(hb.eklendiMesaji)).build());

        } catch (Exception e) {
            // Eğer ilk sepete ekleme işlemi başarısız olursa, detay penceresini ele al

            try {
                wait.until(ExpectedConditions.visibilityOf(hb.detayPenceresiSepeteEkle));
                hb.detayPenceresiSepeteEkle.click();

                // Detay penceresinden sepete eklenme durumunu kontrol et
                wait.until(ExpectedConditions.visibilityOf(hb.eklendiMesaji));
                String expText = "Ürün sepete eklendi";
                Assert.assertEquals(hb.eklendiMesaji.getText(), expText, "Ürün detay penceresinden sepete eklenemedi.");

                extentTest.info("Bu üründe seçenek mevcuttur.");
                extentTest.pass("Ürünün başarılı bir şekilde default seçeneklerle sepete eklendiği doğrulanmıştır.",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(ReusableMethods.WEResmiBase64(hb.eklendiMesaji)).build());

            } catch (TimeoutException ex) {

                extentTest.fail("Ürün sepete eklenemedi ve detay penceresi de başarısız oldu.");
            }
        }
    }

    @Test(priority = 2)
    public void sepetKontrol(){

        extentTest = extent.createTest("Sepete Eklenen Ürün Çeşidinin Doğrulanması",
                "Kullanıcı excelde bulunan ürün çeşidi sayısı ile sepette bulunan ürün çeşidi sayısının aynı olduğunu doğrular.");

        try {
            HepsiburadaPage hb = new HepsiburadaPage();
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
            Actions actions = new Actions(Driver.getDriver());
            SoftAssert sa = new SoftAssert();

            extentTest.info("Kullanıcı sepete gider.");
            wait.until(ExpectedConditions.elementToBeClickable(hb.sepeteGit));
            actions.scrollToElement(hb.sepeteGit)
                    .click(hb.sepeteGit)
                    .perform();

            String expUrl = "https://checkout.hepsiburada.com/sepetim";
            wait.until(ExpectedConditions.urlMatches(expUrl));

            String actUrl = Driver.getDriver().getCurrentUrl();
            sa.assertEquals(expUrl, actUrl);
            extentTest.pass("Açılan sayfanın kullanıcı sepeti olduğu doğrulanır.");

            wait.until(ExpectedConditions.visibilityOf(hb.sepettekiUrunCesidi));
            int sepettekiUrunCesidi = Integer.parseInt(hb.sepettekiUrunCesidi.getText().replaceAll("\\D", ""));

            if (sepettekiUrunCesidi > 0) {

                extentTest.pass("Sepette ürün bulunduğu doğrulanmıştır.",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(ReusableMethods.sayfaSSBase64()).build());
                Object[][] excelListesi = ExcelListesiOkuArrayDondur.readExcelData("src/Aranacak_UrunlerveSonuclar.xlsx");
                int veriSayisi = excelListesi.length;
                Assert.assertEquals(sepettekiUrunCesidi, veriSayisi);
                extentTest.pass("Excelde bulunan ürün sayısı ile sepete eklenen ürün sayısının aynı olduğu doğrulanmıştır: " + veriSayisi + " adet.");
            } else {

                extentTest.fail("Sepette ürün bulunamamıştır.");
            }
        } catch (IOException e) {
            extentTest.fail("Sepet Kontrolü yapılamamıştır.");
        }
    }
}


