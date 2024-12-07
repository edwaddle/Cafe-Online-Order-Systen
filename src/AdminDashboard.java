import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboard extends JFrame {

    private User admin;

    public AdminDashboard(JFrame parent, User admin) {
        super("Admin Dashboard");
        this.admin = admin;
        UserManager userManager = new UserManager();

        setLayout(new BorderLayout());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create panels for different functionalities
        JPanel manageUsersPanel = new JPanel();
        JPanel manageMenuPanel = new JPanel();
        JPanel orderFoodPanel = new JPanel();

        // Add buttons to manage users
        JButton addUserButton = new JButton("Add User");
        JButton editUserButton = new JButton("Edit User");
        JButton deleteUserButton = new JButton("Delete User");

        manageUsersPanel.add(addUserButton);
        manageUsersPanel.add(editUserButton);
        manageUsersPanel.add(deleteUserButton);

        // Add buttons to manage menu
        JButton addMenuItemButton = new JButton("Add Menu Item");
        JButton editMenuItemButton = new JButton("Edit Menu Item");
        JButton deleteMenuItemButton = new JButton("Delete Menu Item");

        manageMenuPanel.add(addMenuItemButton);
        manageMenuPanel.add(editMenuItemButton);
        manageMenuPanel.add(deleteMenuItemButton);

        // Add button to order food
        JButton orderFoodButton = new JButton("Order Food as Customer");
        orderFoodPanel.add(orderFoodButton);

        // Add panels to the main frame
        add(manageUsersPanel, BorderLayout.NORTH);
        add(manageMenuPanel, BorderLayout.CENTER);
        add(orderFoodPanel, BorderLayout.SOUTH);

        // Action listeners for buttons
        addUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement add user functionality
            }
        });

        editUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement edit user functionality
            }
        });

        deleteUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement delete user functionality
            }
        });

        addMenuItemButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement add menu item functionality
            }
        });

        editMenuItemButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement edit menu item functionality
            }
        });

        deleteMenuItemButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement delete menu item functionality
            }
        });

        orderFoodButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement order food as customer functionality
            }
        });

        setVisible(true);
    }
}