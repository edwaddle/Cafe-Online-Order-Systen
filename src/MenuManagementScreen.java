import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MenuManagementScreen extends JFrame {

    private UserManager userManager;
    private User currentUser; // The user who is currently logged in
    private JTextPane menuPane;
    private StyledDocument menuDoc;
    private JComboBox<String> menuTypeComboBox;
    private Map<String, String> nameToItemIDMap = new HashMap<>();

    public MenuManagementScreen(User currentUser) {
        this.currentUser = currentUser;
        this.userManager = new UserManager();

        setTitle("Menu Management");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize components
        menuPane = new JTextPane();
        menuDoc = menuPane.getStyledDocument();

        menuTypeComboBox = new JComboBox<>(new String[]{"Diner", "Pancake"});

        // Add components to the frame
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("Menu Type:"));
        filterPanel.add(menuTypeComboBox);

        mainPanel.add(filterPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(menuPane), BorderLayout.CENTER);

        add(mainPanel);

        // Load menu items
        loadMenuItems();

        // Action listeners
        menuTypeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadMenuItems();
            }
        });

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

        setVisible(true);
    }

    private void loadMenuItems() {
        try {
            menuDoc.remove(0, menuDoc.getLength());
            nameToItemIDMap.clear();
            for (MenuItem item : cafe.DB.getMenu()) {
                if (item.getMenuType().equals(menuTypeComboBox.getSelectedItem())) {
                    menuDoc.insertString(menuDoc.getLength(), item.getTitle() + "\n", null);
                    nameToItemIDMap.put(item.getTitle(), item.getItemID());
                }
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
}