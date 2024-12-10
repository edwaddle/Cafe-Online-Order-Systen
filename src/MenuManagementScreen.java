import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MenuManagementScreen extends JFrame {

    private UserManager userManager;
    private MenuManager menuManager;
    private User currentUser; // The user who is currently logged in
    private JTextPane inSeasonPane;
    private JTextPane outOfSeasonPane;
    private StyledDocument inSeasonDoc;
    private StyledDocument outOfSeasonDoc;
    JCheckBox breakfastCheckbox;
    JCheckBox dinnerCheckBox; 
    private Map<String, String> nameToItemIDMap = new HashMap<>();

    private JComboBox<String> sortByComboBox;
    private JTextField searchField;
    private JComboBox<String> sortOrderBox;

    public MenuManagementScreen(User currentUser) {
        this.currentUser = currentUser;
        this.userManager = new UserManager();
        this.menuManager = new MenuManager(cafe.DB.getMenu());

        setTitle("Menu Management");
        setSize(900, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize components
        inSeasonPane = new JTextPane();
        outOfSeasonPane = new JTextPane();
        inSeasonDoc = inSeasonPane.getStyledDocument();
        outOfSeasonDoc = outOfSeasonPane.getStyledDocument();
        inSeasonPane.setEditable(false);
        outOfSeasonPane.setEditable(false);

        //menuTypeComboBox = new JComboBox<>(new String[]{"Diner", "Pancake"});
        sortByComboBox = new JComboBox<>(new String[]{"Title", "Description", "ItemID", "Price"});
        searchField = new JTextField(20);
        sortOrderBox = new JComboBox<>(new String[]{"Ascending", "Descending"});

        // Top Panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JLabel userLabel = new JLabel();
        JButton logoutButton = new JButton("Logout");
        userLabel.setText(currentUser.getFirstName() + " " + currentUser.getLastName() + " - " + currentUser.getUserName());
        topPanel.add(userLabel);
        topPanel.add(logoutButton);

        JPanel upperInnerLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        breakfastCheckbox = new JCheckBox("Breakfast/Lunch");
        breakfastCheckbox.setSelected(true);
        dinnerCheckBox = new JCheckBox("Dinner");
        dinnerCheckBox.setSelected(true);
        upperInnerLeftPanel.add(breakfastCheckbox);
        upperInnerLeftPanel.add(dinnerCheckBox);

        JPanel menuManagerDashInner = new JPanel();
        menuManagerDashInner.setLayout(new BoxLayout(menuManagerDashInner, BoxLayout.X_AXIS));
        menuManagerDashInner.add(upperInnerLeftPanel);
        menuManagerDashInner.add(topPanel);
        add(menuManagerDashInner, BorderLayout.NORTH);

        // Middle Panel
        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 20, 20));
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        JLabel activeLabel = new JLabel("Backup (Off-season) Menu");
        JLabel inactiveLabel = new JLabel("Current Menu");
        JButton reactiveButton = new JButton("Re-activate");
        JButton inactiveButton = new JButton("Inactive");

        leftPanel.add(activeLabel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(outOfSeasonPane), BorderLayout.CENTER);
        leftPanel.add(reactiveButton, BorderLayout.SOUTH);
        rightPanel.add(inactiveLabel, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(inSeasonPane), BorderLayout.CENTER);
        rightPanel.add(inactiveButton, BorderLayout.SOUTH);

        middlePanel.add(leftPanel);
        middlePanel.add(rightPanel);
        add(middlePanel, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        JPanel buttonBottomPanel = new JPanel(new GridLayout(1, 3, 30, 30));
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        buttonBottomPanel.add(addButton);
        buttonBottomPanel.add(editButton);
        buttonBottomPanel.add(deleteButton);
        bottomPanel.add(buttonBottomPanel);

        JPanel finalBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        finalBottomPanel.add(new JLabel("Sort Order:"));
        finalBottomPanel.add(sortOrderBox);
        finalBottomPanel.add(new JLabel("Search/Sort By:"));
        finalBottomPanel.add(sortByComboBox);
        JButton sortButton = new JButton("Sort");
        finalBottomPanel.add(sortButton);
        JTextField searchTextField = new JTextField("");
        searchTextField.setPreferredSize(new Dimension(200, 30));
        finalBottomPanel.add(searchTextField);
        JButton searchButton = new JButton("Search");
        finalBottomPanel.add(searchButton);
        bottomPanel.add(finalBottomPanel);
        add(bottomPanel, BorderLayout.SOUTH);

        loadMenuItems();

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AdminDashboard(null, currentUser);
            }
        });

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addMenuItem();
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editMenuItem();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteMenuItem();
            }
        });

        reactiveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveMenuItem(outOfSeasonPane, true);
            }
        });

        inactiveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveMenuItem(inSeasonPane, false);
            }
        });

        sortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadMenuItems();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadMenuItems();
            }
        });

        sortByComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadMenuItems();
            }
        });

        dinnerCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadMenuItems();
            }
        });

        breakfastCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadMenuItems();
            }
        });

        sortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadMenuItems();
            }
        });

        searchField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadMenuItems();
            }
        });

        setVisible(true);
    }

    private void loadMenuItems() {
        try {
            inSeasonDoc.remove(0, inSeasonDoc.getLength());
            outOfSeasonDoc.remove(0, outOfSeasonDoc.getLength());
            nameToItemIDMap.clear();
            List<MenuItem> menuItems = new ArrayList<>();
            for (MenuItem item : cafe.DB.getMenu()) {
                if ((breakfastCheckbox.isSelected() && item instanceof PancakeMenuItem) ||
                    (dinnerCheckBox.isSelected() && item instanceof DinerMenuItem)) {
                    menuItems.add(item);
                }
            }

            // Sort the menu items
            String sortBy = (String) sortByComboBox.getSelectedItem();
            Comparator<MenuItem> comparator = null;
            switch (sortBy) {
                case "Title":
                    comparator = Comparator.comparing(MenuItem::getTitle);
                    break;
                case "Description":
                    comparator = Comparator.comparing(MenuItem::getDescription);
                    break;
                case "ItemID":
                    comparator = Comparator.comparing(MenuItem::getItemID);
                    break;
                case "Price":
                    comparator = Comparator.comparing(MenuItem::getPrice);
                    break;
                default:
                    // Default sorting by Title
                    comparator = Comparator.comparing(MenuItem::getTitle);
                    break;
            }

            String sortOrder = (String) sortOrderBox.getSelectedItem();
            if (sortOrder.equals("Descending")) {
                comparator = comparator.reversed();
            }

            menuItems.sort(comparator);

            String searchQuery = searchField.getText();
            if (!searchQuery.isEmpty()) {
                Pattern pattern = Pattern.compile(searchQuery, Pattern.CASE_INSENSITIVE);
                menuItems = menuItems.stream()
                        .filter(item -> pattern.matcher(item.getTitle()).find() ||
                                        pattern.matcher(item.getDescription()).find() ||
                                        pattern.matcher(item.getItemID()).find() ||
                                        pattern.matcher(String.valueOf(item.getPrice())).find())
                        .collect(Collectors.toList());
            }

            for (MenuItem item : menuItems) {
                if (item.isCurrent()) {
                    inSeasonDoc.insertString(inSeasonDoc.getLength(), item.getTitle() + "\n", null);
                } else {
                    outOfSeasonDoc.insertString(outOfSeasonDoc.getLength(), item.getTitle() + "\n", null);
                }
                nameToItemIDMap.put(item.getTitle(), item.getItemID());
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private MenuItem findMenuItemByID(String itemID) {
        for (MenuItem item : cafe.DB.getMenu()) {
            if (item.getItemID().equals(itemID)) {
                return item;
            }
        }
        return null;
    }


    private void addMenuItem() {
        JTextField titleField = new JTextField(10);
        JTextField itemIDField = new JTextField(10);
        JTextField descriptionField = new JTextField(10);
        JTextField priceField = new JTextField(10);
        JTextField countField = new JTextField(10);
        JComboBox<String> menuTypeComboBox = new JComboBox<>(new String[]{"Diner", "Pancake"});
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"In Season", "Out of Season"});

        JPanel panel = new JPanel(new GridLayout(7, 2));
        panel.add(new JLabel("Menu Type:"));
        panel.add(menuTypeComboBox);
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Item ID:"));
        panel.add(itemIDField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Count:"));
        panel.add(countField);
        panel.add(new JLabel("Status:"));
        panel.add(statusComboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Menu Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String itemID = itemIDField.getText();
            String description = descriptionField.getText();
            float price = Float.parseFloat(priceField.getText());
            int count = Integer.parseInt(countField.getText());
            String menuType = (String) menuTypeComboBox.getSelectedItem();
            boolean isCurrent = statusComboBox.getSelectedItem().equals("In Season");

            MenuItem newItem;
            if (menuType.equals("Diner")) {
                newItem = new DinerMenuItem(title, itemID, description, price, count, isCurrent);
            } else {
                newItem = new PancakeMenuItem(title, itemID, description, price, count, isCurrent);
            }

            try {
                menuManager.addMenuItem(newItem);
                cafe.DB.saveData();
                loadMenuItems();
            } catch (CustomExceptions.ItemAlreadyExistsException e) {
                JOptionPane.showMessageDialog(this, "Item already exists!");
            }
        }
    }

    private void editMenuItem() {
        String selectedText = getSelectedText(inSeasonPane);
        if (selectedText == null) {
            selectedText = getSelectedText(outOfSeasonPane);
        }

        if (selectedText != null && nameToItemIDMap.containsKey(selectedText)) {
            String itemID = nameToItemIDMap.get(selectedText);
            MenuItem itemToEdit = findMenuItemByID(itemID);
            if (itemToEdit != null) {
                JTextField titleField = new JTextField(itemToEdit.getTitle(), 10);
                JTextField itemIDField = new JTextField(itemToEdit.getItemID(), 10);
                JTextField descriptionField = new JTextField(itemToEdit.getDescription(), 10);
                JTextField priceField = new JTextField(String.valueOf(itemToEdit.getPrice()), 10);
                JTextField countField = new JTextField(String.valueOf(itemToEdit.getCount()), 10);
                JComboBox<String> menuTypeComboBox = new JComboBox<>(new String[]{"Diner", "Pancake"});
                menuTypeComboBox.setSelectedItem(itemToEdit.getMenuType());
                JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"In Season", "Out of Season"});
                statusComboBox.setSelectedItem(itemToEdit.isCurrent() ? "In Season" : "Out of Season");

                JPanel panel = new JPanel(new GridLayout(7, 2));
                panel.add(new JLabel("Menu Type:"));
                panel.add(menuTypeComboBox);
                panel.add(new JLabel("Title:"));
                panel.add(titleField);
                panel.add(new JLabel("Item ID:"));
                panel.add(itemIDField);
                panel.add(new JLabel("Description:"));
                panel.add(descriptionField);
                panel.add(new JLabel("Price:"));
                panel.add(priceField);
                panel.add(new JLabel("Count:"));
                panel.add(countField);
                panel.add(new JLabel("Status:"));
                panel.add(statusComboBox);

                int result = JOptionPane.showConfirmDialog(null, panel, "Edit Menu Item", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String title = titleField.getText();
                    itemID = itemIDField.getText();
                    String description = descriptionField.getText();
                    float price = Float.parseFloat(priceField.getText());
                    int count = Integer.parseInt(countField.getText());
                    String menuType = (String) menuTypeComboBox.getSelectedItem();
                    boolean isCurrent = statusComboBox.getSelectedItem().equals("In Season");

                    MenuItem editedItem;
                    if (menuType.equals("Diner")) {
                        editedItem = new DinerMenuItem(title, itemID, description, price, count, isCurrent);
                    } else {
                        editedItem = new PancakeMenuItem(title, itemID, description, price, count, isCurrent);
                    }

                    try {
                        menuManager.updateMenuItem(itemToEdit, editedItem);
                        cafe.DB.saveData();
                        loadMenuItems();
                    } catch (CustomExceptions.ItemNotFoundException e) {
                        JOptionPane.showMessageDialog(this, "Item not found!");
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No item selected!");
        }
    }

    private void deleteMenuItem() {
        String selectedText = getSelectedText(inSeasonPane);
        if (selectedText == null) {
            selectedText = getSelectedText(outOfSeasonPane);
        }

        if (selectedText != null && nameToItemIDMap.containsKey(selectedText)) {
            String itemID = nameToItemIDMap.get(selectedText);
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
            } else {
                JOptionPane.showMessageDialog(this, "Menu item not found!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "No item selected!");
        }
    }

    private void moveMenuItem(JTextPane pane, boolean toActive) {
        String selectedText = getSelectedText(pane);
        if (selectedText != null && nameToItemIDMap.containsKey(selectedText)) {
            String itemID = nameToItemIDMap.get(selectedText);
            MenuItem itemToMove = findMenuItemByID(itemID);
            if (itemToMove != null) {
                itemToMove.setCurrent(toActive);
                cafe.DB.saveData();
                loadMenuItems();
                JOptionPane.showMessageDialog(this, "Item moved successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Menu item not found!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "No item selected!");
        }
    }

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
}