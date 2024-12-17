package Products;

// HomeAppliances Class
public class HomeAppliances extends Product {
    public HomeAppliances(String name, double price, String imagePath) {
        super(name, price, imagePath);
    }

    @Override
    public String getCategory() {
        return "Home Appliances";
    }

}
