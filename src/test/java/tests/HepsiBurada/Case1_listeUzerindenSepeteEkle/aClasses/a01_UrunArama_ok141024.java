//package tests.Case1_listeUzerindenSepeteEkle.aClasses;
//
//import org.openqa.selenium.Keys;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.annotations.Test;
//import pages.HepsiburadaPage;
//import utilities.ConfigReader;
//import utilities.DataProviders;
//import utilities.Driver;
//import utilities.ExtentReporthb;
//
//import java.io.IOException;
//import java.time.Duration;
//
//public class a01_UrunArama_ok141024 extends ExtentReporthb {
//
//    private static boolean cookieAccepted = false;
//
//    public boolean shouldCloseDriver = true;
//
//    @Test(dataProvider = "itemIndex", dataProviderClass = DataProviders.class)
//    public void urunAramaTesti(String item, int rowNum) throws IOException, InterruptedException {
//
//        HepsiburadaPage hb = new HepsiburadaPage();
//        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(20));
//        Actions actions = new Actions(Driver.getDriver());
//
//        Driver.getDriver().get(ConfigReader.getProperty("hbUrl"));
//
//        // Cookie kabul durumunu kontrol eden statik bir değişken
//        extentTest.info("Cookie mecutsa kullanıcı kabul eder.");
//        if (!cookieAccepted) {
//            boolean cookie = hb.acceptCookies.isEnabled();
//            if (cookie) {
//                hb.acceptCookies.click();
//                extentTest.info("Açılan cookie kabul edildi.");
//            }
//            cookieAccepted = true;  // Cookie yalnızca bir kez kabul edilir
//        }
//
//        /*
//        //cookie kabul edildikten sonra sayfa kendini yeniliyor
//        //Webelementleri bulamadığı için de NoSuchElement ya da StaleElement hatası veriyor
//        //bunu engellemek için WE göründüğünde ESC basılıyor yenileme durduruluyor, WE bulunuyor ve WElemente öyle tıklanıyor
//        //waitler önce element görünene kadar beklediği için NoSuchElement hatasını engelliyor, sonra da webelementi yenileyip StaleElement hatasını engelliyor
//        */
//        //arama kutusuna tıklayıp açılan input elementine değer atanıyor
//        //işlem yapılana kadar sayfa yenilenecek
////        extentTest.info("Kullanıcı arama kutusuna tıklar ve listede bulunan '" + item + "' ürününü arar.");
//        hb = new HepsiburadaPage();
//
//        actions.sendKeys(Keys.ESCAPE).perform();
//        actions
//                .sendKeys(hb.searchBox, item + Keys.ENTER)
//                .perform();
//    }
//}
//

package tests.HepsiBurada.Case1_listeUzerindenSepeteEkle.aClasses;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HepsiburadaPage;
import utilities.*;

import java.time.Duration;

public class a01_UrunArama_ok141024 extends ExtentReporthb {

    private static boolean cookieAccepted = false;
    SoftAssert sa = new SoftAssert();

    @Test(dataProvider = "itemIndex", dataProviderClass = DataProviders.class)
    public void urunAramaTesti(String item, int rowNum) {

        try {
            extentTest = extent.createTest("Hepsiburada "+ item +" ürününü Bulma ve Sepete Ekleme",
                    "Kullanıcı excelde bulunan ürün listesini bulur ve sepete ekler.");
        } catch (Exception e) {
            System.out.println("Extent başlatılamadı.");
        }

        HepsiburadaPage hb = new HepsiburadaPage();
        Actions actions;
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));

        Driver.getDriver().get(ConfigReader.getProperty("hbUrl"));

        // Cookie kabul durumunu kontrol eden statik bir değişken
        extentTest.info("Cookie mevcutsa kullanıcı kabul eder.");
        if (!cookieAccepted) {
            boolean cookie = hb.acceptCookies.isEnabled();
            if (cookie) {
                hb.acceptCookies.click();
                extentTest.info("Açılan cookie kabul edildi.");
                ReusableMethods
                        .wait(1);
            }
            cookieAccepted = true;  // Cookie yalnızca bir kez kabul edilir
        }

        // Arama işlemi öncesinde sayfanın yenilenmesini bekliyoruz, reklam en son yükleniyor
        wait.until(ExpectedConditions.elementToBeClickable(hb.sayfaYuklemeReklami));

        // Arama kutusuna ürünü yazıp Enter tuşuna basıyoruz
        extentTest.info("Kullanıcı arama kutusuna tıklar ve listede bulunan '" + item + "' ürününü arar.");

        boolean searched = false;
        while (!searched) {

            //Devamlı sayfa yenilemesi nedeniyle tüm işlemler tek tek ayrılmıştır.
            hb = new HepsiburadaPage(); //StaleElementReferenceException Handling
            actions = new Actions(Driver.getDriver()); //StaleElementReferenceException Handling

            actions
                    .click(hb.searchBox)
                    .perform();

            try {
                hb = new HepsiburadaPage(); //StaleElementReferenceException Handling
                actions = new Actions(Driver.getDriver()); //StaleElementReferenceException Handling
                actions
                        .sendKeys(hb.searchBox, item)
                        .perform();

                hb = new HepsiburadaPage(); //StaleElementReferenceException Handling
                actions = new Actions(Driver.getDriver()); //StaleElementReferenceException Handling
                actions
                        .sendKeys(hb.searchBox, Keys.ENTER)
                        .perform();
            }
            catch (Exception e) {
                wait.until(ExpectedConditions.elementToBeClickable(hb.sayfaYuklemeReklami));
                hb = new HepsiburadaPage(); //StaleElementReferenceException Handling
                actions = new Actions(Driver.getDriver()); //StaleElementReferenceException Handling
                actions
                        .sendKeys(hb.searchBox, item)
                        .perform();

                hb = new HepsiburadaPage(); //StaleElementReferenceException Handling
                actions = new Actions(Driver.getDriver()); //StaleElementReferenceException Handling
                actions
                        .sendKeys(hb.searchBox, Keys.ENTER)
                        .perform();
            }
            wait.until(ExpectedConditions.visibilityOf(Driver.getDriver().findElement(By.xpath("//h1"))));
            searched = true;
        }

        String head = Driver.getDriver().getTitle().toLowerCase();
        sa.assertTrue(head.contains(item.toLowerCase()));
        extentTest.pass("Açılan sayfanın başlığı aranan ürün " + item + " içerdiği doğrulanmıştır.",
                MediaEntityBuilder.createScreenCaptureFromBase64String(ReusableMethods.sayfaSSBase64()).build());

        int sonuc = ReusableMethods.hbSonucAdedi(hb.foundItemAmount);
        sa.assertTrue(sonuc > 0);
        extentTest.info(item + " kategorisinde " + sonuc + " adet ürün bulunmuştur.");
    }
}
