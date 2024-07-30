package tests.UrunSayfasindanSepeteEkle;

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HepsiburadaPage;
import utilities.DataProviders;
import utilities.Driver;

import java.time.Duration;

public class C04_SepeteEkleveDogrula {

    @Test(dataProvider = "itemIndex", dataProviderClass = DataProviders.class, priority = 4)
    public void SepeteEkle(String item, int rowNum){

        HepsiburadaPage hb = new HepsiburadaPage();
        Actions actions = new Actions(Driver.getDriver());
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        SoftAssert sa = new SoftAssert();

        actions.click(hb.sepeteEkle).perform();
        hb.sepeteGit2.click();


    }
}
