package tests.Pazarama;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.PazaramaPage;
import utilities.*;

import java.io.IOException;
import java.time.Duration;

public class PazaramaSepeteEkle extends ExtentReportp {

    private static boolean cookieAccepted = false;
    Actions actions = new Actions(Driver.getDriver());

    public static int sonucSayisi(WebElement element) {
        PazaramaPage page = new PazaramaPage();
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOf(element));
        String sonucYazisi = element.getText();

        return Integer.parseInt(sonucYazisi.replaceAll("\\D", ""));
    }

    @Test(dataProvider = "itemIndex", dataProviderClass = DataProviders.class, priority = 1)
    public void urunAraSepeteEkle(String item, int rowNum) throws IOException {

        try {
            extentTest = extent.createTest("Pazarama Ürün Bulma ve Sepete Ürün Ekleme",
                    "Kullanıcı excelde bulunan ürün listesini bulur ve sepete ekler.");

            Driver.getDriver().get(ConfigReader.getProperty("pzrmUrl"));
            extentTest.info("Kullanıcı anasayfaya gider");

            urunAra(item);

            sepeteEkle(1); //1. urunu sepet ekle
        } catch (IOException e) {
            extentTest.fail("Sepete ürün ekleme işleminde hata oluşmuştur.");
        }

    }

    //------------------------------------------------------------------------------------------------------------------------

    @Test(priority = 2)
    public void sepetKontrol() throws IOException {

        try {
            PazaramaPage page = new PazaramaPage();
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));

            extentTest.info("Kullanıcı sepete gider.");
            wait.until(ExpectedConditions.elementToBeClickable(page.sepetim));
            actions.scrollToElement(page.sepetim)
                    .click(page.sepetim)
                    .perform();

            String expUrl = "https://www.pazarama.com/sepetim";
            wait.until(ExpectedConditions.urlMatches(expUrl));

            String actUrl = Driver.getDriver().getCurrentUrl();
            Assert.assertEquals(expUrl, actUrl);
            extentTest.pass("Açılan sayfanın kullanıcı sepeti olduğu doğrulanır.");

            wait.until(ExpectedConditions.visibilityOf(page.sepettekiUrunSayisi));
            int sepettekiUrunSayisi = sonucSayisi(page.sepettekiUrunSayisi);
            if (sepettekiUrunSayisi > 0) {

                extentTest.pass("Sepette ürün bulunduğu doğrulanmıştır.",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(ReusableMethods.sayfaSSBase64()).build());
                Object[][] excelListesi = ExcelListesiOkuArrayDondur.readExcelData("src/Aranacak_UrunlerveSonuclar.xlsx");
                int veriSayisi = excelListesi.length;
                Assert.assertEquals(sepettekiUrunSayisi, veriSayisi);
                extentTest.pass("Excelde bulunan ürün sayısı ile sepete eklenen ürün sayısının aynı olduğu doğrulanmıştır: " + veriSayisi + " adet.");
            } else {

                extentTest.fail("Sepette ürün bulunamamıştır.");
            }
        } catch (IOException e) {
            extentTest.fail("Sepet Kontrolü yapılamamıştır.");
        }
    }

    @Test(priority = 3)
    public void sepettenUrunSil() {
        try {
            PazaramaPage page = new PazaramaPage();
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));

            extentTest.info("Kullanıcı tümünü sil butonuna basar.");

            actions.moveToElement(page.tumunuSil)
                    .click()
                    .perform();

            wait.until(ExpectedConditions.elementToBeClickable(page.silOnay)).click();
            extentTest.info("Onay kutucuğunun çıktığı görülür ve silme işlemi onaylanır.",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(ReusableMethods.sayfaSSBase64()).build());

            wait.until(ExpectedConditions.visibilityOf(page.urunBulunmamaktadir));

            String expText = "Sepetinizde ürün bulunmamaktadır.";
            Assert.assertEquals(page.urunBulunmamaktadir.getText(), expText);
            extentTest.pass("Sepetin boş olduğu doğrulanır.",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(ReusableMethods.sayfaSSBase64()).build());
        } catch (Exception e) {
            extentTest.fail("Sepetten ürün silme işlemi yapılamamıştır.");
        }

    }

    private String listedenSepeteEkleDinamikXpath(int i) { //i 1den baslayacak
        return "//div[3]/div[1]/div[" + i + "]/div/button";
    }

    //------METHODS-----------------------------------------------------------------------------------------------------------

    private String sepeteEklendi(int i) {
        return "//div[3]/div[1]/div[" + i + "]/div/button/span[2]";
    }

    public void urunAra(String item) throws IOException {

        PazaramaPage page = new PazaramaPage();
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));

        // Cookie kabul durumunu kontrol eden statik bir değişken
        extentTest.info("Cookie mecutsa kullanıcı kabul eder.");
        if (!cookieAccepted) {
            boolean cookie = page.acceptCookies.isEnabled();
            if (cookie) {
                page.acceptCookies.click();
                extentTest.info("Açılan cookie kabul edildi.");
            }
            cookieAccepted = true;  // Cookie yalnızca bir kez kabul edilir
        }


        // Arama kutusuna tıklayıp itemi yazıp arat, bazen sayfa kendini yeniliyor ve element bulunamıyor, sayfa kontrollü yenilenerek element aranıyor
        try {
            extentTest.info("Kullanıcı arama kutusuna tıklar ve listede bulunan '" + item + "' ürününü arar.");
            wait.until(ExpectedConditions.elementToBeClickable(page.searchBar));
            actions.sendKeys(page.searchBar, item + Keys.ENTER)
                    .perform();
        } catch (Exception e) {
            Driver.getDriver().navigate().refresh();
            wait.until(ExpectedConditions.elementToBeClickable(page.searchBar));
            actions.sendKeys(page.searchBar, item + Keys.ENTER)
                    .perform();
        }

        WebElement urun = Driver.getDriver().findElement(By.xpath(listedenSepeteEkleDinamikXpath(1))); //1. urun gorunmeden devam etmesin
        wait.until(ExpectedConditions.visibilityOf(urun));

        String expUrl = "https://www.pazarama.com/arama";
        wait.until(ExpectedConditions.urlContains(expUrl));

        String actKategori = page.kategoriYazisi.getText().toLowerCase();
        String expKategori = item.toLowerCase();

        // Doğru itemin arama sonucu verildiğini doğrula
        Assert.assertTrue(actKategori.contains(expKategori));
        extentTest.pass("Aranan ürün kategorisine geçildiği doğrulanmıştır.",
                MediaEntityBuilder.createScreenCaptureFromBase64String(ReusableMethods.sayfaSSBase64()).build());

        // Arama sonucunda ürün çıktığını doğrula
        int sonuc = sonucSayisi(page.kategoriSonucYazisi);
        Assert.assertTrue(sonuc > 0, "Sonuc bulunamadi.");
        extentTest.pass("Aranan ürün kategorisinde ürün bulunduğu doğrulanmıştır.");
        extentTest.info("Aranan ürün kategorisinde " + sonuc + " adet ürün bulunmaktadır.",
                MediaEntityBuilder.createScreenCaptureFromBase64String(ReusableMethods.WEResmiBase64(page.kategoriSonucYazisi)).build());

    }

    public void sepeteEkle(int i) throws IOException {
        Actions actions = new Actions(Driver.getDriver());
        PazaramaPage page = new PazaramaPage();
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));

        // Dinamik Xpath ile sepete ekle butonunu bul
        extentTest.info(i + ". sıradaki ürün sepete eklenecektir.");
        WebElement sepeteEkle = Driver.getDriver().findElement(By.xpath(listedenSepeteEkleDinamikXpath(i)));
        actions.scrollToElement(sepeteEkle).perform();
        wait.until(ExpectedConditions.elementToBeClickable(sepeteEkle));
        actions.moveToElement(sepeteEkle)
                .click()
                .perform();

        WebElement sepeteEklendi = null;
        boolean sepeteEklendiMi = false;

        // Sepete eklenme durumunu kontrol et
        try {
            sepeteEklendi = Driver.getDriver().findElement(By.xpath(sepeteEklendi(i)));
            wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(2));
            sepeteEklendiMi = wait.until(ExpectedConditions.visibilityOf(sepeteEklendi)).isDisplayed();

        } catch (Exception e) {
            extentTest.info("Bazı ürünler seçenekli olduğu için sepete ekleme sırasında açılır pencere açılabilmektedir.");
        }

        if (sepeteEklendiMi) { //seçenek penceresi açılmadıysa

            Assert.assertTrue(sepeteEklendiMi, "Ürün sepete eklenemedi.");

            extentTest.info("Bu üründe seçenek mevcut değildir.");
            extentTest.pass("Ürünün başarılı bir şekilde sepete eklendiği doğrulanmıştır.",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(ReusableMethods.WEResmiBase64(sepeteEklendi)).build());

        } else { //seçenek penceresi açıldıysa

            WebElement secenekPenceresi = Driver.getDriver().findElement(By.xpath("//*[@id='app']/div[2]/div"));

            page.detayPenceresiSepeteEkle.click();

            // Detay penceresinden sepete eklenme durumunu kontrol et
            wait.until(ExpectedConditions.visibilityOf(page.penceredenSepeteEklendi));
            String expText = "Sepete Eklendi!";
            Assert.assertEquals(page.penceredenSepeteEklendi.getText(), expText, "Ürün detay penceresinden sepete eklenemedi.");

            extentTest.info("Bu üründe seçenek mevcuttur.");
            extentTest.pass("Ürünün başarılı bir şekilde default seçeneklerle sepete eklendiği doğrulanmıştır.",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(ReusableMethods.WEResmiBase64(secenekPenceresi)).build());

        }
    }
}




