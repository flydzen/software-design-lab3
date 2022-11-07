package ru.akirakozov.sd.refactoring.db;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class CURD {
    public static final String DB_NAME = "jdbc:sqlite:test.db";

    public static void upsert(String sql) throws SQLException {
        try (Connection c = DriverManager.getConnection(DB_NAME)) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    public static void insert(Product product) throws SQLException {
        String sql = "INSERT INTO PRODUCT " +
                "(NAME, PRICE) VALUES (\"" + product.name + "\"," + product.price + ")";
        upsert(sql);
    }

    public static void query(String sql, Consumer<ResultSet> consumer) throws SQLException {
        try (Connection c = DriverManager.getConnection(DB_NAME)) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
                consumer.accept(rs);

            rs.close();
            stmt.close();
        }
    }

    public static Set<Product> queryProduct(String sql) throws SQLException {
        Set<Product> result = new HashSet<>();
        query(sql, rs -> {
            try {
                result.add(new Product(rs.getString("name"), rs.getInt("price")));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return result;
    }
}
