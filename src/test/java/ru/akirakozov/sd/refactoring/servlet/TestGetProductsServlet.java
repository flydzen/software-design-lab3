package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Test;
import ru.akirakozov.sd.refactoring.db.Product;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

public class TestGetProductsServlet extends BaseTestServlets {
    private final GetProductsServlet servlet = new GetProductsServlet();

    @Test
    public void testSuccess() throws SQLException {
        List<Product> items = Arrays.asList(
                new Product("item1", 1),
                new Product("item2", 22),
                new Product("item3", 333)
        );

        for (Product item : items)
            addProduct(item.name, item.price);

        servlet.doGet(request, response);

        String actual = writer.toString();

        assertTrue(actual.startsWith("<html><body>\r\n"));
        assertTrue(actual.contains("item1\t1</br>\r\n"));
        assertTrue(actual.contains("item2\t22</br>\r\n"));
        assertTrue(actual.contains("item3\t333</br>\r\n"));
        assertTrue(actual.endsWith("</body></html>\r\n"));
    }

    @Test
    public void testEmpty() {
        servlet.doGet(request, response);

        String actual = writer.toString();

        assertTrue(actual.startsWith("<html><body>\r\n"));
        assertTrue(actual.endsWith("</body></html>\r\n"));
    }

}
