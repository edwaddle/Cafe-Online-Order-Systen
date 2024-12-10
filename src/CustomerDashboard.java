import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CustomerDashboard extends JFrame {

    Map<String, Float> cartItems = new HashMap<>();

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
    private JRadioButton fifteenTipButton;
    private JRadioButton eighteenTipButton;
    private JRadioButton twentyTipButton;

    private JComboBox<String> sortByComboBox;
    JComboBox<String> sortOrderBox;
    private JTextField searchField;

    public CustomerDashboard(User currentUser) {
        this.currentUser = currentUser;
        this.userManager = new UserManager();

        setTitle("Customer Dashboard");
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        //left side
        JPanel leftPanel = new JPanel(new BorderLayout(10,10));
        JPanel innerLeftPanel = new JPanel();
        innerLeftPanel.setLayout(new BoxLayout(innerLeftPanel, BoxLayout.Y_AXIS));

        JPanel upperInnerLeftPanel = new JPanel();
        upperInnerLeftPanel.setPreferredSize(new Dimension(380, 30));
        upperInnerLeftPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));

         breakfastCheckbox = new JCheckBox("Breakfast/Lunch");
         breakfastCheckbox.setSelected(true);
         dinnerCheckbox = new JCheckBox("Dinner");
         dinnerCheckbox.setSelected(true);

        upperInnerLeftPanel.add(breakfastCheckbox);
        upperInnerLeftPanel.add(dinnerCheckbox);
        innerLeftPanel.add(upperInnerLeftPanel);

        cartPane = new JTextPane();
        cartPane.setPreferredSize(new Dimension(380, 200));
        cartDoc = cartPane.getStyledDocument();
        String orderedItems = "";
        for (String item: currentUser.getOrderedItems()){
            orderedItems += item;
            for (MenuItem itemName : cafe.DB.getMenu()){
                if (itemName.getTitle().equals( item)){
                    orderedItems += " " + itemName.getPrice();
                }
                else{
                    orderedItems += "IDK";
                }
            }
        }
        cartPane.setText(orderedItems);
        JLabel cartLabel = new JLabel("Cart:");
        cartLabel.setFont(new Font("Serif", Font.PLAIN, 30));
        innerLeftPanel.add(cartLabel);
        innerLeftPanel.add(cartPane);

        billPane = new JTextPane();
        billPane.setPreferredSize(new Dimension(380, 200));
        billDoc = billPane.getStyledDocument();
        String billItems = "";
        double billCost = 0.0;
        for (String item: currentUser.getOrderedItems()){
            billItems += item;
            for (MenuItem itemName : cafe.DB.getMenu()){
                if (itemName.getTitle().equals( item)){
                    billItems += " " + itemName.getPrice() + "\n";
                    billCost += itemName.getPrice();
                }
            }
        }
        if (billItems.length() != 0){
            billItems += ("Subtotal: " + billCost + "\n" +"Tax: " + billCost*(0.0725) + "\n"  + 
            "Tip: " + "\n Total: " + billCost*(1.0725)
            ); //IMPLEMENT TIP
            
        }
        billPane.setText(billItems);
        JLabel billLabel = new JLabel("Bill:");
        billLabel.setFont(new Font("Serif", Font.PLAIN, 30));
        innerLeftPanel.add(billLabel);
        innerLeftPanel.add(billPane);

        ButtonGroup tipGroup = new ButtonGroup();
        JRadioButton noTipButton = new JRadioButton("No tip");
        noTipButton.setSelected(true); 
        fifteenTipButton = new JRadioButton("15% tip");
        eighteenTipButton = new JRadioButton("18% tip");
        twentyTipButton = new JRadioButton("20% tip");

        tipGroup.add(noTipButton);
        tipGroup.add(fifteenTipButton);
        tipGroup.add(eighteenTipButton);
        tipGroup.add(twentyTipButton);
        JPanel tipPanel = new JPanel();
        tipPanel.add(noTipButton);
        tipPanel.add(fifteenTipButton);
        tipPanel.add(eighteenTipButton);
        tipPanel.add(twentyTipButton);
        innerLeftPanel.add(tipPanel);

        leftPanel.add(innerLeftPanel);

        //Right Panel
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JPanel upperRightPanel = new JPanel();
        upperRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        upperRightPanel.setPreferredSize(new Dimension(380, 30));
        JLabel userLabel = new JLabel();
        JButton logoutButton = new JButton("Logout");
        userLabel.setText(currentUser.getFirstName() + " " + currentUser.getLastName() + " - " + currentUser.getUserName());
        upperRightPanel.add(userLabel);
        upperRightPanel.add(logoutButton);
        rightPanel.add(upperRightPanel);

        menuPane = new JTextPane();
        menuPane.setPreferredSize(new Dimension(380, 500));
        menuDoc = menuPane.getStyledDocument();
        String menuItems = "";
        double menuCost = 0.0;
        for (MenuItem itemName : cafe.DB.getMenu()){
            if (itemName.isAvailable()){
                menuItems += itemName.getTitle() + " " + itemName.getPrice() + "\n";
            }
        }
        /* 
        if (menuItems.length() != 0){
            menuItems += ("Subtotal: " + menuCost + "\n" +"Tax: " + menuCost*(0.0725) + "\n"  + 
            "Tip: " + "\n Total: " + menuCost*(1.0725)
            ); //IMPLEMENT TIP
            
        }*/
        menuPane.setText(menuItems);
        JLabel menuLabel = new JLabel("Menu:");
        menuLabel.setFont(new Font("Serif", Font.PLAIN, 30));
        rightPanel.add(menuLabel);
        rightPanel.add(menuPane);

        //Bottom

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel leftBottomPanel = new JPanel();
        JButton cancelButton = new JButton("Cancel");
        JButton orderButton = new JButton("Order");
        leftBottomPanel.add(cancelButton);
        leftBottomPanel.add(orderButton);
        bottomPanel.add(leftBottomPanel, BorderLayout.WEST);

        JPanel rightBottomPanel = new JPanel();
        JButton addToCartButton = new JButton("Add to Cart");
        rightBottomPanel.add(addToCartButton);
        bottomPanel.add(rightBottomPanel, BorderLayout.EAST);

        JPanel finalBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        sortOrderBox = new JComboBox<>(new String[]{"Ascending","Descending"});
        finalBottomPanel.add(new JLabel("Sort Order:"));
        finalBottomPanel.add(sortOrderBox);
        sortByComboBox = new JComboBox<>(new String[]{"Title","Description","ItemID","Price"});
        finalBottomPanel.add(new JLabel("Search/Sort By:"));
        finalBottomPanel.add(sortByComboBox);
        JButton sortButton = new JButton("Sort");
        finalBottomPanel.add(sortButton);
        searchField = new JTextField("");
        searchField.setPreferredSize(new Dimension(200,30));
        finalBottomPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        finalBottomPanel.add(searchButton);
        bottomPanel.add(finalBottomPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);

       

        // Load menu items
        loadMenuItems();

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AdminDashboard(null, currentUser);
            }
        });
        
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
        addToCartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addItemToCart();
            }
        });
        fifteenTipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                updateBill();
            }
        });
        eighteenTipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                updateBill();
            }
        });
        twentyTipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                updateBill();
            }
        });
        noTipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                updateBill();
            }
        });
        sortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                loadMenuItems();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelOrder();
            }
        });
        orderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                orderFunction();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadMenuItems();
            }
        });

        
        

        setVisible(true);
    }

    private void cancelOrder(){

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this menu item?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                cartDoc.remove(0, cartDoc.getLength());
                cartItems.clear();
                JOptionPane.showMessageDialog(this, "Cancelled successfully!");
                loadMenuItems();
                updateBill();
            } 
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Menu item not found!");
            }
        }
    }
    private void orderFunction(){

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you are ready to order?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                cartDoc.remove(0, cartDoc.getLength());
                cartItems.clear();
                JOptionPane.showMessageDialog(this, "Ordered successfully!");
                loadMenuItems();
                updateBill();
            } 
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Menu item not found!");
            }
        }
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
    
            System.out.println("Filtered Menu Size: " + filteredMenu.size());
    
            String sortBy = (String) sortByComboBox.getSelectedItem(); // Sorting criteria
            String sortOrder = (String) sortOrderBox.getSelectedItem(); // Sorting order (Ascending/Descending)
    
            System.out.println("Sort By: " + sortBy);
            System.out.println("Sort Order: " + sortOrder);
    
            Comparator<MenuItem> comparator = null;
            switch (sortBy) {
                case "Title":
                    comparator = Comparator.comparing(MenuItem::getTitle, Comparator.nullsLast(String::compareTo));
                    break;
                case "Description":
                    comparator = Comparator.comparing(MenuItem::getDescription, Comparator.nullsLast(String::compareTo));
                    break;
                case "ItemID":
                    comparator = Comparator.comparing(MenuItem::getItemID, Comparator.nullsLast(String::compareTo));
                    break;
                case "Price":
                    comparator = Comparator.comparing(MenuItem::getPrice);
                    break;
                default:
                    comparator = Comparator.comparing(MenuItem::getTitle); // default to Title
                    break;
            }
    
            if ("Descending".equalsIgnoreCase(sortOrder)) {
                comparator = comparator.reversed();
            }
        /* 
           
            if (comparator == null) {
                comparator = Comparator.comparing(MenuItem::getTitle);
            }
            */
    
            filteredMenu.sort(comparator);
    
            String searchQuery = searchField.getText();
            if (!searchQuery.isEmpty()) {
                Pattern pattern = Pattern.compile(searchQuery, Pattern.CASE_INSENSITIVE);
                filteredMenu = filteredMenu.stream()
                        .filter(item -> pattern.matcher(item.getTitle()).find() ||
                                        pattern.matcher(item.getDescription()).find() ||
                                        pattern.matcher(item.getItemID()).find() ||
                                        pattern.matcher(String.valueOf(item.getPrice())).find())
                        .collect(Collectors.toList());
            }
    
            for (MenuItem item : filteredMenu) {
                menuDoc.insertString(menuDoc.getLength(), item.getTitle() + " " + item.getPrice() + "\n", null);
            }
    
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }




    private void addItemToCart() {
        String selectedText = getSelectedText(menuPane);
    
        if (selectedText == null || selectedText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No item selected!");
            return;
        }
        
        MenuItem matchedItem = null;
        for (MenuItem item : cafe.DB.getMenu()) {
            String itemDetails = item.getTitle() + " " + item.getPrice();
            if (itemDetails.equals(selectedText)) {
                matchedItem = item;
                
                break;
            }
        }
        
        if (matchedItem != null) {
            String selectedString = matchedItem.getTitle() + " " + matchedItem.getPrice() + "\n";
            try {
                
                cartDoc.insertString(cartDoc.getLength(), selectedString, null);
                cartItems.put(matchedItem.getTitle(), matchedItem.getPrice());
                
                
            } catch (BadLocationException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to add item to cart.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Menu item not found!");
        }
        
        updateBill();
    }

        
            /* 
            MenuItem itemToDelete = findMenuItemByID(itemID);
            if (itemToDelete != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this menu item?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        menuManager.removeMenuItem(itemToDelete);
                        cafe.DB.saveData();
                        JOptionPane.showMessageDialog(this, "Menu item deleted successfully!");
                        loadMenuItems();
                    } catch (CustomExceptions.ItemNotFoundException ex) {
                        JOptionPane.showMessageDialog(this, "Menu item not found!");
                    }
                }
                */
        
    

    private String getSelectedText(JTextPane pane) {
        int selectionStart = pane.getSelectionStart();
        int selectionEnd = pane.getSelectionEnd();
        if (selectionStart != selectionEnd) {
            try {
                return pane.getText(selectionStart, selectionEnd - selectionStart).trim();
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private void updateBill() {
        try {
            billDoc.remove(0, billDoc.getLength());
            float total = 0;
            for (Map.Entry<String, Float> entry : cartItems.entrySet()) {
                billDoc.insertString(billDoc.getLength(), entry.getKey() + ": " + entry.getValue() + "\n", null);
                total += entry.getValue();
            }
            /* 
            for (MenuItem item : cafe.DB.getMenu()) {
                if (cartText.contains(item.getTitle())) {
                    total += item.getPrice();
                }
            }
            */
            float tip = 0;
            if (fifteenTipButton.isSelected()) {
                tip = total * 0.10f;
            } else if (eighteenTipButton.isSelected()) {
                tip = total * 0.15f;
            } else if (twentyTipButton.isSelected()) {
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CustomerDashboard(new Customer("John", "Doe", "john.doe@example.com", "johndoe", "password", true));
            }
        });
    }
}