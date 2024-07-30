package tests.listeUzerindenSepeteEkle.bClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HepsiburadaPage;
import tests.listeUzerindenSepeteEkle.aClasses.aC01_HbUrunArama;
import utilities.DataProviders;
import utilities.Driver;

import java.io.IOException;
import java.time.Duration;

public class bC04_xPuanUzeriUrunSecimi {

    public static int puan = 1;
    public boolean shouldCloseDriver4 = true;

    private String getXpathForFiltrelenecekPuan(int puan) {
        String path = null;
        switch (puan) {
            case 4:
                path = "//*[@id='puan']/div/div/div/div/div/div/div[1][@data-test-id='not_checked']";
                break;
            case 3:
                path = "//*[@id='puan']/div/div/div/div/div/div/div[2][@data-test-id='not_checked']";
                break;
            case 2:
                path = "//*[@id='puan']/div/div/div/div/div/div/div[3][@data-test-id='not_checked']";
                break;
            case 1:
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
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        SoftAssert sa = new SoftAssert();

        //excelde yazan sırayla ürün aratılır ve ürün bulunduğu doğrulanarak sayısı kaydedilir
        aC01_HbUrunArama urunArama = new aC01_HbUrunArama();
        urunArama.shouldCloseDriver = false; //urunAramaTesti çalıştıktan sonra driver kapatılmaması için eklendi
        urunArama.urunAramaTesti(item, rowNum);

        //Puanı seç
        boolean loop = true;
        while (loop) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(hb.degerlendirmePuani));
                actions.click(hb.degerlendirmePuani)
                        .scrollToElement(hb.puanListesi)
                        .click(Driver.getDriver().
                                findElement(By.xpath(getXpathForFiltrelenecekPuan(puan))))
                        .perform();
                loop = false;
            } catch (Exception e) {
                System.out.println("Değerlendirme puanı filtresi eklenemedi.");
            }

            Driver.getDriver().navigate().refresh();

            if (shouldCloseDriver4) {
                Driver.quitDriver();
            }
        }
    }
}
