package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.CURD;
import ru.akirakozov.sd.refactoring.db.Product;
import ru.akirakozov.sd.refactoring.html.HTMLFormatter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Set<Product> products = CURD.queryProduct("SELECT * FROM PRODUCT");

            HTMLFormatter writer = new HTMLFormatter(response.getWriter());
            products.forEach(product -> writer.print(product.name + "\t" + product.price).br());
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
