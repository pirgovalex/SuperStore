import IOHandler.PublicFileManager;
import Products.Electronics;
import Products.ProductFactory;
import Products.Subclasses;
import Products.Product;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            // Create an instance of PublicFileManager to load the product data
            PublicFileManager publicFileManager = new PublicFileManager();
            publicFileManager.dataStarter();  // Loading the data from file

            // Parse the products using ProductFactory
            ProductFactory productFactory = new ProductFactory();
            productFactory.parseProducts(publicFileManager.getSubclassMap());  // Pass subclassMap to ProductFactory

            // Access product lists through Subclasses
            Subclasses subclasses = new Subclasses();

            // Example: Print Electronics
            System.out.println("Loaded Electronics:");
            subclasses.getElectronicsList().forEach(e ->
                    System.out.println(e.getName() + " - $" + e.getPrice() + "Image :" + e.getImagePath())
            );

            // Example: Print Books
            System.out.println("\nLoaded Books:");
            subclasses.getBooksList().forEach(b ->
                    System.out.println(b.getName() + " - $" + b.getPrice()+ " Image :" + b.getImagePath())
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
