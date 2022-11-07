package ru.akirakozov.sd.refactoring.servlet;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.Mockito;
import ru.akirakozov.sd.refactoring.db.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;

public class BaseTestServlets {
    private static Connection connection;
    private static final String dbPath = "jdbc:sqlite:test.db";
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected StringWriter writer;

    @BeforeClass
    public static void setUp() throws SQLException {
        connection = DriverManager.getConnection(dbPath);
        String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)";
        runQuery(sql);
    }

    @Before
    public void before() throws SQLException, IOException {
        runQuery("DELETE FROM PRODUCT");
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);

        writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);
    }

    public static void runQuery(String query) throws SQLException {
        Statement stmt = connection.createStatement();

        stmt.executeUpdate(query);
        stmt.close();
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        connection.close();
    }

    protected Set<Product> getProducts() throws SQLException {
        try (Connection c = DriverManager.getConnection(dbPath)) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");
            Set<Product> products = new HashSet<>();
            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                products.add(new Product(name, price));
            }
            rs.close();
            stmt.close();
            return products;
        }
    }

    protected void addProduct(String name, long price) throws SQLException {
        try (Connection c = DriverManager.getConnection(dbPath)) {
            String sql = "INSERT INTO PRODUCT " +
                    "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

}
