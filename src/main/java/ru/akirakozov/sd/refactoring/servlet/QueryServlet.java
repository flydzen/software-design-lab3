package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.CURD;
import ru.akirakozov.sd.refactoring.db.Product;
import ru.akirakozov.sd.refactoring.html.HTMLFormatter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    Map<String, String> titleMapping = new HashMap<String, String>() {{
        put("max", "Product with max price: ");
        put("min", "Product with min price: ");
        put("sum", "Summary price: ");
        put("count", "Number of products: ");
    }};

    Map<String, String> queryMapping = new HashMap<String, String>() {{
        put("max", "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
        put("min", "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
        put("sum", "SELECT SUM(price) FROM PRODUCT");
        put("count", "SELECT COUNT(*) FROM PRODUCT");
    }};

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        try {
            if (Arrays.asList("max", "min").contains(command)) {
                Set<Product> products = CURD.queryProduct(queryMapping.get(command));

                HTMLFormatter writer = new HTMLFormatter(response.getWriter());
                writer.h1(titleMapping.get(command));
                products.forEach(product -> writer.print(product.name + "\t" + product.price).br());
                writer.close();

            } else if (Arrays.asList("sum", "count").contains(command)) {
                HTMLFormatter writer = new HTMLFormatter(response.getWriter());
                writer.println(titleMapping.get(command));

                CURD.query(queryMapping.get(command), rs -> {
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
