package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    static Properties properties;

    static {

        String filePath = "configuration.properties";

        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            properties = new Properties();
            properties.load(fileInputStream);
        } catch (IOException e) {
            System.out.println("Properties dosyasi okunamadi.");
        }
    }

    public static String getPropert(String key) {
        return properties.getProperty(key);
    }
}
