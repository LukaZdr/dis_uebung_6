import java.sql.*;
import java.text.Normalizer.Form;
import java.io.*;

public class managerTable {
    public static void main(String[] args) throws Exception {
        String timeDimension = "";
        String productDimension = "";
        String locationDimension = "";
        String sortSetting = "";
        System.out.println("Zeitdimension? 1: Datum 2: Monat, 3: Quartal 4: Jahr");
        int zeitDimension = FormUtil.readInt("Wähle deine gewünschte Dimension");
        System.out.println(
                "Produktdimension? 1: Artikel 2: Gruppen, 3: Familie 4: Kategorie");
        int produktDimension = FormUtil.readInt("Wähle deine gewünschte Dimension");
        System.out.println("Standortdimension? 1: Shop 2: Stadt, 3: Region 4: Land");
        int standortDimension = FormUtil.readInt("Wähle deine gewünschte Dimension");
        System.out.println("Sortieren nach? 1: Zeit, 2: Standort, 3: Produkt");
        int sortierEinstellung = FormUtil.readInt("Sortierpräferenz");

        switch (zeitDimension) {
            case 1:
                timeDimension = "date";
                break;
            case 2:
                timeDimension = "month";
                break;
            case 3:
                timeDimension = "quarter";
                break;
            default:
                timeDimension = "year";
                break;
        }
        switch (produktDimension) {
            case 1:
                productDimension = "article";
                break;
            case 2:
                productDimension = "productgroup";
                break;
            case 3:
                productDimension = "productfamily";
                break;
            default:
                productDimension = "productcategory";
                break;
        }
        switch (standortDimension) {
            case 1:
                locationDimension = "shopname";
                break;
            case 2:
                locationDimension = "city";
                break;
            case 3:
                locationDimension = "region";
                break;
            default:
                locationDimension = "country";
                break;
        }
        switch (sortierEinstellung) {
            case 1:
                sortSetting = "time.$time";
                break;
            case 2:
                sortSetting = "shop.$location";
                break;
            default:
                sortSetting = "product.$product";
                break;
        }

        System.out.println(timeDimension + " " + productDimension + " " + locationDimension);
        String stringQuery = "SELECT $time, $product, $location, sum(sold) FROM sales, product, shop, time WHERE sales.shopid = shop.shopid AND sales.productid = product.productid AND sales.dateid = time.dateid GROUP BY GROUPING SETS((shop.$location, time.$time, product.$product), (shop.$location, time.$time), (shop.$location, product.$product), (shop.$location), (time.$time), (product.$product), ()) ORDER BY shop.$location, time.$time, product.$product;";
        stringQuery = stringQuery.replace("$sort", sortSetting);
        stringQuery = stringQuery.replace("$time", timeDimension);
        stringQuery = stringQuery.replace("$product", productDimension);
        stringQuery = stringQuery.replace("$location", locationDimension);

        Connection con = DbConnectionManager.getInstance().getConnection();
        Statement query = con.createStatement();
        ResultSet queryResult = query.executeQuery(stringQuery);

        while (queryResult.next()) {
            System.out.println(timeDimension + ": " + queryResult.getString(1) + " " + locationDimension + ": "
                    + queryResult.getString(3) + " " + productDimension + ": " + queryResult.getString(2)
                    + " Summe der verkauften Produkte: " + queryResult.getString(4));
        }
    }
}
