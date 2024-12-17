package Products;

// Books Class
public class Books extends Product {
    public Books(String name, double price, String imagePath) {
        super(name, price, imagePath);
    }

    @Override
    public String getCategory() {
        return "Books";
    }

}
