package Products;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductFactory {

    private static final List<Electronics> electronicsList = new ArrayList<>();
    private static final List<Books> booksList = new ArrayList<>();
    private static final List<Toys> toysList = new ArrayList<>();
    private static final List<Sports> sportsList = new ArrayList<>();
    private static final List<HomeAppliances> homeAppliancesList = new ArrayList<>();
    private static final List<Clothes> clothesList = new ArrayList<>();

    // Parse Products from Map
    public static void parseProducts(Map<String, List<String>> subclassMap) {
        subclassMap.forEach((subclass, products) -> {
            switch (subclass.toLowerCase()) {
                case "electronics" -> parseProducts(products, electronicsList, Electronics::new); //;)
                case "books" -> parseProducts(products, booksList, Books::new);
                case "toys" -> parseProducts(products, toysList, Toys::new);
                case "sports" -> parseProducts(products, sportsList, Sports::new);
                case "home_appliances" -> parseProducts(products, homeAppliancesList, HomeAppliances::new);
                case "clothes" -> parseProducts(products, clothesList, Clothes::new);
            }
        });
    }

    // Generic parsing Method whjich we studied
    private static <T extends Product> void parseProducts(List<String> productData, List<T> list, ProductCreator<T> creator) {
        for (String line : productData) {
            String[] data = line.split(",");
            list.add(creator.create(data[0].trim(), Double.parseDouble(data[1].trim()), data[2].trim()));
        }
    }

    // Accessor Methods
    public static List<Electronics> getElectronicsList() {
        return electronicsList;
    }

    public static List<Books> getBooksList() {
        return booksList;
    }

    public static List<Toys> getToysList() {
        return toysList;
    }

    public static List<Sports> getSportsList() {
        return sportsList;
    }

    public static List<HomeAppliances> getHomeAppliancesList() {
        return homeAppliancesList;
    }

    public static List<Clothes> getClothesList() {
        return clothesList;
    }
}

// Functional Interface for Product Creation
interface ProductCreator<T extends Product> {
    T create(String name, double price, String imagePath);
}
