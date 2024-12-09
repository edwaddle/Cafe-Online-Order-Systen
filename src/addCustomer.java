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

public class addCustomer extends JFrame {

    // Constructor to initialize the form
    public addCustomer() {
        // Set title, size, and default close operation
        setTitle("Add Customer");
        setSize(1200, 130);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Dispose on close to return to previous window
        setLocationRelativeTo(null); // Center the window on the screen
        

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("UserType: "));
        JComboBox<String> userTypeBox = new JComboBox<>(new String[]{"Customer","Admin"});
        topPanel.add(userTypeBox);

        topPanel.add(new JLabel("First Name: "));
        JTextField firstNameText = new JTextField("");
        firstNameText.setPreferredSize(new Dimension(100, 30));
        topPanel.add(firstNameText);

        topPanel.add(new JLabel("Last Name: "));
        JTextField lastNameText = new JTextField("");
        lastNameText.setPreferredSize(new Dimension(100, 30));
        topPanel.add(lastNameText);
        
        topPanel.add(new JLabel("Mail ID: "));
        JTextField mailIdText = new JTextField("");
        mailIdText.setPreferredSize(new Dimension(100, 30));
        topPanel.add(mailIdText);
        
        topPanel.add(new JLabel("Password: "));
        JTextField passwordText = new JTextField("");
        passwordText.setPreferredSize(new Dimension(100, 30));
        topPanel.add(passwordText);

        ButtonGroup statusGroup = new ButtonGroup();
        JRadioButton activeButton = new JRadioButton("active");
        JRadioButton inactiveButton = new JRadioButton("inactive");
        statusGroup.add(activeButton);
        statusGroup.add(inactiveButton);

        topPanel.add(new JLabel("Status: "));
        topPanel.add(activeButton);
        topPanel.add(inactiveButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        bottomPanel.add(okButton);
        bottomPanel.add(cancelButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
        int result = JOptionPane.showConfirmDialog(null, panel, "Add User", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) userTypeComboBox.getSelectedItem();
            boolean isActive = statusComboBox.getSelectedItem().equals("Active");

            String userName = generateUserName(firstName);
            User newUser;
            if (role.equals("Admin")) {
                newUser = new Admin(firstName, lastName, email, userName, password, isActive);
            } else {
                newUser = new Customer(firstName, lastName, email, userName, password, isActive);
            }

            try {
                userManager.addUser(cafe.DB.getUsers(), newUser);
                cafe.DB.saveData();
                loadUsers();
            } catch (CustomExceptions.UserAlreadyExistsException e) {
                JOptionPane.showMessageDialog(this, "User already exists!");
            }
        }
    }

    // Main method to test the form
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new addCustomer();
            }
        });
    }
}