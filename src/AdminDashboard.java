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

        // Create a panel for the initial options
        JPanel initialOptionsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton manageCustomersButton = new JButton("Manage Customers");
        JButton manageMenuButton = new JButton("Manage Menu Items");
        JButton orderFoodButton = new JButton("Order Food as Customer");

        initialOptionsPanel.add(manageCustomersButton);
        initialOptionsPanel.add(manageMenuButton);
        initialOptionsPanel.add(orderFoodButton);

        // Add the initial options panel to the main frame
        add(initialOptionsPanel, BorderLayout.CENTER);

        // Action listeners for buttons
        manageCustomersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CustomerManagementScreen(admin);
            }
        });

        manageMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MenuManagementScreen(admin);
            }
        });

        orderFoodButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                orderFood();
            }
        });

        setVisible(true);
    }

    private void orderFood() {
        // Implement order food as customer functionality
        new CustomerDashboard(admin);
    }
}