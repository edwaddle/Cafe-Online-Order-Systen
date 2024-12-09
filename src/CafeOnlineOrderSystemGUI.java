import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class CafeOnlineOrderSystemGUI extends JFrame {

    JLabel title;
    JButton loginButton;
    JButton exitButton;
    JLabel signUpLabel;
    JButton signUpButton;

    public CafeOnlineOrderSystemGUI() {

        // Set up the frame
        setTitle("Pancake Diner Co.");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        try {
            // Load the image from resources
            Image im = ImageIO.read(getClass().getResource("resources/backdrop1.jpg")); // fine shyt don't work
            ImagePanel image = new ImagePanel(im);
            image.setLayout(new BorderLayout());

            title = new JLabel();
            title.setText("Welcome to Cake Munchers Co.!");
            title.setFont(new Font("Serif", Font.PLAIN, 30));

            loginButton = new JButton("Login");
            exitButton = new JButton("Exit");

            signUpLabel = new JLabel();
            signUpLabel.setText("Not a User? Click here to sign up");
            signUpLabel.setFont(new Font("Serif", Font.PLAIN, 14));
            signUpButton = new JButton("Sign Up");

            JPanel titlePanel = new JPanel(new GridBagLayout());
            titlePanel.add(title);
            titlePanel.setBorder(new EmptyBorder(50, 50, 50, 50));
            titlePanel.setBackground(new Color(213, 134, 145, 0));
            image.add(titlePanel, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel();
            JPanel innerButtonPanel = new JPanel(new GridLayout(2, 1, 20, 0));
            innerButtonPanel.setBackground(new Color(213, 134, 145, 0));// turns background transparent
            innerButtonPanel.setPreferredSize(new Dimension(200, 100));
            innerButtonPanel.add(loginButton);
            innerButtonPanel.add(exitButton);
            buttonPanel.add(innerButtonPanel);
            buttonPanel.setBackground(new Color(213, 134, 145, 0));
            image.add(buttonPanel, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel(new GridBagLayout());
            JPanel innerBottomPanel = new JPanel(new GridLayout(2, 1));
            innerBottomPanel.setPreferredSize(new Dimension(200, 180));
            innerBottomPanel.setBackground(new Color(213, 134, 145, 0));// turns background transparent
            innerBottomPanel.add(signUpLabel);
            innerBottomPanel.add(signUpButton);
            innerBottomPanel.setBorder(new EmptyBorder(30, 0, 50, 0));
            bottomPanel.add(innerBottomPanel);
            bottomPanel.setBackground(new Color(213, 134, 145, 0));// turns background transparent
            image.add(bottomPanel, BorderLayout.SOUTH);

            add(image, BorderLayout.CENTER);

        } catch (IOException e) {
            e.printStackTrace();
        }

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SignupScreen(CafeOnlineOrderSystemGUI.this, cafe.DB);
                dispose();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoginScreen(CafeOnlineOrderSystemGUI.this, cafe.DB.getUsers());
                dispose();
            }
        });

        setVisible(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    class ImagePanel extends JComponent {
        private Image image;

        public ImagePanel(Image image) {
            this.image = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        Map<String, User> myMap = new HashMap<>();
        myMap.put("asdf", new Admin("asdf", "asdf", "asdf@g", "asdf1337", "asdf", true));
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        new CafeOnlineOrderSystemGUI();
    }
}