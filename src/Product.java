import java.sql.*;
import java.io.*;
import java.util.*;

public class Product {
    private String article;
    private String productgroup;
    private String productfamily;
    private String productcategory;
    private int price;

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getProductgroup() {
        return productgroup;
    }

    public void setProductgroup(String productgroup) {
        this.productgroup = productgroup;
    }

    public String getProductfamily() {
        return productfamily;
    }

    public void setProductfamily(String productfamily) {
        this.productfamily = productfamily;
    }

    public String getProductCategory() {
        return productcategory;
    }

    public void setProductcategory(String productcategory) {
        this.productcategory = productcategory;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public static Product load(String article) {

        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM article WHERE name = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setString(1, article);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Product pr = new Product();
                pr.setArticle(rs.getString("name"));
                List<String> productinfo = getProductInformation(rs.getInt("productgroupid"));
                pr.setProductgroup(productinfo.get(0));
                pr.setProductfamily(productinfo.get(1));
                pr.setProductcategory(productinfo.get(2));
                pr.setPrice(rs.getInt("price"));
                rs.close();
                pstmt.close();
                return pr;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getProductInformation(int productgroupid) {
        List<String> productinfo = new ArrayList<String>();
        int family = 0;
        int category = 0;
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM productgroup WHERE productgroupid = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, productgroupid);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                productinfo.add(rs.getString("name"));
                family = rs.getInt("productfamilyid");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM productfamily WHERE productfamilyid = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, family);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                productinfo.add(rs.getString("name"));
                category = rs.getInt("productcategoryid");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM productcategory WHERE productcategoryid = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, category);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                productinfo.add(rs.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return productinfo;
    }

    public int save() {
        Connection con = DbConnectionManager.getInstance().getConnection();
        int id = 0;
        try {
            String insertSQL = "INSERT INTO product(article, productgroup, productfamily, productcategory, price) VALUES (?,?,?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, getArticle());
            pstmt.setString(2, getProductgroup());
            pstmt.setString(3, getProductfamily());
            pstmt.setString(4, getProductCategory());
            pstmt.setInt(5, getPrice());
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
}
