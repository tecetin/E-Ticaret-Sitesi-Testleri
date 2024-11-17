package tests.LCW;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LCWPage;
import utilities.ConfigReader;
import utilities.Driver;
import utilities.ExtentReportlcw;
import utilities.ReusableMethods;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class LCWSepeteEkle extends ExtentReportlcw {

    static String item;

    private static boolean cookieAccepted = false;

    List<String> alinacakUrunler = new ArrayList<>();

    LCWPage page = new LCWPage();
    WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
    Actions actions = new Actions(Driver.getDriver());

    private String getXpathForUrunKutusu(int i) {
        return "//*[@id='root']/div/div[2]/div[1]/div[6]/div/div[2]/div[" + i + "]";
    }

    @Test(priority = 1)
    public void anasyafayaGit() {

        extentTest = extent.createTest("Pazarama Ürün Bulma ve Sepete Ürün Ekleme",
                "Kullanıcı excelde bulunan ürün listesini bulur ve sepete ekler.");

        Driver.getDriver().get(ConfigReader.getProperty("lcwUrl"));
        extentTest.info("Kullanıcı anasayfaya gider");

        alinacakUrunler.add("Elbise");
        alinacakUrunler.add("Gömlek");
        alinacakUrunler.add("Pantolon");

        for (int i = 0; i < alinacakUrunler.size(); i++) {

            item = alinacakUrunler.get(i);

            urunAra();
        }
    }

    public void urunAra() {

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
            wait.until(ExpectedConditions.elementToBeClickable(page.searchBar)).click();

            actions.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
            actions.sendKeys(Keys.DELETE).perform();

            actions.sendKeys(page.searchBar, item + Keys.ENTER)
                    .perform();
        } catch (Exception e) {
            Driver.getDriver().navigate().refresh();
            wait.until(ExpectedConditions.elementToBeClickable(page.searchBar));
            actions.sendKeys(page.searchBar, item + Keys.ENTER)
                    .perform();
        }

        String actKategori = page.kategoriYazisi.getText().toLowerCase();
        String expKategori = item.toLowerCase();

        // Doğru itemin arama sonucu verildiğini doğrula
        Assert.assertTrue(actKategori.contains(expKategori));
        extentTest.pass("Aranan ürün: " + item + " kategorisine geçildiği doğrulanmıştır.",
                MediaEntityBuilder.createScreenCaptureFromBase64String(ReusableMethods.sayfaSSBase64()).build());

        // Arama sonucunda ürün çıktığını doğrula
        int sonuc = ReusableMethods.sonucSayisi(page.kategoriSonucYazisi);
        Assert.assertTrue(sonuc > 0, "Sonuc bulunamadi.");
        extentTest.pass("Aranan ürün: " + item + " kategorisinde ürün bulunduğu doğrulanmıştır.");
        extentTest.info("Aranan ürün: " + item + " kategorisinde " + sonuc + " adet ürün bulunmaktadır.",
                MediaEntityBuilder.createScreenCaptureFromBase64String(ReusableMethods.WEResmiBase64(page.kategoriSonucYazisi)).build());

    }

    public void uruneGit() {

        int i = 2; //2. ürüne gidilecektir.




    //sepete ekle "SEPETE EKLENDİ" oluyor


}

}
