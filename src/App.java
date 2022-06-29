import java.sql.*;
import java.io.*;

public class App {
    private static int batchSize = 2000;
    private static int currentSale;

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
    	currentSale = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("./files/sales.csv"))) {
            String line;
            br.readLine();
            Connection con = DbConnectionManager.getInstance().getConnection();
            String sqlString = "INSERT INTO sales (shopid, productid, dateid, sold, revenue) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement batchInsert = con.prepareStatement(sqlString);
            
            while ((line = br.readLine()) != null) {
            	currentSale = currentSale + 1;
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
                int productid = prod.save();
                int dateid = readDates(singleLine[0]);
                int shopid = readStores(singleLine[1]);
                
                batchInsert.setInt(1, shopid);
                batchInsert.setInt(2, productid);
                batchInsert.setInt(3, dateid);
                batchInsert.setDouble(4, Integer.parseInt(singleLine[3]));
                batchInsert.setDouble(5, Double.parseDouble(singleLine[4].replace(",", ".")));
                batchInsert.addBatch();

                if ((currentSale % batchSize) == 0) {
                	batchInsert.executeBatch();
                	System.out.println(currentSale + " Verkaeufe wurden gespeichert");
                	batchInsert.clearBatch();
                }

            }
            
            // if last batch has under {batchSize} entrys
            batchInsert.executeBatch();
            System.out.println(currentSale + " Verkaeufe wurden gespeichert"); 
        	batchInsert.clearBatch();

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
        String[] nameList = storeName.split(" ");
        storeName = nameList[nameList.length-1];
        Shop shop = Shop.load(storeName);
        int shopid = shop.save(shopname);
        return shopid;
    }

    public static int readDates(String date) {
        String[] dateArray = date.split("\\.");
        return Date.saveDate(dateArray[0], dateArray[1], dateArray[2], date);
    }
}
