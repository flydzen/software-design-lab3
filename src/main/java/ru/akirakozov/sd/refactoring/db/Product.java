package ru.akirakozov.sd.refactoring.db;

public class Product {
    public String name;
    public long price;

    public Product(String name, long price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public int hashCode() {
        return (int) (this.name.hashCode() ^ this.price);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Product)) {
            return false;
        }
        Product other = (Product) obj;
        return name.equals(other.name) && price == other.price;
    }
}