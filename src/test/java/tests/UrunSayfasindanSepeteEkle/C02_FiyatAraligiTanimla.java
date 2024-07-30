package tests.UrunSayfasindanSepeteEkle;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HepsiburadaPage;
import utilities.DataProviders;
import utilities.Driver;
import utilities.ReusableMethods;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class C02_FiyatAraligiTanimla {

    static String minFiyat;
    static String maxFiyat;

    @Test(dataProvider = "itemIndex", dataProviderClass = DataProviders.class, priority = 2)
    public void FiyatFiltresi(String item, int rowNum) throws IOException {

        HepsiburadaPage hb = new HepsiburadaPage();
        Actions actions = new Actions(Driver.getDriver());
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        SoftAssert sa = new SoftAssert();

        //Fiyat listesini açmak için Fiyat Aralığına Tıkla - sayfayı yeniliyor bulamıyor, engelleyici eklendi
        minFiyat = "200";
        maxFiyat = "500";
        boolean secenekler = true;

        //Fiyat seçenekleri bazen açık bazen kapalı geliyor. Try-catch
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.id("fiyat")));
            try {
                actions.moveToElement(hb.fiyatAraligi)
                        .click()
                        .sendKeys(Keys.TAB +
                                minFiyat +
                                Keys.TAB +
                                maxFiyat +
                                Keys.TAB +
                                Keys.ENTER)
                        .perform();
            } catch (Exception e){
                actions.moveToElement(hb.fiyatAraligi)
                        .click()
                        .click()
                        .sendKeys(Keys.TAB +
                                minFiyat +
                                Keys.TAB +
                                maxFiyat +
                                Keys.TAB +
                                Keys.ENTER)
                        .perform();
            } finally {
                // filtrenin işleyebilmesi için yenilemek gerekiyor test sayfasında
                Driver.getDriver().navigate().refresh();
            }

            //seçilen tüm filtreler liste halinde alınır
            List<WebElement> filtreler = Driver.getDriver().findElements(By.className("appliedVerticalFilter-rxdhhFDFaJiRVL0RqUW_"));
            List<String> filtrelerIsim = ReusableMethods.toStringList(filtreler);

            String kategori = null;
            if (maxFiyat != null && minFiyat != null) {
                kategori = minFiyat + " - " + maxFiyat + " TL";
            } else if (minFiyat == null && maxFiyat != null) {
                kategori = maxFiyat + " TL altında";
            } else if (minFiyat != null && maxFiyat == null) {
                kategori = minFiyat + " TL üzerinde";
            } else {
                System.out.println("Fiyat girilmemiş");
            }
            sa.assertTrue(filtrelerIsim.contains(kategori), "Fiyat Aralığı filtrelenememiştir.");
        } catch (Exception e) {
            System.out.println("Filtreleme yapılamadı");
        }


    }
}
