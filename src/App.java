import java.sql.*;
import java.io.*;

public class App {
    private static int batchSize;
    private static int maxBatchSize = 200000;

    public static void main(String[] args) throws Exception {
        // Read and Execute SQL File
        Connection con = DbConnectionManager.getInstance().getConnection();
        String sqlStatement = "";
        String sqlCSVStatement = "";
        try (BufferedReader br = new BufferedReader(new FileReader("./files/stores-and-products.sql"))) {
            String line;

            while ((line = br.readLine()) != null) {
                sqlStatement += line;
            }
        }
        try {
            PreparedStatement pstmt = con.prepareStatement(sqlStatement);
            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (BufferedReader br2 = new BufferedReader(new FileReader("./files/csv-table-structure.sql"))) {
            String line;

            while ((line = br2.readLine()) != null) {
                sqlCSVStatement += line;
            }
        }
        try {
            PreparedStatement pstmt2 = con.prepareStatement(sqlCSVStatement);
            pstmt2.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        readProducts();
    }

    public static void readAndWriteCSV() {

    }

    public static void readProducts() {
        batchSize = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("./files/sales.csv"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null && batchSize < maxBatchSize) {
                batchSize = batchSize + 1;
                String[] singleLine = line.split(";");
                String product = singleLine[2];
                if (product.contains("�")) {
                    if (product.charAt(0) == 'G') {
                        product = singleLine[2].replace("�", "ü");
                    } else {
                        product = singleLine[2].replace("�", "Ö");
                    }

                }
                Product prod = Product.load(product);
                int prodID = prod.save();
                int dateid = readDates(singleLine[0]);
                int shopid = readStores(singleLine[1]);

                Sales.saveToSales(prodID, Double.parseDouble(singleLine[3].replace(",", ".")),
                        Double.parseDouble(singleLine[4].replace(",", ".")), shopid, dateid);
                System.out.println("Produktkauf für " + prod.getArticle() + " wurde gespeichert!");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        batchSize = batchSize + 1;
    }

    public static int readStores(String storeName) {
        if (storeName.contains("�")) {
            storeName = storeName.replace("�", "ü");
        }
        String shopname = storeName;
        storeName = storeName.split(" ")[1];
        Shop shop = Shop.load(storeName);
        int shopid = shop.save(shopname);
        return shopid;
    }

    public static int readDates(String date) {
        String[] dateArray = date.split("\\.");
        return Date.saveDate(dateArray[0], dateArray[1], dateArray[2], date);
    }
}
