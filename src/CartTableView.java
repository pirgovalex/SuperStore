import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import Products.Product;

class NonEditableTableModel extends DefaultTableModel {
    public NonEditableTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;  // immutable
    }
}

public class CartTableView extends JFrame {

    private ArrayList<Product> cart;
    private JTable table;
    private NonEditableTableModel tableModel;
    private JButton payButton, sortByNameButton, sortByPriceButton;

    private JButton plusButton, minusButton;

    public CartTableView(ArrayList<Product> cart) {
        this.cart = cart;

        setTitle("Cart View");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialization
        tableModel = new NonEditableTableModel(new Object[][]{}, new String[]{"Name", "Price"});
        table = new JTable(tableModel);
        TableRowSorter<NonEditableTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add
        refreshTable(cart);

        // Sorting Buttons
        sortByNameButton = new JButton("Sort by Name");
        sortByNameButton.addActionListener(e -> {
            sortByName();
            JOptionPane.showMessageDialog(this, "Sorted by Name.");
        });

        sortByPriceButton = new JButton("Sort by Price");
        sortByPriceButton.addActionListener(e -> {
            sortByPrice();
            JOptionPane.showMessageDialog(this, "Sorted by Price.");
        });

        // New Plus and Minus Buttons
        plusButton = new JButton("Add (+)");
        plusButton.addActionListener(e -> adjustQuantity(1));

        minusButton = new JButton("Remove (-)");
        minusButton.addActionListener(e -> adjustQuantity(-1));

        // Pay Button
        payButton = new JButton("Pay with Card");
        payButton.addActionListener(e -> showPaymentDialog());

        // Button Panel (South)
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        buttonPanel.add(sortByNameButton);
        buttonPanel.add(sortByPriceButton);
        buttonPanel.add(plusButton);
        buttonPanel.add(minusButton);
        buttonPanel.add(payButton);

        // Add Components
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }


    private void sortByName() {
        TableRowSorter<NonEditableTableModel> sorter = (TableRowSorter<NonEditableTableModel>) table.getRowSorter();
        sorter.setComparator(0, String.CASE_INSENSITIVE_ORDER);
        sorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.ASCENDING))); // Column 0 (Name)
    }

    private void sortByPrice() {
        TableRowSorter<NonEditableTableModel> sorter = (TableRowSorter<NonEditableTableModel>) table.getRowSorter();
        sorter.setComparator(1, Comparator.comparingDouble(price -> Double.parseDouble(price.toString().replace("$", "").replace(",", ""))));
        sorter.setSortKeys(List.of(new RowSorter.SortKey(1, SortOrder.ASCENDING))); // Column 1 (Price)
    }

    private void refreshTable(ArrayList<Product> cart) {
        // Clear and add rows without sorting logic
        tableModel.setRowCount(0);
        HashMap<String, Integer> productCounts = new HashMap<>();
        HashMap<String, Double> productPrices = new HashMap<>();

        // Count products and group
        for (Product product : cart) {
            productCounts.put(product.getName(), productCounts.getOrDefault(product.getName(), 0) + 1);
            productPrices.put(product.getName(), product.getPrice());
        }

        // Populate
        for (String productName : productCounts.keySet()) {
            int count = productCounts.get(productName);
            double price = productPrices.get(productName);
            tableModel.addRow(new Object[]{productName + " (x" + count + ")", "$" + (price * count)});
        }

        // Add total row
        double totalSum = cart.stream().mapToDouble(Product::getPrice).sum();
        tableModel.addRow(new Object[]{"Current total:", "$" + totalSum});
    }
    private void adjustQuantity(int delta) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to modify.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String productNameWithCount = table.getValueAt(selectedRow, 0).toString();
        String productName = productNameWithCount.split(" \\(x")[0]; // Extract the name before " (xCount)"

        // Find the product in the cart
        Product targetProduct = null;
        for (Product product : cart) {
            if (product.getName().equals(productName)) {
                targetProduct = product;
                break;
            }
        }

        if (targetProduct != null) {
            int currentCount = countInstances(targetProduct);

            // Prevent removing the last instance
            if (delta == -1 && currentCount == 1) {
                JOptionPane.showMessageDialog(this, "Cannot remove the last instance of the product.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }


            if (delta == 1) {
                cart.add(targetProduct); // Add
            } else if (delta == -1) {
                cart.remove(targetProduct); // Remove
            }

            // Refresh table
            refreshTable(cart);
        } else {
            JOptionPane.showMessageDialog(this, "Selected product not found in the cart.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int countInstances(Product product) {
        int count = 0;
        for (Product p : cart) {
            if (p.getName().equals(product.getName())) {
                count++;
            }
        }
        return count;
    }


    private void showPaymentDialog() {
        // Tabbed pane to switch between payment methods
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel for Card Payment
        JPanel cardPaymentPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField cardNumberField = new JTextField();
        JTextField expirationDateField = new JTextField();
        JTextField cvcField = new JTextField();

        cardPaymentPanel.add(new JLabel("Card Number:"));
        cardPaymentPanel.add(cardNumberField);
        cardPaymentPanel.add(new JLabel("Expiration Date (MM/YY):"));
        cardPaymentPanel.add(expirationDateField);
        cardPaymentPanel.add(new JLabel("CVC:"));
        cardPaymentPanel.add(cvcField);

        tabbedPane.addTab("Pay with Card", cardPaymentPanel);

        // Panel for PayPal Payment
        JPanel paypalPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField paypalEmailField = new JTextField();

        paypalPanel.add(new JLabel("PayPal Email:"));
        paypalPanel.add(paypalEmailField);

        tabbedPane.addTab("Pay with PayPal", paypalPanel);

        // Show dialog with both tabs
        int option = JOptionPane.showConfirmDialog(this, tabbedPane, "Select Payment Method", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            // Handle Card Payment
            if (tabbedPane.getSelectedIndex() == 0) { // Card Payment Tab
                String cardNumber = cardNumberField.getText().trim();
                String expirationDate = expirationDateField.getText().trim();
                String cvc = cvcField.getText().trim();

                if (PaymentValidator.checkCard(cardNumber) && PaymentValidator.checkCVC(cvc, this) && PaymentValidator.checkDate(expirationDate, this)) {
                    JOptionPane.showMessageDialog(this, "Payment Successful.");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid payment information.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            // Handle PayPal Payment
            if (tabbedPane.getSelectedIndex() == 1) { // PayPal Tab
                String paypalEmail = paypalEmailField.getText().trim();

                if (PayPalValidator.isValidPayPalEmail(paypalEmail)) {
                    JOptionPane.showMessageDialog(this, "Payment Successful via PayPal!");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid PayPal email.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }


    // Comparator classes
    static class NameComparator implements Comparator<Product> {
        @Override
        public int compare(Product p1, Product p2) {
            return p1.getName().compareToIgnoreCase(p2.getName());
        }
    }

    static class PriceComparator implements Comparator<Product> {
        @Override
        public int compare(Product p1, Product p2) {
            return Double.compare(p1.getPrice(), p2.getPrice());
        }
    }
}

