import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;

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
        passwordLabel.setText("Password: (At least 4 letters)");
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
                        myCafe.addUser(userName, newUser);
                    } else {
                        newUser = new Customer(firstName, lastName, email, userName, password, true);
                        myCafe.addUser(userName, newUser);
                    }
                    myCafe.saveData();
                    JOptionPane.showMessageDialog(SignupScreen.this, "Your username is: " + userName);
                    new CafeOnlineOrderSystemGUI();
                    dispose();
                } 
            }
        });

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private boolean validateInput(String firstName, String lastName, String email, String password) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(SignupScreen.this, "Fill out all fields");
        }
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(SignupScreen.this, "Invalid Email");
        }
        try {
            isValidPassword(password);
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(SignupScreen.this, e.getMessage());
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

    private boolean isValidPassword(String password) throws PasswordException {
        if (password.length() < 8) {
            throw new Minimum8CharactersRequired("Password must be 8 characters long");
         } else if (!password.matches(".*\\d.*")) {
            throw new NumberCharacterMissing("Password must contain at least one number");
         } else if (!password.matches(".*[a-z].*")) {
            throw new LowerCaseCharacterMissing("Password must require a lowercase letter");
         } else if (!password.matches(".*[A-Z].*")) {
            throw new UpperCaseCharacterMissing("Password must require a uppercase letter");
         } else if (!password.matches(".*[!@#$%^&*()].*")) {
            throw new SpecialCharacterMissing("Password require one special letter (!@#$%^&*())");
         }
        return true;
    }

    private String generateUserName(String firstName) {
        String userName = "";
        String randomDigit = "";
        for (int i = 0; i < 4; i++){
            randomDigit += (int)(Math.random()*10);
        }

        userName = firstName.toLowerCase() + randomDigit;
        return userName;
    }
}