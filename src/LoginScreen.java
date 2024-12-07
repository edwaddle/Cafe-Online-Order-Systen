import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class LoginScreen extends JDialog {

    private Map<String, User> users;
    static JLabel userLabel;
    static JTextField userText;
    static JLabel passwordLabel;
    static JPasswordField passwordText;
    static JButton loginButton;
    static JButton cancelButton;

    public LoginScreen(JFrame parent, Map<String, User> users) {
        super(parent, "Login", true);

        this.users = users;
        UserManager userManager = new UserManager();

        setLayout(new BorderLayout());
        setSize(600, 300);
        setLocationRelativeTo(parent);

        userLabel = new JLabel();
        userLabel.setText("Username:");
        userText = new JTextField(""); // automatically editable

        passwordLabel = new JLabel();
        passwordLabel.setText("Password:");
        passwordText = new JPasswordField("");

        loginButton = new JButton("Login");
        cancelButton = new JButton("Cancel");

        // Panel for username and password
        JPanel inputPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        inputPanel.add(userLabel);
        inputPanel.add(userText);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordText);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        // Add panels to the main dialog layout
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(new JPanel(), BorderLayout.EAST);
        add(new JPanel(), BorderLayout.WEST);

        // Button Stuff
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());
                User user = users.get(username);
                if (user != null && user.getPassword().equals(password)) {
                    if (user.isActive()) {
                        if (user.getRole().equals("Admin")) {
                            new AdminDashboard(parent, user);
                        } else {
                            new CustomerDashboard(user);
                        }
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(LoginScreen.this, "User is inactive");
                    }
                } else {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Invalid username or password");
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CafeOnlineOrderSystemGUI();
                dispose(); // Close the dialog when cancel is clicked
            }
        });

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}