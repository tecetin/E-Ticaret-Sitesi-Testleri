package tests.UrunSayfasindanSepeteEkle;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HepsiburadaPage;
import utilities.DataProviders;
import utilities.Driver;
import utilities.ReusableMethods;

import java.time.Duration;
import java.util.Set;

import static org.testng.Assert.assertTrue;

public class C03_xUrununSayfasinaGit {

    private String getXpathForUrunBilgiKutusu(int i) {
        return "//*[@id='i" + (i - 1) + "']";
    }

    @Test(dataProvider = "itemIndex", dataProviderClass = DataProviders.class, priority = 3)
    public void UrunSayfasinaGit(String item, int rowNum) {

        int i = 1; //listede kaçıncı ürün ile işlem yapılacağını burada belirleyeceğiz

        HepsiburadaPage hb = new HepsiburadaPage();
        Actions actions = new Actions(Driver.getDriver());
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        SoftAssert sa = new SoftAssert();

        //küçük bir pencere açıldığı için sıralamaya tıklayamıyor.


        actions.scrollByAmount(100, 100).perform();
        wait.until(ExpectedConditions.elementToBeClickable(hb.popUp)).click();
        wait.until(ExpectedConditions.elementToBeClickable(hb.siralama));
        try {
            actions.click(hb.siralama)
                    .perform();
            actions.moveToElement(hb.yuksekPuanlilar)
                    .click()
                    .perform();
        } catch (Exception e) {
            actions.sendKeys(Keys.ESCAPE)
                    .click(hb.siralama)
                    .moveToElement(hb.yuksekPuanlilar)
                    .click()
                    .perform();
        }

        Driver.getDriver().navigate().refresh();

        //Farklı window açılacağı için bu window kaydedilir
        String wh = Driver.getDriver().getWindowHandle();

        WebElement urunKutusu = null;
        try {
            urunKutusu = Driver.getDriver().findElement(By.xpath(getXpathForUrunBilgiKutusu(i)));
            urunKutusu.click();
        } catch (Exception e) {
            ;
            i = 1;
            urunKutusu = Driver.getDriver().findElement(By.xpath(getXpathForUrunBilgiKutusu(i)));
            urunKutusu.click();
        }

        //üzerine tıklanan ürün yeni pencerede açılmaktadır, pencere değiştir
        Set<String> allHandles = Driver.getDriver().getWindowHandles();
        for (String handle : allHandles) {
            if (!handle.equals(wh)) {
                Driver.getDriver().switchTo().window(handle);
                break;
            }
        }

        //Doğru ürün bulunduğunu kontrol et
        String actual = hb.urunAdi.getText();
        Assert.assertTrue(actual.contains(item), "Aranan ürün adı: " + item + " Bulunan ürün adı: " + actual);
    }
}
