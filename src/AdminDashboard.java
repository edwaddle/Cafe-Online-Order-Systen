import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class AdminDashboard extends JFrame {

    private User admin;
    private UserManager userManager;
    private MenuManager menuManager;

    public AdminDashboard(JFrame parent, User admin) {
        super("Admin Dashboard");
        this.admin = admin;
        this.userManager = new UserManager();
        this.menuManager = new MenuManager(cafe.DB.getMenu());

      setLayout(new BorderLayout());
      setSize(800, 600);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      JLabel welcomeLabel = new JLabel("Welcome, " + admin.getUserName() + "!", SwingConstants.CENTER); // Center alignment
      welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 30));
      welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the text
      JPanel welcomeWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 50));
      welcomeWrapper.add(welcomeLabel); 
      add(welcomeWrapper, BorderLayout.NORTH);

      JPanel centerPanel = new JPanel();
      centerPanel.setLayout(new GridLayout(2, 1, 30, 30)); // 2 rows, 1 column layout
      centerPanel.setPreferredSize(new Dimension(800, 200));

      JPanel topButtonPanel = new JPanel();
      topButtonPanel.setLayout(new GridLayout(1, 2,20,50)); // 1 row, 2 columns

      JButton customerManagerButton = new JButton("Manage Customers");
      JButton menuManagerButton = new JButton("Manage Menu");
      JButton customerLoginButton = new JButton("Login as Customer");

      customerManagerButton.setPreferredSize(new Dimension(380, 50));
      menuManagerButton.setPreferredSize(new Dimension(380, 50));
      customerLoginButton.setPreferredSize(new Dimension(800, 50));

      topButtonPanel.add(customerManagerButton);
      topButtonPanel.add(menuManagerButton);

      centerPanel.add(topButtonPanel); // Add top buttons
      centerPanel.add(customerLoginButton); // Add full-width button

      JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 150));
      wrapperPanel.add(centerPanel); 
      add(wrapperPanel, BorderLayout.CENTER);

      customerManagerButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
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
            userLabel.setText(admin.getFirstName() + " " + admin.getLastName() + " - " + admin.getUserName());
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
            JButton addUserButton= new JButton("Add");
            JButton editUserButton = new JButton("Edit");
            JButton deleteUserButton = new JButton("Delete");
            buttonBottomPanel.add(addUserButton);
            buttonBottomPanel.add(editUserButton);
            buttonBottomPanel.add(addUserButton);
            buttonBottomPanel.add(deleteUserButton);
            bottomPanel.add(buttonBottomPanel);

            JPanel finalBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            JComboBox<String> sortOrderBox = new JComboBox<>(new String[]{"Ascending","Descending"});
            finalBottomPanel.add(new JLabel("Sort Order:"));
            finalBottomPanel.add(sortOrderBox);
            JComboBox<String> searchOrSortBox = new JComboBox<>(new String[]{"Name","Username"});
            finalBottomPanel.add(new JLabel("Search/Sort By:"));
            finalBottomPanel.add(searchOrSortBox);
            JButton sortButton = new JButton("Sort");
            finalBottomPanel.add(sortButton);
            JTextField sortTextField = new JTextField("");
            sortTextField.setPreferredSize(new Dimension(250,30));
            finalBottomPanel.add(sortTextField);
            bottomPanel.add(finalBottomPanel);

            customerManagerDashboard.add(bottomPanel, BorderLayout.SOUTH);


        }
      });

      menuManagerButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
            JFrame menuManagerDash = new JFrame();
            menuManagerDash.setLayout(new BorderLayout(20,20));
            menuManagerDash.setSize(800,800);
            menuManagerDash.setResizable(false);
            menuManagerDash.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            menuManagerDash.setVisible(true);
            //top
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10)); 
            JLabel userLabel = new JLabel();
            JButton logoutButton = new JButton("Logout");
            userLabel.setText(admin.getFirstName() + " " + admin.getLastName() + " - " + admin.getUserName());
            topPanel.add(userLabel);
            topPanel.add(logoutButton);

            menuManagerDash.add(topPanel, BorderLayout.NORTH);

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

            menuManagerDash.add(leftPanel, BorderLayout.WEST);
            menuManagerDash.add(rightPanel, BorderLayout.EAST);

            //bottom
            JPanel bottomPanel = new JPanel(new GridLayout(2,1,20,20));
            JPanel buttonBottomPanel  = new JPanel(new GridLayout(1,3,30,30));
            JButton addUserButton= new JButton("Add");
            JButton editUserButton = new JButton("Edit");
            JButton deleteUserButton = new JButton("Delete");
            buttonBottomPanel.add(addUserButton);
            buttonBottomPanel.add(editUserButton);
            buttonBottomPanel.add(addUserButton);
            buttonBottomPanel.add(deleteUserButton);
            bottomPanel.add(buttonBottomPanel);

            JPanel finalBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
            JComboBox<String> sortOrderBox = new JComboBox<>(new String[]{"Ascending","Descending"});
            finalBottomPanel.add(new JLabel("Sort Order:"));
            finalBottomPanel.add(sortOrderBox);
            JComboBox<String> searchOrSortBox = new JComboBox<>(new String[]{"Title","Description","ItemID","Price"});
            finalBottomPanel.add(new JLabel("Search/Sort By:"));
            finalBottomPanel.add(searchOrSortBox);
            JButton sortButton = new JButton("Sort");
            finalBottomPanel.add(sortButton);
            JTextField searchTextField = new JTextField("");
            searchTextField.setPreferredSize(new Dimension(200,30));
            finalBottomPanel.add(searchTextField);
            JButton searchButton = new JButton("Search");
            finalBottomPanel.add(searchButton);
            bottomPanel.add(finalBottomPanel, BorderLayout.SOUTH);


            menuManagerDash.add(bottomPanel, BorderLayout.SOUTH);


            }
      });


        /* 

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
                addUser();
            }
        });

        editUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editUser();
            }
        });

        deleteUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });

        addMenuItemButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addMenuItem();
            }
        });

        editMenuItemButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editMenuItem();
            }
        });

        deleteMenuItemButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteMenuItem();
            }
        });

        orderFoodButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                orderFood();
            }
        });
        */

        setVisible(true);
    }

    private void addUser() {
        // Implement add user functionality
        JTextField firstNameField = new JTextField(10);
        JTextField lastNameField = new JTextField(10);
        JTextField emailField = new JTextField(10);
        JTextField userNameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);
        JCheckBox isActiveCheckBox = new JCheckBox("Active");
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Admin", "Customer"});

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("User Name:"));
        panel.add(userNameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Active:"));
        panel.add(isActiveCheckBox);
        panel.add(new JLabel("Role:"));
        panel.add(roleComboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add User", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String userName = userNameField.getText();
            String password = new String(passwordField.getPassword());
            boolean isActive = isActiveCheckBox.isSelected();
            String role = (String) roleComboBox.getSelectedItem();

            User newUser;
            if (role.equals("Admin")) {
                newUser = new Admin(firstName, lastName, email, userName, password, isActive);
            } else {
                newUser = new Customer(firstName, lastName, email, userName, password, isActive);
            }

            try {
                userManager.addUser(cafe.DB.getUsers(), newUser);
                cafe.DB.saveData();
                JOptionPane.showMessageDialog(null, "User added successfully!");
            } catch (CustomExceptions.UserAlreadyExistsException ex) {
                JOptionPane.showMessageDialog(null, "User already exists!");
            }
        }
    }

    private void editUser() {
        // Implement edit user functionality
        JTextField userNameField = new JTextField(10);
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("User Name:"));
        panel.add(userNameField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Edit User", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String userName = userNameField.getText();
            User user = cafe.DB.getUsers().get(userName);
            if (user != null) {
                editUserDetails(user);
            } else {
                JOptionPane.showMessageDialog(null, "User not found!");
            }
        }
    }

    private void editUserDetails(User user) {
        JTextField firstNameField = new JTextField(user.getFirstName(), 10);
        JTextField lastNameField = new JTextField(user.getLastName(), 10);
        JTextField emailField = new JTextField(user.getEmail(), 10);
        JPasswordField passwordField = new JPasswordField(user.getPassword(), 10);
        JCheckBox isActiveCheckBox = new JCheckBox("Active", user.isActive());
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Admin", "Customer"});
        roleComboBox.setSelectedItem(user.getRole());

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Active:"));
        panel.add(isActiveCheckBox);
        panel.add(new JLabel("Role:"));
        panel.add(roleComboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Edit User Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            boolean isActive = isActiveCheckBox.isSelected();
            String role = (String) roleComboBox.getSelectedItem();

            User updatedUser;
            if (role.equals("Admin")) {
                updatedUser = new Admin(firstName, lastName, email, user.getUserName(), password, isActive);
            } else {
                updatedUser = new Customer(firstName, lastName, email, user.getUserName(), password, isActive);
            }

            try {
                userManager.updateUser(cafe.DB.getUsers(), user.getUserName(), updatedUser);
                cafe.DB.saveData();
                JOptionPane.showMessageDialog(null, "User updated successfully!");
            } catch (CustomExceptions.UserNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "User not found!");
            }
        }
    }

    private void deleteUser() {
        // Implement delete user functionality
        JTextField userNameField = new JTextField(10);
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("User Name:"));
        panel.add(userNameField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Delete User", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String userName = userNameField.getText();
            try {
                userManager.removeUser(cafe.DB.getUsers(), userName);
                cafe.DB.saveData();
                JOptionPane.showMessageDialog(null, "User deleted successfully!");
            } catch (CustomExceptions.UserNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "User not found!");
            }
        }
    }

    private void addMenuItem() {
        // Implement add menu item functionality
        JTextField titleField = new JTextField(10);
        JTextField itemIDField = new JTextField(10);
        JTextField descriptionField = new JTextField(10);
        JTextField priceField = new JTextField(10);
        JTextField countField = new JTextField(10);
        JCheckBox isCurrentCheckBox = new JCheckBox("Current");
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Diner", "Pancake"});

        JPanel panel = new JPanel(new GridLayout(0, 2));
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
        panel.add(new JLabel("Current:"));
        panel.add(isCurrentCheckBox);
        panel.add(new JLabel("Type:"));
        panel.add(typeComboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Menu Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String itemID = itemIDField.getText();
            String description = descriptionField.getText();
            float price = Float.parseFloat(priceField.getText());
            int count = Integer.parseInt(countField.getText());
            boolean isCurrent = isCurrentCheckBox.isSelected();
            String type = (String) typeComboBox.getSelectedItem();

            MenuItem newItem;
            if (type.equals("Diner")) {
                newItem = new DinerMenuItem(title, itemID, description, price, count, isCurrent);
            } else {
                newItem = new PancakeMenuItem(title, itemID, description, price, count, isCurrent);
            }

            try {
                menuManager.addMenuItem(newItem);
                cafe.DB.saveData();
                JOptionPane.showMessageDialog(null, "Menu item added successfully!");
            } catch (CustomExceptions.ItemAlreadyExistsException ex) {
                JOptionPane.showMessageDialog(null, "Menu item already exists!");
            }
        }
    }

    private void editMenuItem() {
        // Implement edit menu item functionality
        JTextField itemIDField = new JTextField(10);
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Item ID:"));
        panel.add(itemIDField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Menu Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String itemID = itemIDField.getText();
            MenuItem item = findMenuItemByID(itemID);
            if (item != null) {
                editMenuItemDetails(item);
            } else {
                JOptionPane.showMessageDialog(null, "Menu item not found!");
            }
        }
    }

    private void editMenuItemDetails(MenuItem item) {
        JTextField titleField = new JTextField(item.getTitle(), 10);
        JTextField descriptionField = new JTextField(item.getDescription(), 10);
        JTextField priceField = new JTextField(String.valueOf(item.getPrice()), 10);
        JTextField countField = new JTextField(String.valueOf(item.getCount()), 10);
        JCheckBox isCurrentCheckBox = new JCheckBox("Current", item.isCurrent());
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Diner", "Pancake"});
        typeComboBox.setSelectedItem(item.getMenuType());

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Count:"));
        panel.add(countField);
        panel.add(new JLabel("Current:"));
        panel.add(isCurrentCheckBox);
        panel.add(new JLabel("Type:"));
        panel.add(typeComboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Menu Item Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String description = descriptionField.getText();
            float price = Float.parseFloat(priceField.getText());
            int count = Integer.parseInt(countField.getText());
            boolean isCurrent = isCurrentCheckBox.isSelected();
            String type = (String) typeComboBox.getSelectedItem();

            MenuItem updatedItem;
            if (type.equals("Diner")) {
                updatedItem = new DinerMenuItem(title, item.getItemID(), description, price, count, isCurrent);
            } else {
                updatedItem = new PancakeMenuItem(title, item.getItemID(), description, price, count, isCurrent);
            }

            try {
                menuManager.updateMenuItem(item, updatedItem);
                cafe.DB.saveData();
                JOptionPane.showMessageDialog(null, "Menu item updated successfully!");
            } catch (CustomExceptions.ItemNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Menu item not found!");
            }
        }
    }

    private void deleteMenuItem() {
        // Implement delete menu item functionality
        JTextField itemIDField = new JTextField(10);
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Item ID:"));
        panel.add(itemIDField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Delete Menu Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String itemID = itemIDField.getText();
            MenuItem item = findMenuItemByID(itemID);
            if (item != null) {
                try {
                    menuManager.removeMenuItem(item);
                    cafe.DB.saveData();
                    JOptionPane.showMessageDialog(null, "Menu item deleted successfully!");
                } catch (CustomExceptions.ItemNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Menu item not found!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Menu item not found!");
            }
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

    private void orderFood() {
        // Implement order food as customer functionality
        new CustomerDashboard(admin);
    }
}