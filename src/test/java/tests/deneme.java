package tests;

import java.io.IOException;

import static utilities.DataProviders.itemsDP;

public class deneme {

    public static void main(String[] args) throws IOException {
        Object[][] data = itemsDP();
        for (Object[] row : data) {
            System.out.println("Item: " + row[0] + ", RowNum: " + row[1]);
        }
    }

}
