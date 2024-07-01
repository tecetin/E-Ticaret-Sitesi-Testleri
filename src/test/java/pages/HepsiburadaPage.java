package pages;

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

    @FindBy (css = ".searchBoxOld-M1esqHPyWSuRUjMCALPK")
    public WebElement searchBox;

    @FindBy (css = ".theme-IYtZzqYPto8PhOx3ku3c.theme-JOTHTAYrQhCBEf9bVgI8")
    public WebElement searchBoxText;

    @FindBy (className = "'searchBoxOld-yDJzsIfi_S5gVgoapx6f")
    public WebElement searchButton;

    @FindBy (xpath = "//b[@class='searchResultSummaryBar-AVnHBWRNB0_veFy34hco']")
    public WebElement foundItemAmount;





}
