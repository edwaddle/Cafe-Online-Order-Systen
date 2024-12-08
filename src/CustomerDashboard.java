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
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        //left side
        JPanel leftPanel = new JPanel(new BorderLayout(10,10));
        JPanel innerLeftPanel = new JPanel();
        innerLeftPanel.setLayout(new BoxLayout(innerLeftPanel, BoxLayout.Y_AXIS));

        JPanel upperInnerLeftPanel = new JPanel();
        upperInnerLeftPanel.setPreferredSize(new Dimension(380, 30));
        upperInnerLeftPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JCheckBox bOrLCheckBox = new JCheckBox("Breakfast/Lunch");
        JCheckBox dinnerCheckbox = new JCheckBox("Dinner");

        upperInnerLeftPanel.add(bOrLCheckBox);
        upperInnerLeftPanel.add(dinnerCheckbox);
        innerLeftPanel.add(upperInnerLeftPanel);

        

        cartPane = new JTextPane();
        cartPane.setPreferredSize(new Dimension(380, 200));
        cartDoc = cartPane.getStyledDocument();
        String orderedItems = "";
        for (String item: currentUser.getOrderedItems()){
            orderedItems += item;
            for (MenuItem itemName : cafe.DB.getMenu()){
                if (itemName.getTitle().equals( item)){
                    orderedItems += " " + itemName.getPrice();
                }
            }
        }
        cartPane.setText(orderedItems);
        JLabel cartLabel = new JLabel("Cart:");
        cartLabel.setFont(new Font("Serif", Font.PLAIN, 30));
        innerLeftPanel.add(cartLabel);
        innerLeftPanel.add(cartPane);

        billPane = new JTextPane();
        billPane.setPreferredSize(new Dimension(380, 200));
        billDoc = cartPane.getStyledDocument();
        String billItems = "";
        double billCost = 0.0;
        for (String item: currentUser.getOrderedItems()){
            billItems += item;
            for (MenuItem itemName : cafe.DB.getMenu()){
                if (itemName.getTitle().equals( item)){
                    billItems += " " + itemName.getPrice() + "\n";
                    billCost += itemName.getPrice();
                }
            }
        }
        if (billItems.length() != 0){
            billItems += ("Subtotal: " + billCost + "\n" +"Tax: " + billCost*(0.0725) + "\n"  + 
            "Tip: " + "\n Total: " + billCost*(1.0725)
            ); //IMPLEMENT TIP
            
        }
        billPane.setText(billItems);
        JLabel billLabel = new JLabel("Bill:");
        billLabel.setFont(new Font("Serif", Font.PLAIN, 30));
        innerLeftPanel.add(billLabel);
        innerLeftPanel.add(billPane);

        ButtonGroup tipGroup = new ButtonGroup();
        JRadioButton noTipButton = new JRadioButton("No tip");
        noTipButton.setSelected(true); 
        JRadioButton fifteenTipButton = new JRadioButton("15% tip");
        JRadioButton eighteenTipButton = new JRadioButton("18% tip");
        JRadioButton twentyTipButton = new JRadioButton("20% tip");

        tipGroup.add(noTipButton);
        tipGroup.add(fifteenTipButton);
        tipGroup.add(eighteenTipButton);
        tipGroup.add(twentyTipButton);
        JPanel tipPanel = new JPanel();
        tipPanel.add(noTipButton);
        tipPanel.add(fifteenTipButton);
        tipPanel.add(eighteenTipButton);
        tipPanel.add(twentyTipButton);
        innerLeftPanel.add(tipPanel);


        leftPanel.add(innerLeftPanel);


        //Right Panel
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JPanel upperRightPanel = new JPanel();
        upperRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        upperRightPanel.setPreferredSize(new Dimension(380, 30));
        JLabel userLabel = new JLabel();
        JButton logoutButton = new JButton("Logout");
        userLabel.setText(currentUser.getFirstName() + " " + currentUser.getLastName() + " - " + currentUser.getUserName());
        upperRightPanel.add(userLabel);
        upperRightPanel.add(logoutButton);
        rightPanel.add(upperRightPanel);

        menuPane = new JTextPane();
        menuPane.setPreferredSize(new Dimension(380, 500));
        menuDoc = cartPane.getStyledDocument();
        String menuItems = "";
        double menuCost = 0.0;
        for (String item: currentUser.getOrderedItems()){
            menuItems += item;
            for (MenuItem itemName : cafe.DB.getMenu()){
                if (itemName.getTitle().equals( item)){
                    menuItems += " " + itemName.getPrice() + "\n";
                    menuCost += itemName.getPrice();
                }
            }
        }
        if (menuItems.length() != 0){
            menuItems += ("Subtotal: " + menuCost + "\n" +"Tax: " + menuCost*(0.0725) + "\n"  + 
            "Tip: " + "\n Total: " + menuCost*(1.0725)
            ); //IMPLEMENT TIP
            
        }
        menuPane.setText(menuItems);
        JLabel menuLabel = new JLabel("Menu:");
        menuLabel.setFont(new Font("Serif", Font.PLAIN, 30));
        rightPanel.add(menuLabel);
        rightPanel.add(menuPane);

        //Bottom

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel leftBottomPanel = new JPanel();
        JButton cancelButton = new JButton("Cancel");
        JButton orderButton = new JButton("Order");
        leftBottomPanel.add(cancelButton);
        leftBottomPanel.add(orderButton);
        bottomPanel.add(leftBottomPanel, BorderLayout.WEST);

        JPanel rightBottomPanel = new JPanel();
        JButton addToCartButton = new JButton("Add to Cart");
        rightBottomPanel.add(addToCartButton);
        bottomPanel.add(rightBottomPanel, BorderLayout.EAST);

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


        add(bottomPanel, BorderLayout.SOUTH);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);


/* 

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
            */
        setVisible(true);
    }
        
}