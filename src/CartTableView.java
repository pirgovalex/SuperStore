import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

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

        // Pay Button with Pop-up Menu
        payButton = new JButton("Pay with Card");
        payButton.addActionListener(e -> showPaymentDialog());

        // Button Panel (South)
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        buttonPanel.add(sortByNameButton);
        buttonPanel.add(sortByPriceButton);
        buttonPanel.add(payButton);

        // Add Components
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void sortByName() {
        cart.sort(new NameComparator());
        refreshTable(cart);
    }

    private void sortByPrice() {
        cart.sort(new PriceComparator());
        refreshTable(cart);
    }

    private void refreshTable(ArrayList<Product> cart) {
        // Map to track product count
        HashMap<String, Integer> productCounts = new HashMap<>();
        HashMap<String, Double> productPrices = new HashMap<>();

        // Calculate counts and prices
        for (Product product : cart) {
            productCounts.put(product.getName(), productCounts.getOrDefault(product.getName(), 0) + 1);
            productPrices.put(product.getName(), product.getPrice());
        }

        // Clear
        tableModel.setRowCount(0);

        // Add grouped rows
        for (String productName : productCounts.keySet()) {
            int count = productCounts.get(productName);
            double price = productPrices.get(productName);
            tableModel.addRow(new Object[]{productName + " (x" + count + ")", "$" + (price * count)});
        }

        // Calculate and add the total sum
        double totalSum = cart.stream().mapToDouble(Product::getPrice).sum();
        tableModel.addRow(new Object[]{"Current total:", "$" + totalSum});
    }

    private void showPaymentDialog() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        // Input fields for card number, expiration date, and CVC
        JTextField cardNumberField = new JTextField();
        JTextField expirationDateField = new JTextField();
        JTextField cvcField = new JTextField();

        panel.add(new JLabel("Card Number:"));
        panel.add(cardNumberField);
        panel.add(new JLabel("Expiration Date (MM/YY):"));
        panel.add(expirationDateField);
        panel.add(new JLabel("CVC:"));
        panel.add(cvcField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Enter Payment Information", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String cardNumber = cardNumberField.getText().trim();
            String expirationDate = expirationDateField.getText().trim();
            String cvc = cvcField.getText().trim();
           /* PaymentValidator.checkCard(cardNumber);
            PaymentValidator.checkCVC(cvc,this);
            PaymentValidator.checkDate(expirationDate,this);*/
            // Call the PaymentValidator methods
            if (PaymentValidator.checkCard(cardNumber) && PaymentValidator.checkCVC(cvc,this)&& PaymentValidator.checkDate(expirationDate,this)) {
                JOptionPane.showMessageDialog(this, "Payment Successful.");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid payment information.", "Error", JOptionPane.ERROR_MESSAGE);
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

