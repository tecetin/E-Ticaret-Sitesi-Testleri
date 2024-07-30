package pages;

import org.checkerframework.framework.qual.FromStubFile;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

public class HepsiburadaPage {

    public HepsiburadaPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy (xpath = "//*[text()='Kabul et']")
    public WebElement acceptCookies;

    @FindBy (xpath = "//div[2]/div/div[2]/div/div/div/div/div[1]/div[2]")
    public WebElement searchBox;

    @FindBy (xpath = "/html/body/div[1]/div/div/div[4]/div[5]/div/div/div/div[1]/div[2]/div[2]/div/div[2]/div/div/div/div/div[1]/div[2]/input")
    public WebElement searchBoxInput;

    @FindBy (xpath = "//b[@class='searchResultSummaryBar-AVnHBWRNB0_veFy34hco']")
    public WebElement foundItemAmount;

    @FindBy (className = "horizontalSortingBar-Ce404X9mUYVCRa5bjV4D")
    public WebElement siralama;

    @FindBy (xpath = "//*[text()='Yüksek puanlılar']")
    public WebElement yuksekPuanlilar;

    //@FindBy (className = "LocationBox-dw9u20rbc1hxlbtwkdhU")
    @FindBy (xpath = "//div[3]/div[1]/div/div[2]/div/div/div/div/div/div")
    public WebElement konum;

    @FindBy (xpath = "(//*[@data-test-id='select-dropdown'])[1]")
    public WebElement ilSec;

    @FindBy (xpath = "(//*[@data-test-id='select-dropdown'])[2]")
    public WebElement ilceSec;

    @FindBy (xpath = "(//*[@data-test-id='select-dropdown'])[3]")
    public WebElement mahalleSec;

    @FindBy (css = "input[type='text'][placeholder='Filtrele']")
    public WebElement filtre;

    @FindBy (css = "[data-test-id='option-item']")
    public WebElement sec;

    @FindBy (xpath = "//button[text()='Kaydet']")
    public WebElement kaydet;

    @FindBy (xpath = "//*[@data-testid='custom-element']")
    public WebElement yarinKapindaClick;

    @FindBy (className = "hb-toast-text")
    public WebElement eklendiMesaji;

    @FindBy (className = "hb-toast-close-icon-holder")
    public WebElement mesajiKapat;

    //@FindBy (className = "hb-toast-link")
    @FindBy (xpath = "//*[@href='https://checkout.hepsiburada.com/sepetim']")
    public WebElement sepeteGit;

    @FindBy (id = "basket_payedPrice")
    public WebElement sepetToplami;

    @FindBy (xpath = "//*[text()='Değerlendirme Puanı']")
    public WebElement degerlendirmePuani;

    @FindBy (xpath = "//*[@id='puan']/div")
    public WebElement puanListesi;

    @FindBy (xpath = "//div[@data-test-id='collapse-title' and text()='Fiyat Aralığı']")
    public WebElement fiyatAraligi;

    @FindBy (xpath = "//div[4]/div/div/div/div/div[1]/div/div[1]/input")
   public WebElement enAzFiyat;

    @FindBy (className = "addToCartButton")
    public WebElement sepeteEkle;

    @FindBy (xpath = "//*[text()='Sepete git']")
    public WebElement sepeteGit2;

    @FindBy (xpath = "//header/h1")
    public WebElement urunAdi;

    @FindBy (className = "tippy-content")
    public WebElement popUp;




}
