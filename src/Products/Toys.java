package Products;


public class Toys extends Product {
    public Toys(String name, double price, String imagePath) {
        super(name, price, imagePath);
    }

    @Override
    public String getCategory() {
        return "Toys";
    }
}
