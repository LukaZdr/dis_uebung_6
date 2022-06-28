import java.sql.*;
import java.io.*;
import java.util.*;

public class Shop {
    private String shopname;
    private String city;
    private String region;
    private String country;

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static Shop load(String city) {

        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM city WHERE name = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setString(1, city);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Shop pr = new Shop();
                pr.setCity(rs.getString("name"));
                List<String> shopinfo = getShopInformation(rs.getInt("regionid"));
                System.out.println(shopinfo);
                pr.setRegion(shopinfo.get(0));
                pr.setCountry(shopinfo.get(1));
                rs.close();
                pstmt.close();
                return pr;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getShopInformation(int regionid) {
        List<String> shopinfo = new ArrayList<String>();
        int region = 0;
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM region WHERE regionid = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, regionid);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                shopinfo.add(rs.getString("name"));
                region = rs.getInt("countryid");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM country WHERE countryid = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, region);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                shopinfo.add(rs.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return shopinfo;
    }

    public int save(String shopname) {
        Connection con = DbConnectionManager.getInstance().getConnection();
        int id = 0;
        try {
            String insertSQL = "INSERT INTO shop(shopname, city, region, country) VALUES (?,?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, shopname);
            pstmt.setString(2, getCity());
            pstmt.setString(3, getRegion());
            pstmt.setString(4, getCountry());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
                System.out.println(id);
            }
            rs.close();
            pstmt.close();
            return id;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
