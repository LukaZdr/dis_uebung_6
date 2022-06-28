import java.sql.*;
import java.util.*;
import java.io.*;

public class Date {

    public static int saveDate(String day, String month, String year, String date) {
        Connection con = DbConnectionManager.getInstance().getConnection();
        int id = 0;
        try {
            String insertSQL = "INSERT INTO time(day, month, year, quarter, date) VALUES (?,?,?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, day);
            pstmt.setString(2, month);
            pstmt.setString(3, year);
            pstmt.setString(4, getQuarter(month));
            pstmt.setString(5, date);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
            rs.close();
            pstmt.close();
            return id;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getQuarter(String month) {
        int monthString = Integer.parseInt(month);
        if (monthString < 4) {
            return "1";
        } else if (monthString < 7) {
            return "2";
        } else if (monthString < 10) {
            return "3";
        } else {
            return "4";
        }
    }
}
