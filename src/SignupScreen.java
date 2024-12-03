import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;


public class SignupScreen extends JDialog {

	private CafeOnlineOrderSystemGUI mainGUI;
    private cafe mycafe; //IMPLEMENT LATER

	JLabel firstNameLabel;
	JTextField firstNameText;
	JLabel lastNameLabel;
	JTextField lastNameText;
	JLabel emailLabel;
	JTextField emailText;
	JLabel passwordLabel;
	JPasswordField passwordField;
	JLabel roleLabel;
	JComboBox<String> roleDropDown = new JComboBox<String>(new String[] {"Customer", "Admin"});

	JButton submitButton;
	JButton cancelButton;


   		// xxx your codes
	public SignupScreen(CafeOnlineOrderSystemGUI mainGui){
		this.mainGUI = mainGui;

        setLayout(new BorderLayout());
        setSize(600, 300);
        
        firstNameLabel = new JLabel();
        firstNameLabel.setText("First Name:");
        firstNameText = new JTextField(""); //automatically editable

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

        submitButton = new JButton("Login");
        cancelButton = new JButton("Cancel");

        JPanel inputPanel = new JPanel(new GridLayout(10, 1, 10, 10));
		inputPanel.setPreferredSize(new Dimension(300, 300)); //THIS DOESN"T WORK
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
                dispose(); // Close the dialog when cancel is clicked
            }
        });

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true); 
        

    }
	
}
