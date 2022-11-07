package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class TestAddProductServlet extends BaseTestServlets {
    private final AddProductServlet servlet = new AddProductServlet();

    @Test
    public void testSuccess() throws IOException, SQLException {
        List<Product> items = Arrays.asList(
                new Product("item1", 1),
                new Product("item2", 22),
                new Product("item3", 333)
        );

        for (Product item : items) {
            when(request.getParameter("name")).thenReturn(item.name);
            when(request.getParameter("price")).thenReturn(String.valueOf(item.price));

            servlet.doGet(request, response);
            assertTrue(writer.toString().contains("OK"));
        }
        Set<Product> actual = getProducts();
        Set<Product> expected = new HashSet<>(items);

        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));
    }

    @Test
    public void testMissingArgument() {
        // parsing long from Null
        assertThrows(NumberFormatException.class, () -> servlet.doGet(request, response));
    }

}
