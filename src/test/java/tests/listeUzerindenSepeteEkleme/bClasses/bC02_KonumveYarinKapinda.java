package tests.listeUzerindenSepeteEkleme.bClasses;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HepsiburadaPage;
import tests.listeUzerindenSepeteEkleme.aClasses.aC01_HbUrunArama;
import utilities.ConfigReader;
import utilities.DataProviders;
import utilities.Driver;
import utilities.ReusableMethods;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;

public class bC02_KonumveYarinKapinda {

    public static int kargoBilgi;
    public boolean shouldCloseDriver2 = true;

    @Test(dataProvider = "itemIndex", dataProviderClass = DataProviders.class)
    public void yarinKapinda(String item, int rowNum) throws IOException {

        HepsiburadaPage hb = new HepsiburadaPage();
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        Actions actions = new Actions(Driver.getDriver());

        //excelde yazan sırayla ürün aratılır ve ürün bulunduğu doğrulanarak sayısı kaydedilir
        aC01_HbUrunArama urunArama = new aC01_HbUrunArama();
        urunArama.shouldCloseDriver = false; //urunAramaTesti çalıştıktan sonra driver kapatılmaması için eklendi
        urunArama.urunAramaTesti(item, rowNum);

        /*
        //C01 testinde benzer şekilde ürün arandıktan sonra pencere bir kaç kere yenilenerek WebElementlerin bulunmasını engelliyor
        //Webelementleri bulamadığı için de NoSuchElement ya da StaleElement hatası veriyor
        //bunu engellemek için konum WE göründüğünde ESC basılıyor yenileme durduruluyor, WE bulunuyor ve WElemente öyle tıklanıyor
        //wait önce element görünene kadar beklediği için NoSuchElement hatasını engelliyor, sonra da webelementi yenileyip StaleElement hatasını engelliyor
        */
        //konuma tıkla
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[3]/div[1]/div/div[2]/div/div/div/div/div/div"))); //hb.konum
        actions.moveToElement(hb.konum).sendKeys(Keys.ESCAPE).click().perform();

        //il izmir seç
        hb.ilSec.click();
        hb.filtre.sendKeys("izmir");
        hb.sec.click();

        //ilçe karabağlar seç
        hb.ilceSec.click();
        hb.filtre.sendKeys("karabağlar");
        hb.sec.click();

        //mahalle poligon seç
        hb.mahalleSec.click();
        hb.filtre.sendKeys("poligon");
        hb.sec.click();

        //adresi kaydet
        hb.kaydet.click();

        String kargoBilgi;
        try {
            //yarın kapında seçeneğini işaretle ve işaretlendiğini doğrula
            wait.until(ExpectedConditions.elementToBeClickable(hb.yarinKapindaClick)).click();
            wait.until(ExpectedConditions.elementToBeSelected(hb.yarinKapindaClick));
            SoftAssert sa = new SoftAssert();
            sa.assertTrue(hb.yarinKapindaClick.isSelected());
            kargoBilgi = "Yarın Kapında";

        } catch (NoSuchElementException | TimeoutException e) {
            kargoBilgi = "Yarın Kapında Değil";
        }

        //filtreden sonra yenilemek gerekiyor test sayfası engellendiği için
        Driver.getDriver().navigate().refresh();

        //pencereyi kapat
        if (shouldCloseDriver2) {
            Driver.quitDriver();
        }
    }
}



