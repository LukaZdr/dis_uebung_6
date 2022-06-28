import java.sql.*;
import java.io.*;
import java.util.*;

public class Sales {

    public static void saveToSales(int product, double sold, double revenue, int shopid, int dateid) {
        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            String insertSQL = "INSERT INTO sales(productid, sold, revenue, shopid, dateid) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(insertSQL);
            pstmt.setInt(1, product);
            pstmt.setDouble(2, sold);
            pstmt.setDouble(3, revenue);
            pstmt.setInt(4, shopid);
            pstmt.setInt(5, dateid);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
