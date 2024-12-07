import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class CustomerManagementScreen extends JFrame {

    private UserManager userManager;
    private User currentUser; // The user who is currently logged in
    private JTextPane inactiveCustomersPane;
    private JTextPane activeCustomersPane;
    private StyledDocument inactiveUsersDoc;
    private StyledDocument activeUsersDoc;
    private JComboBox<String> userTypeComboBox;
    private Map<String, String> nameToUserNameMap = new HashMap<>();

    public CustomerManagementScreen(User currentUser) {
        this.currentUser = currentUser;
        this.userManager = new UserManager();

        setTitle("Customer Management");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize components
        inactiveCustomersPane = new JTextPane();
        activeCustomersPane = new JTextPane();
        inactiveUsersDoc = inactiveCustomersPane.getStyledDocument();
        activeUsersDoc = activeCustomersPane.getStyledDocument();

        userTypeComboBox = new JComboBox<>(new String[]{"Customer", "Admin"});

        // Add components to the frame
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("User Type:"));
        filterPanel.add(userTypeComboBox);

        mainPanel.add(filterPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(activeCustomersPane), BorderLayout.CENTER);
        mainPanel.add(new JScrollPane(inactiveCustomersPane), BorderLayout.SOUTH);

        add(mainPanel);

        // Load users
        loadUsers();

        // Action listeners
        userTypeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadUsers();
            }
        });

        setVisible(true);
    }

    private void loadUsers() {
        try {
            activeUsersDoc.remove(0, activeUsersDoc.getLength());
            inactiveUsersDoc.remove(0, inactiveUsersDoc.getLength());
            for (User user : cafe.DB.getUsers().values()) {
                if (user.getRole().equals(userTypeComboBox.getSelectedItem())) {
                    if (user.isActive()) {
                        activeUsersDoc.insertString(activeUsersDoc.getLength(), user.getUserName() + "\n", null);
                    } else {
                        inactiveUsersDoc.insertString(inactiveUsersDoc.getLength(), user.getUserName() + "\n", null);
                    }
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}