package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.CURD;
import ru.akirakozov.sd.refactoring.db.Product;
import ru.akirakozov.sd.refactoring.html.HTMLFormatter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.Set;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        try {
            if ("max".equals(command)) {
                Set<Product> products = CURD.queryProduct("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");

                HTMLFormatter writer = new HTMLFormatter(response.getWriter());
                writer.h1("Product with max price: ");
                products.forEach(product -> writer.print(product.name + "\t" + product.price).br());
                writer.close();

            } else if ("min".equals(command)) {
                Set<Product> products = CURD.queryProduct("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");

                HTMLFormatter writer = new HTMLFormatter(response.getWriter());
                writer.h1("Product with min price: ");
                products.forEach(product -> writer.print(product.name + "\t" + product.price).br());
                writer.close();

            } else if ("sum".equals(command)) {
                HTMLFormatter writer = new HTMLFormatter(response.getWriter());
                writer.println("Summary price: ");

                CURD.query("SELECT SUM(price) FROM PRODUCT", rs -> {
                    try {
                        writer.println(rs.getInt(1));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                writer.close();
            } else if ("count".equals(command)) {
                HTMLFormatter writer = new HTMLFormatter(response.getWriter());
                writer.println("Number of products: ");

                CURD.query("SELECT COUNT(*) FROM PRODUCT", rs -> {
                    try {
                        writer.println(rs.getInt(1));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                writer.close();
            } else {
                response.getWriter().println("Unknown command: " + command);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
