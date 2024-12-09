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

        JFrame customerManagerDashboard = new JFrame();
        customerManagerDashboard.setLayout(new BorderLayout(20,20));
        customerManagerDashboard.setSize(800,800);
        customerManagerDashboard.setResizable(false);
        customerManagerDashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        customerManagerDashboard.setVisible(true);
        //top
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10)); 
        JLabel userLabel = new JLabel();
        JButton logoutButton = new JButton("Logout");
        userLabel.setText(currentUser.getFirstName() + " " + currentUser.getLastName() + " - " + currentUser.getUserName());
        topPanel.add(userLabel);
        topPanel.add(logoutButton);

        customerManagerDashboard.add(topPanel, BorderLayout.NORTH);

        //middle
        JPanel leftPanel = new JPanel(new BorderLayout(10,10));
        JPanel rightPanel = new JPanel(new BorderLayout(10,10));
        JLabel activeLabel = new JLabel("Cafe Active Customers:");
        JLabel inactiveLabel = new JLabel("Cafe Inactive Customers:");
        JTextArea activeArea = new JTextArea();
        activeArea.setPreferredSize(new Dimension(380,400));
        String activeText = "";
        String inactiveText = "";
        for (Map.Entry<String,User> user: cafe.DB.getUsers().entrySet()){
            if (user.getValue().isActive()){
                activeText += user.getValue().getLastName() + ", " + user.getValue().getFirstName() + "\n";
            }
            else{
                inactiveText += user.getValue().getLastName() + ", " + user.getValue().getFirstName() + "\n";
            }


        }
        activeArea.setText(activeText);
        activeArea.setEditable(false);
        JTextArea inactiveArea = new JTextArea();
        inactiveArea.setPreferredSize(new Dimension(380,400));
        inactiveArea.setText(inactiveText);
        inactiveArea.setEditable(false);

        JButton reactiveButton = new JButton("Re-activate");
        JButton inactiveButton = new JButton("Inactive");

        leftPanel.add(inactiveLabel, BorderLayout.NORTH);
        leftPanel.add(inactiveArea, BorderLayout.CENTER);
        leftPanel.add(reactiveButton, BorderLayout.SOUTH);
        rightPanel.add(activeLabel, BorderLayout.NORTH);
        rightPanel.add(activeArea, BorderLayout.CENTER);
        rightPanel.add(inactiveButton  , BorderLayout.SOUTH);

        customerManagerDashboard.add(leftPanel, BorderLayout.WEST);
        customerManagerDashboard.add(rightPanel, BorderLayout.EAST);

        //bottom
        JPanel bottomPanel = new JPanel(new GridLayout(2,1,20,20));
        JPanel buttonBottomPanel  = new JPanel(new GridLayout(1,3,30,30));
        JButton addButton= new JButton("Add");
        JButton editUserButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        buttonBottomPanel.add(addButton);
        buttonBottomPanel.add(editUserButton);
        buttonBottomPanel.add(addButton);
        buttonBottomPanel.add(deleteButton);
        bottomPanel.add(buttonBottomPanel);
        JPanel finalBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JComboBox<String> sortOrderBox = new JComboBox<>(new String[]{"Ascending","Descending"});
        finalBottomPanel.add(new JLabel("Sort Order:"));
        finalBottomPanel.add(sortOrderBox);
        JComboBox<String> searchOrSortBox = new JComboBox<>(new String[]{"Customer","Admin"});
        finalBottomPanel.add(new JLabel("Search/Sort By:"));
        finalBottomPanel.add(searchOrSortBox);
        JButton sortButton = new JButton("Sort");
        finalBottomPanel.add(sortButton);
        JTextField sortTextField = new JTextField("");
        sortTextField.setPreferredSize(new Dimension(250,30));
        finalBottomPanel.add(sortTextField);
        bottomPanel.add(finalBottomPanel);
        customerManagerDashboard.add(bottomPanel, BorderLayout.SOUTH);

        /* 

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
        
        mainPanel.add(logoutButton, BorderLayout.SOUTH);

        // Add buttons for adding and deleting users
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add User");
        JButton deleteButton = new JButton("Delete User");
        JButton moveButton = new JButton("Move User");
        */

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AdminDashboard(null, currentUser);
                dispose();
            }
        });
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new addCustomer();
                dispose();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteUser();
                dispose();
            }
        });



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

    /* 
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
        */
        private class addCustomer extends JFrame {

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
                activeButton.setSelected(true);
                JRadioButton inactiveButton = new JRadioButton("inactive");
                statusGroup.add(activeButton);
                statusGroup.add(inactiveButton);
        
                topPanel.add(new JLabel("Status: "));
                topPanel.add(activeButton);
                topPanel.add(inactiveButton);
        
                mainPanel.add(topPanel, BorderLayout.NORTH);
        /* 
                JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton okButton = new JButton("OK");
                JButton cancelButton = new JButton("Cancel");
                bottomPanel.add(okButton);
                bottomPanel.add(cancelButton);
                
        
                mainPanel.add(bottomPanel, BorderLayout.SOUTH);
                */
                add(mainPanel);
                setVisible(true);
                int result = JOptionPane.showConfirmDialog(null, mainPanel, "Add User", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String firstName = firstNameText.getText();
                    String lastName = lastNameText.getText();
                    String email = mailIdText.getText();
                    String password = new String(passwordText.getText());
                    String role = (String) userTypeComboBox.getSelectedItem();
                    boolean isActive = activeButton.isSelected(); 

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