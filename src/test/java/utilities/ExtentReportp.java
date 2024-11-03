package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ExtentReportp {
    protected static ExtentReports extent;
    protected static ExtentTest extentTest;
    protected static ExtentSparkReporter extentSparkReporter;

    @BeforeTest(alwaysRun = true)
    public void setUpTest() {

        extent = new ExtentReports(); // Raporlamayi baslatir

        //rapor oluştuktan sonra raporunuz nereye eklensin
        String tarih = new SimpleDateFormat("_dd_MM_yy-HH_mm_ss").format(new Date());
        String dosyaYolu = System.getProperty("user.dir") + "/target/extentReport/Pazarama/Report" + tarih + "/SparkReport.html";

        //oluşturmak istediğimiz raporu (spark formatında) başlatıyoruz, dosya yolunu belirliyoruz.
        extentSparkReporter = new ExtentSparkReporter(dosyaYolu);
        extent.attachReporter(extentSparkReporter);

        // Bilgileri buraya ekle
        extent.setSystemInfo("Browser", ConfigReader.getProperty("browser"));
        extent.setSystemInfo("Tester", ConfigReader.getProperty("testerName"));
        extentSparkReporter.config().setDocumentTitle("Extent Spark Report");
        extentSparkReporter.config().setReportName("TestNG Reports");
        extentSparkReporter.config().setEncoding("utf-8");
        extentSparkReporter.config().setTheme(Theme.DARK);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownMethod(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE) { // eğer testin sonucu başarısızsa
            String screenshotLocation = ReusableMethods.sayfaSSBase64();
            extentTest.fail(result.getName() + " testi başarısız oldu.");
            extentTest.fail(result.getThrowable().getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromBase64String((screenshotLocation)).build());
        } else if (result.getStatus() == ITestResult.SUCCESS) { // Eğer test başarılı olduysa
            extentTest.pass("Test Case başarılı: " + result.getName());
        }
    }

    @AfterTest
    public void tearDownTest() {
        Driver.quitDriver();

        // Raporu bitirir ve dosyaya kaydeder
        if (extent != null) {
            extent.flush();
        } else {
            System.out.println("Extent rapor bulunamadı.");
        }

    }
}

