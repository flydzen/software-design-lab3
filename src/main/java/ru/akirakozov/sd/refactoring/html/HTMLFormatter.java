package ru.akirakozov.sd.refactoring.html;

import java.io.PrintWriter;

public class HTMLFormatter implements AutoCloseable {
    private final PrintWriter writer;

    public HTMLFormatter(PrintWriter writer) {
        this.writer = writer;

        writer.println("<html><body>");
    }

    public HTMLFormatter println(String text) {
        writer.println(text);
        return this;
    }

    public HTMLFormatter println(int num) {
        writer.println(num);
        return this;
    }

    public HTMLFormatter print(String text) {
        writer.print(text);
        return this;
    }


    public HTMLFormatter h1(String str) {
        writer.print("<h1>");
        writer.print(str);
        writer.println("</h1>");
        return this;
    }

    public HTMLFormatter br() {
        writer.println("</br>");
        return this;
    }

    @Override
    public void close() {
        writer.println("</body></html>");
    }
}
