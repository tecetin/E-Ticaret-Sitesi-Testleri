package utilities;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.HepsiburadaPage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ReusableMethods {
    static JavascriptExecutor js;
    static WebDriverWait wait;

    public static List<String> toStringList(List<WebElement> webElementList){
        List<String> stringListe = new ArrayList<>();
        for (WebElement each : webElementList){
            stringListe.add(each.getText());
        }
        return stringListe;
    }

    public static void wait(
            int sec){
        try {
            Thread.sleep(sec*1000);
        } catch (InterruptedException e) {
            System.out.println("Bekletme isleminde sorun olustu.");
        }
    }

    public static void changeWindow(WebDriver driver, String nextUrl){
        Set<String> allWHD = driver.getWindowHandles();

        for (String each : allWHD){
            driver.switchTo().window(each);
            String actualUrl = driver.getCurrentUrl();
            if (actualUrl.equals(nextUrl)){
                break;
            }
        }
    }

    public static void pageScreenShot(WebDriver driver, String ssName){
        TakesScreenshot screenshot = (TakesScreenshot) driver;

        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateLbl = ldt.format(format1);

        ssName = ssName.replaceAll("\\s", "");

        File pageSS = new File("test-reports/Screenshots/"
        +ssName + dateLbl + ".png");

        File tempFile = screenshot.getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(tempFile, pageSS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static void webelementSS (WebElement ssWebelement, String ssName){

        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateLbl = ldt.format(format1);

        File webElementScreenshot = new File("target/Screenshots/"
                + ssName + dateLbl + ".png");

        File tempFile = ssWebelement.getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(tempFile,webElementScreenshot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getScreenshot(String name) throws IOException {
        // naming the screenshot with the current date to avoid duplication
        String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        // TakesScreenshot is an interface of selenium that takes the screenshot
        TakesScreenshot ts = (TakesScreenshot) Driver.getDriver();
        File source = ts.getScreenshotAs(OutputType.FILE);
        // full path to the screenshot location
        String target = System.getProperty("user.dir") + "/test-reports/Screenshots/" + name + date + ".png";
        File finalDestination = new File(target);
        // save the screenshot to the path given
        FileUtils.copyFile(source, finalDestination);
        return target;
    }

    public static int hbSonucAdedi(WebElement element){
        int sonuc = Integer.parseInt(element.getText().replaceAll("\\D", ""));
        return sonuc;
    }

    public static int rowCount(String filePath) throws IOException {
        //path verilen dosyadaki başlık hariç satır sayısını hesaplar,
        //aslında son satır sayısını verir fakat sayma 0'dan başladığı için başlık 0 olarak sayılır

        String filePathh = filePath;
        FileInputStream fileInputStream = new FileInputStream(filePathh);
        Workbook workbook = WorkbookFactory.create(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);
        return sheet.getLastRowNum();
    }



}
