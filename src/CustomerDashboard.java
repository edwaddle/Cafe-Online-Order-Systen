import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerDashboard extends JFrame {

    private UserManager userManager;
    private User currentUser; // The user who is currently logged in
    private JTextPane cartPane;
    private JTextPane billPane;
    private JTextPane menuPane;
    private StyledDocument cartDoc;
    private StyledDocument billDoc;
    private StyledDocument menuDoc;

    private JCheckBox breakfastCheckbox;
    private JCheckBox dinnerCheckbox;

    private JPanel tipPanel;
    private ButtonGroup tipGroup;
    private JRadioButton noTipButton;
    private JRadioButton tenPercentButton;
    private JRadioButton fifteenPercentButton;
    private JRadioButton twentyPercentButton;

    private JComboBox<String> sortByComboBox;
    private JTextField searchField;

    public CustomerDashboard(User currentUser) {
        this.currentUser = currentUser;
        this.userManager = new UserManager();

        setTitle("Customer Dashboard");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize components
        cartPane = new JTextPane();
        billPane = new JTextPane();
        menuPane = new JTextPane();
        cartDoc = cartPane.getStyledDocument();
        billDoc = billPane.getStyledDocument();
        menuDoc = menuPane.getStyledDocument();

        breakfastCheckbox = new JCheckBox("Breakfast");
        dinnerCheckbox = new JCheckBox("Dinner");

        tipPanel = new JPanel();
        tipGroup = new ButtonGroup();
        noTipButton = new JRadioButton("No Tip");
        tenPercentButton = new JRadioButton("10% Tip");
        fifteenPercentButton = new JRadioButton("15% Tip");
        twentyPercentButton = new JRadioButton("20% Tip");

        tipGroup.add(noTipButton);
        tipGroup.add(tenPercentButton);
        tipGroup.add(fifteenPercentButton);
        tipGroup.add(twentyPercentButton);

        tipPanel.add(noTipButton);
        tipPanel.add(tenPercentButton);
        tipPanel.add(fifteenPercentButton);
        tipPanel.add(twentyPercentButton);

        sortByComboBox = new JComboBox<>(new String[]{"title", "description", "itemID", "price"});
        searchField = new JTextField(20);

        // Add components to the frame
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel filterPanel = new JPanel();
        filterPanel.add(breakfastCheckbox);
        filterPanel.add(dinnerCheckbox);
        filterPanel.add(new JLabel("Sort By:"));
        filterPanel.add(sortByComboBox);
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);

        mainPanel.add(filterPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(menuPane), BorderLayout.CENTER);
        mainPanel.add(new JScrollPane(cartPane), BorderLayout.EAST);
        mainPanel.add(new JScrollPane(billPane), BorderLayout.SOUTH);
        mainPanel.add(tipPanel, BorderLayout.WEST);

        add(mainPanel);

        // Load menu items
        loadMenuItems();

        // Action listeners
        breakfastCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadMenuItems();
            }
        });

        dinnerCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadMenuItems();
            }
        });

        sortByComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadMenuItems();
            }
        });

        searchField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadMenuItems();
            }
        });

        // Add MouseListener to menuPane for double-click events
        menuPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int pos = menuPane.viewToModel(e.getPoint());
                    if (pos >= 0) {
                        try {
                            int start = menuDoc.getParagraphElement(pos).getStartOffset();
                            int end = menuDoc.getParagraphElement(pos).getEndOffset();
                            String selectedText = menuPane.getText(start, end - start).trim();
                            MenuItem selectedItem = findMenuItemByTitle(selectedText);
                            if (selectedItem != null) {
                                showItemDetails(selectedItem);
                            }
                        } catch (BadLocationException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        // Add MouseListener to cartPane for double-click events
        cartPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int pos = cartPane.viewToModel(e.getPoint());
                    if (pos >= 0) {
                        try {
                            int start = cartDoc.getParagraphElement(pos).getStartOffset();
                            int end = cartDoc.getParagraphElement(pos).getEndOffset();
                            String selectedText = cartPane.getText(start, end - start).trim();
                            MenuItem selectedItem = findMenuItemByTitle(selectedText);
                            if (selectedItem != null) {
                                removeItemFromCart(selectedItem);
                            }
                        } catch (BadLocationException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        // Add ActionListener to tip buttons
        noTipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateBill();
            }
        });

        tenPercentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateBill();
            }
        });

        fifteenPercentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateBill();
            }
        });

        twentyPercentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateBill();
            }
        });

        setVisible(true);
    }

    private void loadMenuItems() {
        try {
            menuDoc.remove(0, menuDoc.getLength());
            List<MenuItem> filteredMenu = new ArrayList<>();
            for (MenuItem item : cafe.DB.getMenu()) {
                if ((breakfastCheckbox.isSelected() && item instanceof PancakeMenuItem) ||
                    (dinnerCheckbox.isSelected() && item instanceof DinerMenuItem)) {
                    filteredMenu.add(item);
                }
            }

            // Sort the filtered menu
            Utils.sortMenuItems(filteredMenu, (String) sortByComboBox.getSelectedItem());

            // Search the filtered menu
            String searchQuery = searchField.getText();
            if (!searchQuery.isEmpty()) {
                filteredMenu = Utils.searchMenuItems(filteredMenu, searchQuery);
            }

            for (MenuItem item : filteredMenu) {
                menuDoc.insertString(menuDoc.getLength(), item.getTitle() + "\n", null);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private MenuItem findMenuItemByTitle(String title) {
        for (MenuItem item : cafe.DB.getMenu()) {
            if (item.getTitle().equals(title)) {
                return item;
            }
        }
        return null;
    }

    private void showItemDetails(MenuItem item) {
        JOptionPane.showMessageDialog(this,
                "Title: " + item.getTitle() + "\n" +
                        "Description: " + item.getDescription() + "\n" +
                        "Price: " + item.getPrice() + "\n" +
                        "Count: " + item.getCount() + "\n" +
                        "Available: " + item.isAvailable() + "\n" +
                        "Current: " + item.isCurrent(),
                "Item Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addItemToCart(MenuItem item) {
        try {
            cartDoc.insertString(cartDoc.getLength(), item.getTitle() + "\n", null);
            updateBill();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void removeItemFromCart(MenuItem item) {
        try {
            String itemTitle = item.getTitle();
            String cartText = cartPane.getText();
            int start = cartText.indexOf(itemTitle);
            if (start >= 0) {
                int end = start + itemTitle.length();
                cartDoc.remove(start, end - start);
                updateBill();
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void updateBill() {
        try {
            billDoc.remove(0, billDoc.getLength());
            float total = 0;
            String cartText = cartPane.getText();
            for (MenuItem item : cafe.DB.getMenu()) {
                if (cartText.contains(item.getTitle())) {
                    total += item.getPrice();
                }
            }
            float tip = 0;
            if (tenPercentButton.isSelected()) {
                tip = total * 0.10f;
            } else if (fifteenPercentButton.isSelected()) {
                tip = total * 0.15f;
            } else if (twentyPercentButton.isSelected()) {
                tip = total * 0.20f;
            }
            float grandTotal = total + tip;
            billDoc.insertString(billDoc.getLength(), "Subtotal: $" + total + "\n", null);
            billDoc.insertString(billDoc.getLength(), "Tip: $" + tip + "\n", null);
            billDoc.insertString(billDoc.getLength(), "Grand Total: $" + grandTotal + "\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}