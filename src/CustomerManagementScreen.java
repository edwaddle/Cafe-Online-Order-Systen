import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
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
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize components
        inactiveCustomersPane = new JTextPane();
        activeCustomersPane = new JTextPane();
        inactiveUsersDoc = inactiveCustomersPane.getStyledDocument();
        activeUsersDoc = activeCustomersPane.getStyledDocument();

        userTypeComboBox = new JComboBox<>(new String[]{"Customer", "Admin"});
        sortByComboBox = new JComboBox<>(new String[]{"firstName", "lastName", "email", "userName"});
        searchField = new JTextField(20);

        // Top Panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JLabel userLabel = new JLabel();
        JButton logoutButton = new JButton("Logout");
        userLabel.setText(currentUser.getFirstName() + " " + currentUser.getLastName() + " - " + currentUser.getUserName());
        topPanel.add(userLabel);
        topPanel.add(logoutButton);
        add(topPanel, BorderLayout.NORTH);

        // Middle Panel
        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 20, 20));
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        JLabel activeLabel = new JLabel("Cafe Active Customers:");
        JLabel inactiveLabel = new JLabel("Cafe Inactive Customers:");
        JTextArea activeArea = new JTextArea();
        activeArea.setPreferredSize(new Dimension(380, 400));
        JTextArea inactiveArea = new JTextArea();
        inactiveArea.setPreferredSize(new Dimension(380, 400));
        JButton reactiveButton = new JButton("Re-activate");
        JButton inactiveButton = new JButton("Inactive");

        leftPanel.add(inactiveLabel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(inactiveArea), BorderLayout.CENTER);
        leftPanel.add(reactiveButton, BorderLayout.SOUTH);
        rightPanel.add(activeLabel, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(activeArea), BorderLayout.CENTER);
        rightPanel.add(inactiveButton, BorderLayout.SOUTH);
        middlePanel.add(leftPanel);
        middlePanel.add(rightPanel);
        add(middlePanel, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        JPanel buttonBottomPanel = new JPanel(new GridLayout(1, 3, 30, 30));
        JButton addButton = new JButton("Add");
        JButton editUserButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        buttonBottomPanel.add(addButton);
        buttonBottomPanel.add(editUserButton);
        buttonBottomPanel.add(deleteButton);
        bottomPanel.add(buttonBottomPanel);

        JPanel finalBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JComboBox<String> sortOrderBox = new JComboBox<>(new String[]{"Ascending", "Descending"});
        finalBottomPanel.add(new JLabel("Sort Order:"));
        finalBottomPanel.add(sortOrderBox);
        JComboBox<String> searchOrSortBox = new JComboBox<>(new String[]{"Customer", "Admin"});
        finalBottomPanel.add(new JLabel("Search/Sort By:"));
        finalBottomPanel.add(searchOrSortBox);
        JButton sortButton = new JButton("Sort");
        finalBottomPanel.add(sortButton);
        JTextField sortTextField = new JTextField("");
        sortTextField.setPreferredSize(new Dimension(250, 30));
        finalBottomPanel.add(sortTextField);
        bottomPanel.add(finalBottomPanel);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load users
        loadUsers();

        // Action listeners
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AdminDashboard(null, currentUser);
            }
        });

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });

        editUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editUser();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });

        reactiveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveUser(inactiveArea, true);
            }
        });

        inactiveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveUser(activeArea, false);
            }
        });

        sortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadUsers();
            }
        });

        searchOrSortBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadUsers();
            }
        });

        sortOrderBox.addActionListener(new ActionListener() {
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
        activeArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int pos = activeArea.viewToModel(e.getPoint());
                    if (pos >= 0) {
                        try {
                            int start = activeUsersDoc.getParagraphElement(pos).getStartOffset();
                            int end = activeUsersDoc.getParagraphElement(pos).getEndOffset();
                            String selectedText = activeArea.getText(start, end - start).trim();
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
        inactiveArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int pos = inactiveArea.viewToModel(e.getPoint());
                    if (pos >= 0) {
                        try {
                            int start = inactiveUsersDoc.getParagraphElement(pos).getStartOffset();
                            int end = inactiveUsersDoc.getParagraphElement(pos).getEndOffset();
                            String selectedText = inactiveArea.getText(start, end - start).trim();
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
        JTextField firstNameField = new JTextField(10);
        JTextField lastNameField = new JTextField(10);
        JTextField emailField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);
        JComboBox<String> userTypeComboBox = new JComboBox<>(new String[]{"Customer", "Admin"});
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Active", "Inactive"});

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("User Type:"));
        panel.add(userTypeComboBox);
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Status:"));
        panel.add(statusComboBox);

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

    private void editUser() {
        String selectedText = getSelectedText(activeCustomersPane);
        if (selectedText == null) {
            selectedText = getSelectedText(inactiveCustomersPane);
        }

        if (selectedText != null) {
            User userToEdit = findUserByUserName(selectedText);
            if (userToEdit != null) {
                JTextField firstNameField = new JTextField(userToEdit.getFirstName(), 10);
                JTextField lastNameField = new JTextField(userToEdit.getLastName(), 10);
                JTextField emailField = new JTextField(userToEdit.getEmail(), 10);
                JPasswordField passwordField = new JPasswordField(userToEdit.getPassword(), 10);
                JComboBox<String> userTypeComboBox = new JComboBox<>(new String[]{"Customer", "Admin"});
                userTypeComboBox.setSelectedItem(userToEdit.getRole());
                JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Active", "Inactive"});
                statusComboBox.setSelectedItem(userToEdit.isActive() ? "Active" : "Inactive");

                JPanel panel = new JPanel(new GridLayout(6, 2));
                panel.add(new JLabel("User Type:"));
                panel.add(userTypeComboBox);
                panel.add(new JLabel("First Name:"));
                panel.add(firstNameField);
                panel.add(new JLabel("Last Name:"));
                panel.add(lastNameField);
                panel.add(new JLabel("Email:"));
                panel.add(emailField);
                panel.add(new JLabel("Password:"));
                panel.add(passwordField);
                panel.add(new JLabel("Status:"));
                panel.add(statusComboBox);

                int result = JOptionPane.showConfirmDialog(null, panel, "Edit User", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String firstName = firstNameField.getText();
                    String lastName = lastNameField.getText();
                    String email = emailField.getText();
                    String password = new String(passwordField.getPassword());
                    String role = (String) userTypeComboBox.getSelectedItem();
                    boolean isActive = statusComboBox.getSelectedItem().equals("Active");

                    User editedUser;
                    if (role.equals("Admin")) {
                        editedUser = new Admin(firstName, lastName, email, userToEdit.getUserName(), password, isActive);
                    } else {
                        editedUser = new Customer(firstName, lastName, email, userToEdit.getUserName(), password, isActive);
                    }

                    try {
                        userManager.updateUser(cafe.DB.getUsers(), userToEdit.getUserName(), editedUser);
                        cafe.DB.saveData();
                        loadUsers();
                    } catch (CustomExceptions.UserNotFoundException e) {
                        JOptionPane.showMessageDialog(this, "User not found!");
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No user selected!");
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

    private void moveUser(JTextArea area, boolean toActive) {
        String selectedText = getSelectedText(area);
        if (selectedText != null) {
            User userToMove = findUserByUserName(selectedText);
            if (userToMove != null) {
                userToMove.setActive(toActive);
                cafe.DB.saveData();
                loadUsers();
                JOptionPane.showMessageDialog(this, "User moved successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "User not found!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "No user selected!");
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
    private String getSelectedText(JTextArea area) {
    int selectionStart = area.getSelectionStart();
    int selectionEnd = area.getSelectionEnd();
    if (selectionStart != selectionEnd) {
        try {
            return area.getText(selectionStart, selectionEnd - selectionStart).trim();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    return null;
}
}