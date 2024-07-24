package tests.listeUzerindenSepeteEkleme.bClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HepsiburadaPage;
import tests.listeUzerindenSepeteEkleme.aClasses.aC01_HbUrunArama;
import utilities.DataProviders;
import utilities.Driver;
import utilities.ReusableMethods;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class bC04_xPuanUzeriUrunSecimi {

    public boolean shouldCloseDriver4 = true;
    public int puan;
    private String getXpathForFiltrelenecekPuan (int puan) {
        String path = null;
        switch (puan){
            case 4 :
                path = "//*[@id='puan']/div/div/div/div/div/div/div[1][@data-test-id='not_checked']";
                break;
            case 3 :
                path = "//*[@id='puan']/div/div/div/div/div/div/div[2][@data-test-id='not_checked']";
                break;
            case 2 :
                path = "//*[@id='puan']/div/div/div/div/div/div/div[3][@data-test-id='not_checked']";
                break;
            case 1 :
                path = "//*[@id='puan']/div/div/div/div/div/div/div[4][@data-test-id='not_checked']";
                break;
            default:
                System.out.println("1 ile 4 arasında geçerli bir puan giriniz.");
                break;
        }
        return path;
    }

    @Test(dataProvider = "itemIndex", dataProviderClass = DataProviders.class)
    public void DegerlendirmePuani(String item, int rowNum) throws IOException {

        HepsiburadaPage hb = new HepsiburadaPage();
        Actions actions = new Actions(Driver.getDriver());
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(5));
        SoftAssert sa = new SoftAssert();

        //excelde yazan sırayla ürün aratılır ve ürün bulunduğu doğrulanarak sayısı kaydedilir
        aC01_HbUrunArama urunArama = new aC01_HbUrunArama();
        urunArama.shouldCloseDriver = false; //urunAramaTesti çalıştıktan sonra driver kapatılmaması için eklendi
        urunArama.urunAramaTesti(item, rowNum);

        //Puan listesini açmak için Değerlendirme Puanına Tıkla - sayfayı yeniliyor bulamıyor, engelleyici eklendi
        try {
            hb.degerlendirmePuani.click();
        } catch (Exception e) {
            hb.degerlendirmePuani.click();
        }

        //Puanı seç
        puan = 3;
        actions.scrollToElement(hb.puanListesi).perform();

        try {
            actions.click(Driver.getDriver().findElement(By.xpath(getXpathForFiltrelenecekPuan(puan)))).perform();
            Driver.getDriver().navigate().refresh();

            //seçilen tüm filtreler liste halinde alınır
            List<WebElement> filtreler = Driver.getDriver().findElements(By.className("appliedVerticalFilter-rxdhhFDFaJiRVL0RqUW_"));

            List<String> filtrelerIsim = ReusableMethods.toStringList(filtreler);
            String kategori = puan + " yıldız ve üzeri";
            sa.assertTrue(filtreler.contains(kategori), puan + " puan ve üzeri filtrelenememiştir.");
        } catch (Exception e) {
            System.out.println("Filtreleme yapılamadı");
            ReusableMethods.wait(2);
        }


        if (shouldCloseDriver4) {
            Driver.quitDriver();
        }
}}
