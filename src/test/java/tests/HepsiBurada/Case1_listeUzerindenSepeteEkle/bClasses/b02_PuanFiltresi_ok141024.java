package tests.HepsiBurada.Case1_listeUzerindenSepeteEkle.bClasses;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

import java.time.Duration;

public class b02_PuanFiltresi_ok141024 extends ExtentReporthb {

    private String getXpathForFiltrelenecekPuan(int puan) {
        String path = null;
        switch (puan) {
            case 4:
                path = "//*[@id='puan']//div[1]/label/input";
                break;
            case 3:
                path = "//*[@id='puan']//div[2]/label/input";
                break;
            case 2:
                path = "//*[@id='puan']//div[3]/label/input";
                break;
            case 1:
                path = "//*[@id='puan']//div[4]/label/input";
                break;
            default:
                System.out.println("1 ile 4 arasında geçerli bir puan giriniz.");
                break;
        }
        return path;
    }
    public static int puan = 4;

    @Test(dataProvider = "itemIndex", dataProviderClass = DataProviders.class)
    public void DegerlendirmePuani(String item, int rowNum) {

        HepsiburadaPage hb = new HepsiburadaPage();
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        SoftAssert sa = new SoftAssert();

        //excelde yazan sırayla ürün aratılır ve ürün bulunduğu doğrulanarak sayısı kaydedilir
        a01_UrunArama_ok141024 urunArama = new a01_UrunArama_ok141024();
        urunArama.urunAramaTesti(item, rowNum);

        wait.until(ExpectedConditions.elementToBeClickable(hb.sayfaYuklemeReklami));

        boolean filtre = false;
        int countLoop = 0;
        while (!filtre && countLoop <3) {
            try {
                //Puanı seç
                extentTest.info("Kullanıcı, her ürün için " + puan + " ve üzeri Değerlendirme Puanı için filtreleme yapacaktır.");

                hb = new HepsiburadaPage();
                hb.degerlendirmePuani.click();

                WebElement puanWE = Driver.getDriver().findElement(By.xpath(getXpathForFiltrelenecekPuan(puan)));
                puanWE.click();

                //filtreden sonra yenilemek gerekiyor test sayfası olduğu için
                Driver.getDriver().navigate().refresh();

                hb = new HepsiburadaPage();
                wait.until(ExpectedConditions.visibilityOf(hb.seciliFiltreler));
                String puanFilter = puan + " yıldız ve üzeri";
                sa.assertTrue(hb.seciliFiltreler.getText().contains(puanFilter));
                extentTest.pass("Filtreleme kriteri eklenmiştir: " + puanFilter,
                        MediaEntityBuilder.createScreenCaptureFromBase64String(ReusableMethods.sayfaSSBase64()).build());

                filtre = true;
                countLoop = 3;
            } catch (Exception e) {
                System.out.println("Değerlendirme puanı filtresi eklenemedi.");
                extentTest.fail("Filtreleme yapılamadı");
                countLoop++;
            }
        }

    }
}
