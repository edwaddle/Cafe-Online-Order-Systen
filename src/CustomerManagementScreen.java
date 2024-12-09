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
import java.util.stream.Collectors;

public class CustomerManagementScreen extends JFrame {

    private UserManager userManager;
    private User currentUser; // The user who is currently logged in
    private JTextPane inactiveCustomersPane;
    private JTextPane activeCustomersPane;
    private StyledDocument inactiveUsersDoc;
    private StyledDocument activeUsersDoc;
    private JComboBox<String> userTypeComboBox;
    private Map<String, String> nameToUserNameMap = new HashMap<>();

    private JComboBox<String> sortByComboBox;
    private JTextField searchField;

    public CustomerManagementScreen(User currentUser) {
        this.currentUser = currentUser;
        this.userManager = new UserManager();

        setTitle("Customer Management");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Initialize components
        inactiveCustomersPane = new JTextPane();
        activeCustomersPane = new JTextPane();
        inactiveUsersDoc = inactiveCustomersPane.getStyledDocument();
        activeUsersDoc = activeCustomersPane.getStyledDocument();

        userTypeComboBox = new JComboBox<>(new String[]{"Customer", "Admin"});
        sortByComboBox = new JComboBox<>(new String[]{"firstName", "lastName", "email", "userName"});
        searchField = new JTextField(20);

        // Add components to the frame
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("User Type:"));
        filterPanel.add(userTypeComboBox);
        filterPanel.add(new JLabel("Sort By:"));
        filterPanel.add(sortByComboBox);
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);

        mainPanel.add(filterPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(activeCustomersPane), BorderLayout.CENTER);
        mainPanel.add(new JScrollPane(inactiveCustomersPane), BorderLayout.SOUTH);

        // Add a logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AdminDashboard(null, currentUser);
            }
        });
        mainPanel.add(logoutButton, BorderLayout.SOUTH);

        // Add buttons for adding and deleting users
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add User");
        JButton deleteButton = new JButton("Delete User");
        JButton moveButton = new JButton("Move User");

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });

        moveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveUser();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(moveButton);
        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        add(mainPanel);

        // Load users
        loadUsers();

        // Action listeners
        userTypeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadUsers();
            }
        });

        sortByComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadUsers();
            }
        });

        searchField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadUsers();
            }
        });

        // Add MouseListener to activeCustomersPane for double-click events
        activeCustomersPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int pos = activeCustomersPane.viewToModel(e.getPoint());
                    if (pos >= 0) {
                        try {
                            int start = activeUsersDoc.getParagraphElement(pos).getStartOffset();
                            int end = activeUsersDoc.getParagraphElement(pos).getEndOffset();
                            String selectedText = activeCustomersPane.getText(start, end - start).trim();
                            User selectedUser = findUserByUserName(selectedText);
                            if (selectedUser != null) {
                                showUserDetails(selectedUser);
                            }
                        } catch (BadLocationException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        // Add MouseListener to inactiveCustomersPane for double-click events
        inactiveCustomersPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int pos = inactiveCustomersPane.viewToModel(e.getPoint());
                    if (pos >= 0) {
                        try {
                            int start = inactiveUsersDoc.getParagraphElement(pos).getStartOffset();
                            int end = inactiveUsersDoc.getParagraphElement(pos).getEndOffset();
                            String selectedText = inactiveCustomersPane.getText(start, end - start).trim();
                            User selectedUser = findUserByUserName(selectedText);
                            if (selectedUser != null) {
                                showUserDetails(selectedUser);
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

    private void loadUsers() {
        try {
            activeUsersDoc.remove(0, activeUsersDoc.getLength());
            inactiveUsersDoc.remove(0, inactiveUsersDoc.getLength());
            List<User> users = new ArrayList<>(cafe.DB.getUsers().values());

            // Filter by user type
            users = users.stream()
                    .filter(user -> user.getRole().equals(userTypeComboBox.getSelectedItem()))
                    .collect(Collectors.toList());

            // Sort the users
            Utils.sortUsers(users, (String) sortByComboBox.getSelectedItem());

            // Search the users
            String searchQuery = searchField.getText();
            if (!searchQuery.isEmpty()) {
                users = Utils.searchUsers(users, searchQuery);
            }

            for (User user : users) {
                if (user.isActive()) {
                    activeUsersDoc.insertString(activeUsersDoc.getLength(), user.getUserName() + "\n", null);
                } else {
                    inactiveUsersDoc.insertString(inactiveUsersDoc.getLength(), user.getUserName() + "\n", null);
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private User findUserByUserName(String userName) {
        return cafe.DB.getUsers().get(userName);
    }

    private void showUserDetails(User user) {
        JOptionPane.showMessageDialog(this,
                "First Name: " + user.getFirstName() + "\n" +
                        "Last Name: " + user.getLastName() + "\n" +
                        "Email: " + user.getEmail() + "\n" +
                        "User Name: " + user.getUserName() + "\n" +
                        "Active: " + user.isActive(),
                "User Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addUser() {
        String firstName = JOptionPane.showInputDialog(this, "Enter first name:");
        String lastName = JOptionPane.showInputDialog(this, "Enter last name:");
        String email = JOptionPane.showInputDialog(this, "Enter email:");
        String password = JOptionPane.showInputDialog(this, "Enter password:");
        String role = JOptionPane.showInputDialog(this, "Enter role (Admin/Customer):");

        String userName = generateUserName(firstName);
        User newUser;
        if (role.equals("Admin")) {
            newUser = new Admin(firstName, lastName, email, userName, password, true);
        } else {
            newUser = new Customer(firstName, lastName, email, userName, password, true);
        }

        try {
            userManager.addUser(cafe.DB.getUsers(), newUser);
            cafe.DB.saveData();
            loadUsers();
        } catch (CustomExceptions.UserAlreadyExistsException e) {
            JOptionPane.showMessageDialog(this, "User already exists!");
        }
    }

    private void deleteUser() {
        String userName = JOptionPane.showInputDialog(this, "Enter user name to delete:");
        User userToDelete = cafe.DB.getUsers().get(userName);
        if (userToDelete != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    userManager.removeUser(cafe.DB.getUsers(), userName);
                    cafe.DB.saveData();
                    JOptionPane.showMessageDialog(this, "User deleted successfully!");
                    loadUsers();
                } catch (CustomExceptions.UserNotFoundException ex) {
                    JOptionPane.showMessageDialog(this, "User not found!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "User not found!");
        }
    }

    private void moveUser() {
        String userName = JOptionPane.showInputDialog(this, "Enter user name to move:");
        User userToMove = cafe.DB.getUsers().get(userName);
        if (userToMove != null) {
            boolean newActiveStatus = !userToMove.isActive();
            userToMove.setActive(newActiveStatus);
            cafe.DB.saveData();
            loadUsers();
            JOptionPane.showMessageDialog(this, "User moved successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "User not found!");
        }
    }

    private String generateUserName(String firstName) {
        String baseUserName = firstName.toLowerCase();
        String userName;
        do {
            String randomDigits = Utils.generateRandomNumber(4);
            userName = baseUserName + randomDigits;
        } while (cafe.DB.getUsers().containsKey(userName));
        return userName;
    }
}