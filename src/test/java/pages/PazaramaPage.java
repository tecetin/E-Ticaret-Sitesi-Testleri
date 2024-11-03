package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

public class PazaramaPage {

    public PazaramaPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(xpath = "//*[@data-testid='base-cookie-control-accept-cookies']")
    public WebElement acceptCookies;

    @FindBy (xpath = "//*[@data-testid='search-input']//input") ////*[@placeholder='Marka, ürün veya hizmet ara...']
    public WebElement searchBar;

    @FindBy (xpath = "//h1")
    public WebElement kategoriYazisi;

    @FindBy (xpath = "//*[@id='app']/div[1]/div/div[2]/div/div[1]/div[1]/p")
    public WebElement kategoriSonucYazisi;

    @FindBy (xpath = "//*[@id='app']/div[2]/div/div/div//button[@data-testid='base-addbasket-button']")
    public WebElement detayPenceresiSepeteEkle;

    @FindBy (xpath = "//*[@data-testid='base-button-text'][@class='ml-4']")
    public WebElement penceredenSepeteEklendi;

    @FindBy (xpath = "//*[@id='app']/header/div[1]/div[2]/div/div/div/div[3]/div")
    public WebElement sepetim;

    @FindBy (xpath = "//div[3]/div/div[1]/p/span")
    public WebElement sepettekiUrunSayisi;

    @FindBy (xpath = "//*[@id='__layout']/div/div[1]/div[3]/div/div[1]/button/span[1]")
    public WebElement tumunuSil;

    @FindBy (xpath = "//*[@data-testid='base-modal-approve-button']")
    public WebElement silOnay;

    @FindBy (xpath = "//div[3]/div/div[2]/div/p[1]")
    public WebElement urunBulunmamaktadir;



}
