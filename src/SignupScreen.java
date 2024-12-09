import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupScreen extends JDialog {

    private CafeOnlineOrderSystemGUI mainGUI;
    private cafe myCafe; // IMPLEMENT LATER

    JLabel firstNameLabel;
    JTextField firstNameText;
    JLabel lastNameLabel;
    JTextField lastNameText;
    JLabel emailLabel;
    JTextField emailText;
    JLabel passwordLabel;
    JPasswordField passwordField;
    JLabel roleLabel;
    JComboBox<String> roleDropDown = new JComboBox<String>(new String[]{"Customer", "Admin"});

    JButton submitButton;
    JButton cancelButton;

    public SignupScreen(CafeOnlineOrderSystemGUI mainGui, cafe myCafe) {
        this.mainGUI = mainGui;
        this.myCafe = myCafe;

        setLayout(new BorderLayout());
        setSize(600, 300);
        setLocationRelativeTo(null);

        firstNameLabel = new JLabel();
        firstNameLabel.setText("First Name:");
        firstNameText = new JTextField(""); // automatically editable

        lastNameLabel = new JLabel();
        lastNameLabel.setText("Last Name:");
        lastNameText = new JTextField("");

        emailLabel = new JLabel();
        emailLabel.setText("Email:");
        emailText = new JTextField("");

        passwordLabel = new JLabel();
        passwordLabel.setText("Password:");
        passwordField = new JPasswordField("");

        roleLabel = new JLabel();
        roleLabel.setText("Role:");

        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");

        JPanel inputPanel = new JPanel(new GridLayout(10, 1, 10, 10));
        inputPanel.setPreferredSize(new Dimension(300, 300)); // THIS DOESN'T WORK
        inputPanel.add(firstNameLabel);
        inputPanel.add(firstNameText);
        inputPanel.add(lastNameLabel);
        inputPanel.add(lastNameText);
        inputPanel.add(emailLabel);
        inputPanel.add(emailText);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);
        inputPanel.add(roleLabel);
        inputPanel.add(roleDropDown);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        // Add panels to the main dialog layout
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(new JPanel(), BorderLayout.EAST);
        add(new JPanel(), BorderLayout.WEST);

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CafeOnlineOrderSystemGUI();
                dispose(); // Close the dialog when cancel is clicked
            }
        });

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameText.getText();
                String lastName = lastNameText.getText();
                String email = emailText.getText();
                String password = new String(passwordField.getPassword());
                String role = (String) roleDropDown.getSelectedItem();

                if (validateInput(firstName, lastName, email, password)) {
                    String userName = generateUserName(firstName);
                    User newUser;
                    if (role.equals("Admin")) {
                        newUser = new Admin(firstName, lastName, email, userName, password, true);
                    } else {
                        newUser = new Customer(firstName, lastName, email, userName, password, true);
                    }
                    myCafe.addUser(userName, newUser);
                    myCafe.saveData();
                    JOptionPane.showMessageDialog(SignupScreen.this, "User registered successfully. Your username is: " + userName);
                    new CafeOnlineOrderSystemGUI();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(SignupScreen.this, "Invalid input");
                }
            }
        });

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private boolean validateInput(String firstName, String lastName, String email, String password) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return false;
        }
        if (!isValidEmail(email)) {
            return false;
        }
        if (!Utils.isValidPassword(password)) {
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private String generateUserName(String firstName) {
        String baseUserName = firstName.toLowerCase();
        String userName;
        do {
            String randomDigits = Utils.generateRandomNumber(4);
            userName = baseUserName + randomDigits;
        } while (myCafe.getUsers().containsKey(userName));
        return userName;
    }
}