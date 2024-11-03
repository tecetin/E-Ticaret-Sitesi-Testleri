package tests.HepsiBurada.Case1_listeUzerindenSepeteEkle.bClasses;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HepsiburadaPage;
import tests.HepsiBurada.Case1_listeUzerindenSepeteEkle.aClasses.a01_UrunArama_ok141024;
import utilities.DataProviders;
import utilities.Driver;
import utilities.ExtentReporthb;
import utilities.ReusableMethods;

import java.io.IOException;
import java.time.Duration;

public class b01_KonumFiltresi_ok141024 extends ExtentReporthb {

    public static String kargoBilgi = "";
    public static boolean konumOk = false;
    String sehir = "izmir";
    String ilce = "karabağlar";
    String mahalle = "poligon";

    @Test(dataProvider = "itemIndex", dataProviderClass = DataProviders.class)
    public void yarinKapinda(String item, int rowNum) throws IOException, InterruptedException {

        HepsiburadaPage hb = new HepsiburadaPage();
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        Actions actions;
        SoftAssert sa = new SoftAssert();

        //excelde yazan sırayla ürün aratılır ve ürün bulunduğu doğrulanarak sayısı kaydedilir
        a01_UrunArama_ok141024 urunArama = new a01_UrunArama_ok141024();
        urunArama.urunAramaTesti(item, rowNum);

        if (!konumOk) {
            //Driver kapanmadığı sürece konum ilk girildiği gibi kalıyor
            //konuma tıkla
            extentTest.info("Kullanıcı her oturum için bir defa konum ayarlarını yapacaktır.");

            wait.until(ExpectedConditions.elementToBeClickable(hb.sayfaYuklemeReklami));

            hb = new HepsiburadaPage();
            wait.until(ExpectedConditions.elementToBeClickable(hb.konum));

            actions = new Actions(Driver.getDriver());
            actions
                    .moveToElement(hb.konum)
                    .click()
                    .perform();

            wait.until(ExpectedConditions.elementToBeClickable(hb.ilSec));
            hb = new HepsiburadaPage();
            //il seç
            hb.ilSec.click();
            hb.filtre.sendKeys(sehir);
            hb.sec.click();

            //ilçe seç
            hb.ilceSec.click();
            hb.filtre.sendKeys(ilce);
            hb.sec.click();

            //mahalle seç
            hb.mahalleSec.click();
            hb.filtre.sendKeys(mahalle);
            hb.sec.click();

            //adresi kaydet
            hb.kaydet.click();

            //filtreden sonra yenilemek gerekiyor test sayfası olduğu için
            Driver.getDriver().navigate().refresh();

            konumOk = true;

            extentTest.pass("Kullanıcı konumu şehir, ilçe, mahalle olarak ayarlamıştır: "+ sehir + ", " + ilce + ", "  + mahalle,
                    MediaEntityBuilder.createScreenCaptureFromBase64String(ReusableMethods.sayfaSSBase64()).build());
        }

        try {
            //yarın kapında seçeneğini işaretle ve işaretlendiğini doğrula
            wait.until(ExpectedConditions.elementToBeClickable(hb.yarinKapindaClick)).click();
            wait.until(ExpectedConditions.elementToBeSelected(hb.yarinKapindaClick));

            sa.assertTrue(hb.yarinKapindaClick.isSelected());
            kargoBilgi = "Yarın Kapında";
            extentTest.info("Aranan üründe yarın kapında seçeneği mevcuttur ve seçilmiştir.");

            sa.assertTrue(hb.yarinKapindaClick.isSelected());
            extentTest.pass("Yarın kapında seçeneğinin işaretli olduğu doğrulanmıştır.",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(ReusableMethods.sayfaSSBase64()).build());

        }
        catch (NoSuchElementException | TimeoutException e) {
            kargoBilgi = "Yarın Kapında Değil";
            extentTest.info("Aranan üründe yarın kapında seçeneği mevcut değildir.");
        }
    }
}



