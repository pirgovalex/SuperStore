import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import Products.*;
import IOHandler.*;

public class OnlineShopApp {

    private JFrame frame;
    private JTree categoryTree;
    private JPanel productPanel;
    private List<Product> cart;
    private DefaultMutableTreeNode cartNode;
    private final Subclasses subclasses;

    public OnlineShopApp(Subclasses subclasses) {
        this.subclasses = subclasses;
        cart = new java.util.ArrayList<>();
        initialize();
    }
//tuk shte se oplacha che internships are scarce , NQMA rabota za programisti :::)))))) tochno 1 imashe v plovdiv po java i bam! ne me vzeha :(
    public static void main(String[] args) {
        PublicFileManager publicFileManager = new PublicFileManager();
        try {
            publicFileManager.dataStarter();
            ProductFactory productFactory = new ProductFactory();
            productFactory.parseProducts(publicFileManager.getSubclassMap());

            Subclasses subclasses = new Subclasses();

            SwingUtilities.invokeLater(() -> new OnlineShopApp(subclasses));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        frame = new JFrame("Online Shop");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 600);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Categories");
        addCategoryToTree(root, "Electronics", subclasses.getElectronicsList());
        addCategoryToTree(root, "Books", subclasses.getBooksList());
        addCategoryToTree(root, "Toys", subclasses.getToysList());
        addCategoryToTree(root, "Sports", subclasses.getSportsList());
        addCategoryToTree(root, "Home Appliances", subclasses.getHomeAppliancesList());
        addCategoryToTree(root, "Clothes", subclasses.getClothesList());

        cartNode = new DefaultMutableTreeNode("Shopping Cart");
        root.add(cartNode);

        categoryTree = new JTree(root);
        categoryTree.addTreeSelectionListener(e -> {
            String selectedNode = e.getPath().getLastPathComponent().toString();
            if (selectedNode.equals("Shopping Cart")) {
                new CartTableView((ArrayList<Product>) cart);
            } else {
                loadProducts(selectedNode);
            }
        });

        frame.add(new JScrollPane(categoryTree), BorderLayout.WEST);

        productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        frame.add(new JScrollPane(productPanel), BorderLayout.CENTER);

        JButton viewCartButton = new JButton("View Cart");
        viewCartButton.addActionListener(e -> new CartTableView((ArrayList<Product>) cart));
        frame.add(viewCartButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void addCategoryToTree(DefaultMutableTreeNode root, String category, List<? extends Product> products) {
        if (products != null && !products.isEmpty()) {
            DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(category);
            products.forEach(product -> categoryNode.add(new DefaultMutableTreeNode(product.getName())));
            root.add(categoryNode);
        }
    }

    private void loadProducts(String subcategory) {
        productPanel.removeAll();
        List<? extends Product> products = getProductsByCategory(subcategory);

        if (products != null && !products.isEmpty()) {
            for (Product product : products) {
                JPanel productRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
                productRow.setPreferredSize(new Dimension(400, 120));

                // Load and scale the image
                JLabel imageLabel = new JLabel(getScaledImage(product.getImagePath(), 100, 100));
                JLabel nameLabel = new JLabel(product.getName());
                JLabel priceLabel = new JLabel("$" + product.getPrice());
                JButton addToCartButton = new JButton("Add to Cart");

                // Add "Add to Cart" button functionality
                addToCartButton.addActionListener(e -> {
                    cart.add(product);
                    updateCartNode();
                    JOptionPane.showMessageDialog(frame, product.getName() + " added to cart.");
                });

                // Add components to the product row
                productRow.add(imageLabel);
                productRow.add(nameLabel);
                productRow.add(priceLabel);
                productRow.add(addToCartButton);

                productPanel.add(productRow);
            }
        } else {
            productPanel.add(new JLabel("No products available for this subcategory."));
        }

        productPanel.revalidate();
        productPanel.repaint();
    }

    private ImageIcon getScaledImage(String imagePath, int width, int height) {
        try {
            ImageIcon originalIcon = new ImageIcon(imagePath);
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            // Placeholder for missing images
            return new ImageIcon(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
        }
    }


    private List<? extends Product> getProductsByCategory(String category) {//wildcard,  almost.
        return switch (category) {
            case "Electronics" -> subclasses.getElectronicsList();
            case "Books" -> subclasses.getBooksList();
            case "Toys" -> subclasses.getToysList();
            case "Sports" -> subclasses.getSportsList();
            case "Home Appliances" -> subclasses.getHomeAppliancesList();
            case "Clothes" -> subclasses.getClothesList();
            default -> null;
        };
    }

    private void updateCartNode() {
        cartNode.removeAllChildren();
        for (Product product : cart) {
            cartNode.add(new DefaultMutableTreeNode(product.getName()));
        }
        ((DefaultTreeModel) categoryTree.getModel()).reload();
    }
}
