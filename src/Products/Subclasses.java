package Products;

import java.util.List;

public class Subclasses {
    //Namiong class
    public List<Electronics> getElectronicsList() {
        return ProductFactory.getElectronicsList();
    }

    public List<Books> getBooksList() {
        return ProductFactory.getBooksList();
    }

    public List<Toys> getToysList() {
        return ProductFactory.getToysList();
    }

    public List<Sports> getSportsList() {
        return ProductFactory.getSportsList();
    }

    public List<HomeAppliances> getHomeAppliancesList() {
        return ProductFactory.getHomeAppliancesList();
    }

    public List<Clothes> getClothesList() {
        return ProductFactory.getClothesList();
    }
}


