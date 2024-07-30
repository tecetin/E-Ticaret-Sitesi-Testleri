package tests.UrunSayfasindanSepeteEkle;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import pages.HepsiburadaPage;
import utilities.ConfigReader;
import utilities.DataProviders;
import utilities.Driver;

import java.io.IOException;
import java.time.Duration;

public class C01_HbUrunArama {

    @Test(dataProvider = "itemIndex", dataProviderClass = DataProviders.class, priority = 1)
    public void urunAramaTesti(String item, int rowNum) throws IOException {

        Driver.getDriver().get(ConfigReader.getProperty("hbUrl"));

        HepsiburadaPage hb = new HepsiburadaPage();
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        Actions actions = new Actions(Driver.getDriver());

        //cookie kabul et fakat sayfayı yeniliyor
        hb.acceptCookies.click();

        /*
        //cookie kabul edildikten sonra sayfa kendini yeniliyor
        //Webelementleri bulamadığı için de NoSuchElement ya da StaleElement hatası veriyor
        //bunu engellemek için WE göründüğünde ESC basılıyor yenileme durduruluyor, WE bulunuyor ve WElemente öyle tıklanıyor
        //waitler önce element görünene kadar beklediği için NoSuchElement hatasını engelliyor, sonra da webelementi yenileyip StaleElement hatasını engelliyor
        */
        //arama kutusuna tıklayıp açılan input elementine değer atanıyor
        //işlem yapılana kadar sayfa yenilenecek
        boolean success = false;
        while (!success) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[2]/div/div[2]/div/div/div/div/div[1]/div[2]"))); //hb.searchBox
                actions.moveToElement(hb.searchBox).sendKeys(Keys.ESCAPE).perform();
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[2]/div/div[2]/div/div/div/div/div[1]/div[2]"))); //hb.searchBox
                actions.click(hb.searchBox).perform();
                wait.until(ExpectedConditions.elementToBeClickable(hb.searchBoxInput))
                        .sendKeys(item + Keys.ENTER);

                success = true;
            } catch (Exception e) {
                Driver.getDriver().navigate().refresh();
            }
        }
    }
}






