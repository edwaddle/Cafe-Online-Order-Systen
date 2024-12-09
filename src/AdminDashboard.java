import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        // Action listeners for buttons
        customerManagerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CustomerManagementScreen(admin);
                dispose();
            }
        });

        menuManagerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MenuManagementScreen(admin);
                dispose();
            }
        });

        customerLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                orderFood();
                dispose();
            }
        });

        setVisible(true);
    }

    private void orderFood() {
        // Implement order food as customer functionality
        new CustomerDashboard(admin);
    }
}