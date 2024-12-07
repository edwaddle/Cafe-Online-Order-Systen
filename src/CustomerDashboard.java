import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerDashboard extends JFrame {

    private UserManager userManager;
    private User currentUser; // The user who is currently logged in

    private JTextPane cartPane;
    private JTextPane billPane;
    private JTextPane menuPane;
    private StyledDocument cartDoc;
    private StyledDocument billDoc;
    private StyledDocument menuDoc;

    private JCheckBox breakfastCheckbox;
    private JCheckBox dinnerCheckbox;

    private JPanel tipPanel;
    private ButtonGroup tipGroup;
    private JRadioButton noTipButton;
    private JRadioButton tenPercentButton;
    private JRadioButton fifteenPercentButton;
    private JRadioButton twentyPercentButton;

    public CustomerDashboard(User currentUser) {
        this.currentUser = currentUser;
        this.userManager = new UserManager();

        setTitle("Customer Dashboard");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize components
        cartPane = new JTextPane();
        billPane = new JTextPane();
        menuPane = new JTextPane();
        cartDoc = cartPane.getStyledDocument();
        billDoc = billPane.getStyledDocument();
        menuDoc = menuPane.getStyledDocument();

        breakfastCheckbox = new JCheckBox("Breakfast");
        dinnerCheckbox = new JCheckBox("Dinner");

        tipPanel = new JPanel();
        tipGroup = new ButtonGroup();
        noTipButton = new JRadioButton("No Tip");
        tenPercentButton = new JRadioButton("10% Tip");
        fifteenPercentButton = new JRadioButton("15% Tip");
        twentyPercentButton = new JRadioButton("20% Tip");

        tipGroup.add(noTipButton);
        tipGroup.add(tenPercentButton);
        tipGroup.add(fifteenPercentButton);
        tipGroup.add(twentyPercentButton);

        tipPanel.add(noTipButton);
        tipPanel.add(tenPercentButton);
        tipPanel.add(fifteenPercentButton);
        tipPanel.add(twentyPercentButton);

        // Add components to the frame
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel filterPanel = new JPanel();
        filterPanel.add(breakfastCheckbox);
        filterPanel.add(dinnerCheckbox);

        mainPanel.add(filterPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(menuPane), BorderLayout.CENTER);
        mainPanel.add(new JScrollPane(cartPane), BorderLayout.EAST);
        mainPanel.add(new JScrollPane(billPane), BorderLayout.SOUTH);
        mainPanel.add(tipPanel, BorderLayout.WEST);

        add(mainPanel);

        // Load menu items
        loadMenuItems();

        // Action listeners
        breakfastCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadMenuItems();
            }
        });

        dinnerCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadMenuItems();
            }
        });

        setVisible(true);
    }

    private void loadMenuItems() {
        try {
            menuDoc.remove(0, menuDoc.getLength());
            for (MenuItem item : cafe.DB.getMenu()) {
                if ((breakfastCheckbox.isSelected() && item instanceof PancakeMenuItem) ||
                    (dinnerCheckbox.isSelected() && item instanceof DinerMenuItem)) {
                    menuDoc.insertString(menuDoc.getLength(), item.getTitle() + "\n", null);
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}