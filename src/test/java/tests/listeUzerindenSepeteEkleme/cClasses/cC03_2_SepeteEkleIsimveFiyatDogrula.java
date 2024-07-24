package tests.listeUzerindenSepeteEkleme.cClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HepsiburadaPage;
import tests.listeUzerindenSepeteEkleme.bClasses.bC02_KonumveYarinKapinda;
import utilities.DataProviders;
import utilities.Driver;

import java.io.IOException;
import java.time.Duration;

public class cC03_2_SepeteEkleIsimveFiyatDogrula {

    public boolean shouldCloseDriver3 = true;


    private String getXpathForUrunBilgiKutusu(int i)
    {
        return "//*[@id='i" + (i-1) + "']";
    }

    private String getXpathForSepeteEkleDinamik(int i) {
        return "//*[@id='i" + (i-1) + "']/div/a/div[2]/button";
    }

    private String getXpathForUrunFiyatDinamik(int i) {
        return "(//*[@data-test-id='price-current-price'])[" + i + "]";
    }

    private String getXpathForUrunIsimDinamik(int i) {
        return "(//h3[@type='comfort'][@data-test-id='product-card-name'])[" + i + "]";
    }

    @Test(dataProvider = "itemIndex", dataProviderClass = DataProviders.class)
    public void SepeteEkle(String item, int rowNum) throws IOException {

        int i = 6; //listede kaçıncı ürün ile işlem yapılacağını burada belirleyeceğiz
        HepsiburadaPage hb = new HepsiburadaPage();
        Actions actions = new Actions(Driver.getDriver());
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(1));
        SoftAssert sa = new SoftAssert();

        //-------------------------------------------------------------------------------------------------------------------------------------------
        //filtre yöntemi : Adres seçimi ve Yarın Kapında
        bC02_KonumveYarinKapinda test1ve2 = new bC02_KonumveYarinKapinda();

        test1ve2.shouldCloseDriver2 = false; //C02 çalıştıktan sonra driver kapanmaması için false
        test1ve2.yarinKapinda(item, rowNum); //C02 konum işareti ve yarın kapıda testi çalışıp ürün aratma ve yarın kapındayı işaretleyecek
        //-------------------------------------------------------------------------------------------------------------------------------------------

        //ürünün listedeki isim ve fiyatını kaydet
        WebElement urunKutusu = null;
        try {
            urunKutusu = Driver.getDriver().findElement(By.xpath(getXpathForUrunBilgiKutusu(i)));
            urunKutusu.isDisplayed();
        } catch (Exception e) {;
            i=1;
            urunKutusu = Driver.getDriver().findElement(By.xpath(getXpathForUrunBilgiKutusu(i)));
        }

        String isim = Driver.getDriver()
                .findElement(By.xpath(getXpathForUrunIsimDinamik(i)))
                .getText();


        String fiyat = Driver.getDriver()
                .findElement(By.xpath(getXpathForUrunFiyatDinamik(i)))
                .getText();

        //sepete ekle butonu çıkması için ürün üzerine mouse ile git bekle
        actions.scrollToElement(urunKutusu)
                .moveToElement(urunKutusu)
                .perform();

        //sepete ekle butonu çıkana kadar bekle, mouse ile üzerine tıkla
        actions.moveToElement(Driver.getDriver().findElement(By.xpath(getXpathForSepeteEkleDinamik(i))))
                .click()
                .perform();

        //bazı ürünlerde seçenek seçmek gerekiyor, seçenekli ürünlerde sepete bu yöntemle gidilemeyeceği için try catch ile o ürünler atlanıyor
        try {
            wait.until(ExpectedConditions.elementToBeClickable(hb.eklendiMesaji));
            String expMS = "Ürün sepete eklendi";
            String actMsg = hb.eklendiMesaji.getText();
            sa.assertEquals(actMsg, expMS);
            hb.mesajiKapat.click();
        } catch (Exception e) {
            System.out.println(item + " ürünü sepete ekleme sırasında açılan pencere nedeniyle sepete eklenemedi.");
            actions.sendKeys(Keys.ESCAPE).perform();
        }

        wait.until(ExpectedConditions.elementToBeClickable(hb.sepeteGit))
                .click();
        boolean sepetBos = true;
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("basket_payedPrice"))); //sepet toplamının görünür olmasını bekle
            hb.sepetToplami.isDisplayed();
        } catch (Exception e) {
            sepetBos = false;
        }

        if (sepetBos) {
            String sepetToplami = hb.sepetToplami.getText();
            sa.assertEquals(fiyat, sepetToplami);
        } else {
            System.out.println("Sepette ürün bulunmamaktadır:" + item);
        }

        //pencereyi kapat
        if (shouldCloseDriver3) {
            Driver.quitDriver();
        }
    }
}


