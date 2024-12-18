package Products;

// Sports Class
public class Sports extends Product {
    public Sports(String name, double price, String imagePath) {
        super(name, price, imagePath);
    }

    @Override
    public String getCategory() {
        return "Sports";
    }
}
