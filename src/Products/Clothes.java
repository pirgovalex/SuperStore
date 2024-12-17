package Products;

// Clothes Classc??
public class Clothes extends Product {
    public Clothes(String name, double price, String imagePath) {
        super(name, price, imagePath);
    }

    @Override
    public String getCategory() {
        return "Clothes";
    }
}
