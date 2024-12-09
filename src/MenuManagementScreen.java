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
                        int start = pane.getStyledDocument().getParagraphElement(pos).getStartOffset();
                        int end = pane.getStyledDocument().getParagraphElement(pos).getEndOffset();
                        pane.setSelectionStart(start);
                        pane.setSelectionEnd(end);
                        
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