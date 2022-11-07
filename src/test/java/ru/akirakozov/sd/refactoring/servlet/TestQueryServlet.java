package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Test;
import ru.akirakozov.sd.refactoring.db.Product;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class TestQueryServlet extends BaseTestServlets {
    private final QueryServlet servlet = new QueryServlet();

    public void baseTest(String command, String header, String product) throws SQLException, IOException {
        List<Product> items = Arrays.asList(
                new Product("item1", 1),
                new Product("item2", 22),
                new Product("item3", 333)
        );

        for (Product item : items)
            addProduct(item.name, item.price);

        when(request.getParameter("command")).thenReturn(command);

        servlet.doGet(request, response);

        String actual = writer.toString();

        System.out.println(actual);

        assertTrue(actual.startsWith("<html><body>\r\n"));
        assertTrue(actual.contains(header));
        assertTrue(actual.contains(product));
        assertTrue(actual.endsWith("</body></html>\r\n"));
    }

    @Test
    public void testMax() throws IOException, SQLException {
        baseTest("max", "<h1>Product with max price: </h1>\r\n", "item3\t333</br>\r\n");
    }

    @Test
    public void testMin() throws IOException, SQLException {
        baseTest("min", "<h1>Product with min price: </h1>\r\n", "item1\t1</br>\r\n");
    }

    @Test
    public void testSum() throws IOException, SQLException {
        long sum = 1 + 22 + 333;
        baseTest("sum", "Summary price: \r\n", sum + "\r\n");
    }

    @Test
    public void testCount() throws IOException, SQLException {
        baseTest("count", "Number of products: \r\n", 3 + "\r\n");
    }

    @Test
    public void testUnknown() throws SQLException, IOException {
        when(request.getParameter("command")).thenReturn("unknown");

        servlet.doGet(request, response);

        String actual = writer.toString();

        assertTrue(actual.contains("Unknown command: unknown"));
    }
}
