package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

public class LCWPage {

    public LCWPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }


    @FindBy (id = "cookieseal-banner-accept")
    public WebElement acceptCookies;

    @FindBy (id = "search-form__input-field__search-input")
    public WebElement searchBar;

    @FindBy (className = "product-list-heading__heading")
    public WebElement kategoriYazisi;

    @FindBy (xpath = "//div[2]/div/span/p")
    public WebElement kategoriSonucYazisi;

    @FindBy (xpath = "//*[@id='option-size']/a[1]")
    public WebElement ilkBeden;

    @FindBy (id = "pd_add_to_cart")
    public WebElement sepeteEkle;

    @FindBy (xpath = "//div/div[1]/div//*[@placeholder='Cinsiyet Ara']")
    public WebElement cinsiyetFiltresi;

    @FindBy (xpath = "//*[@id='root']/div/div[2]/div[1]/div[6]/div/div[1]/div//*[text()='Erkek']")
    public WebElement erkekFiltresi;

    @FindBy (className = "collapsible-filter-container__clear-all-button")
    public WebElement filtreTemizle;


}
