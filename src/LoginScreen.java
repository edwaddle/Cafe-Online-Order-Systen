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
    static JTextField passwordText;
    static JButton loginButton;
    static JButton cancelButton;


    public LoginScreen(JFrame parent, Map<String, User> users) { 
        super(parent, "Login", true);

        UserManager userManager = new UserManager();

        setLayout(new BorderLayout());
        setSize(600, 300);
        setLocationRelativeTo(parent);
        
        userLabel = new JLabel();
        userLabel.setText("Username:");
        userText = new JTextField(""); //automatically editable

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

        //Button Stuff
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                for (User currUser: users.values()){
                    if (currUser.getUserName() == userText.getText() && currUser.getPassword().equals(passwordText.getText())){
                        System.out.println();
                    }
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the dialog when cancel is clicked
            }
        });

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true); 
        

    }


	// xxx your codes
}
