package utilities;

import org.apache.poi.ss.usermodel.*;
import org.testng.annotations.DataProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataProviders {

    @DataProvider(name = "itemIndex")
    public static Object[][] itemsDP() throws IOException {

        return ExcelListesiOkuArrayDondur.readExcelData(ConfigReader.getProperty("filePath"));
    }
}
