import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class MenuManagementScreen extends JFrame {

    private UserManager userManager;
    private MenuManager menuManager;
    private User currentUser; // The user who is currently logged in
    private JTextPane inSeasonPane;
    private JTextPane outOfSeasonPane;
    private StyledDocument inSeasonDoc;
    private StyledDocument outOfSeasonDoc;
    private JComboBox<String> menuTypeComboBox;
    private Map<String, String> nameToItemIDMap = new HashMap<>();

    private JComboBox<String> sortByComboBox;
    private JTextField searchField;

    public MenuManagementScreen(User currentUser) {
        this.currentUser = currentUser;
        this.userManager = new UserManager();
        this.menuManager = new MenuManager(cafe.DB.getMenu());

        setTitle("Menu Management");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Initialize components
        inSeasonPane = new JTextPane();
        outOfSeasonPane = new JTextPane();
        inSeasonDoc = inSeasonPane.getStyledDocument();
        outOfSeasonDoc = outOfSeasonPane.getStyledDocument();

        menuTypeComboBox = new JComboBox<>(new String[]{"Diner", "Pancake"});
        sortByComboBox = new JComboBox<>(new String[]{"title", "description", "itemID", "price"});
        searchField = new JTextField(20);

        // Add components to the frame
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("Menu Type:"));
        filterPanel.add(menuTypeComboBox);
        filterPanel.add(new JLabel("Sort By:"));
        filterPanel.add(sortByComboBox);
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);

        mainPanel.add(filterPanel, BorderLayout.NORTH);

        // Add columns for in-season and out-of-season items
        JPanel columnsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        columnsPanel.add(createColumnPanel(inSeasonPane, "Move to Out of Season"));
        columnsPanel.add(createColumnPanel(outOfSeasonPane, "Move to In Season"));
        mainPanel.add(columnsPanel, BorderLayout.CENTER);

        // Add a logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AdminDashboard(null, currentUser);
            }
        });
        mainPanel.add(logoutButton, BorderLayout.SOUTH);

        // Add buttons for adding and deleting items
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Item");
        JButton deleteButton = new JButton("Delete Item");
        JButton moveButton = new JButton("Move Item");

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addMenuItem();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteMenuItem();
            }
        });

        moveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveMenuItem();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(moveButton);
        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        add(mainPanel);

        // Load menu items
        loadMenuItems();

        // Action listeners
        menuTypeComboBox.addActionListener(new ActionListener() {
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

        setVisible(true);
    }

    private JPanel createColumnPanel(JTextPane pane, String buttonLabel) {
        JPanel columnPanel = new JPanel(new BorderLayout());
        pane.setEditable(false);
        pane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int pos = pane.viewToModel(e.getPoint());
                    if (pos >= 0) {
                        try {
                            int start = pane.getStyledDocument().getParagraphElement(pos).getStartOffset();
                            int end = pane.getStyledDocument().getParagraphElement(pos).getEndOffset();
                            pane.setSelectionStart(start);
                            pane.setSelectionEnd(end);
                        } catch (BadLocationException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else if (e.getClickCount() == 2) {
                    int pos = pane.viewToModel(e.getPoint());
                    if (pos >= 0) {
                        try {
                            int start = pane.getStyledDocument().getParagraphElement(pos).getStartOffset();
                            int end = pane.getStyledDocument().getParagraphElement(pos).getEndOffset();
                            String selectedText = pane.getText(start, end - start).trim();
                            if (nameToItemIDMap.containsKey(selectedText)) {
                                String itemID = nameToItemIDMap.get(selectedText);
                                MenuItem selectedItem = findMenuItemByID(itemID);
                                if (selectedItem != null) {
                                    showItemDetails(selectedItem);
                                }
                            }
                        } catch (BadLocationException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        JButton moveButton = new JButton(buttonLabel);
        moveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedText = getSelectedText(pane);
                if (selectedText != null && nameToItemIDMap.containsKey(selectedText)) {
                    String itemID = nameToItemIDMap.get(selectedText);
                    MenuItem item = findMenuItemByID(itemID);
                    if (item != null) {
                        item.setCurrent(!item.isCurrent());
                        cafe.DB.saveData();
                        loadMenuItems();
                    }
                }
            }
        });

        columnPanel.add(new JScrollPane(pane), BorderLayout.CENTER);
        columnPanel.add(moveButton, BorderLayout.SOUTH);
        return columnPanel;
    }

    private void loadMenuItems() {
        try {
            inSeasonDoc.remove(0, inSeasonDoc.getLength());
            outOfSeasonDoc.remove(0, outOfSeasonDoc.getLength());
            nameToItemIDMap.clear();
            List<MenuItem> menuItems = new ArrayList<>();
            for (MenuItem item : cafe.DB.getMenu()) {
                if (item.getMenuType().equals(menuTypeComboBox.getSelectedItem())) {
                    menuItems.add(item);
                }
            }

            // Sort the menu items
            Utils.sortMenuItems(menuItems, (String) sortByComboBox.getSelectedItem());

            // Search the menu items
            String searchQuery = searchField.getText();
            if (!searchQuery.isEmpty()) {
                menuItems = Utils.searchMenuItems(menuItems, searchQuery);
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

    private void deleteMenuItem() {
        String itemID = JOptionPane.showInputDialog(this, "Enter item ID to delete:");
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
    }

    private void moveMenuItem() {
        String itemID = JOptionPane.showInputDialog(this, "Enter item ID to move:");
        MenuItem itemToMove = findMenuItemByID(itemID);
        if (itemToMove != null) {
            boolean newCurrentStatus = !itemToMove.isCurrent();
            itemToMove.setCurrent(newCurrentStatus);
            cafe.DB.saveData();
            loadMenuItems();
            JOptionPane.showMessageDialog(this, "Item moved successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Menu item not found!");
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