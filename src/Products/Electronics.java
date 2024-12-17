package Products;

public class Electronics extends Product {
    public Electronics(String name, double price, String imagePath) {
        super(name, price, imagePath);
    }

    @Override
    public String getCategory() {
        return "Electronics";
    }
}
