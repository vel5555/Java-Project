package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame {

    private JLabel title;
    private JLabel booksSection;
    private JLabel usersSection;
    private JButton addBookBtn, viewBooksBtn, addUserBtn, manageUsersBtn;

    public HomePage() {
        // Set frame properties
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 600); // Initial size
        setLocationRelativeTo(null); // Center the window

        // Main panel using GridBagLayout for flexibility
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        title = new JLabel("Library Management System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.BLUE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 20, 10); // Padding
        mainPanel.add(title, gbc);

        // Books Section
        booksSection = new JLabel("Books Section", SwingConstants.CENTER);
        booksSection.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(booksSection, gbc);

        // Books Section Buttons
        addBookBtn = new JButton("Add New Book");
        viewBooksBtn = new JButton("View Available Books");
        styleButton(addBookBtn, new Color(60, 130, 255));
        styleButton(viewBooksBtn, new Color(50, 205, 150));

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10); // Spacing between buttons
        mainPanel.add(addBookBtn, gbc);
        gbc.gridx = 1;
        mainPanel.add(viewBooksBtn, gbc);

        // Users Section
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        usersSection = new JLabel("Users Section", SwingConstants.CENTER);
        usersSection.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(usersSection, gbc);

        // Users Section Buttons
        addUserBtn = new JButton("Add User");
        manageUsersBtn = new JButton("Take/Return Book");
        styleButton(addUserBtn, new Color(148, 80, 255));
        styleButton(manageUsersBtn, new Color(255, 80, 80));

        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        mainPanel.add(addUserBtn, gbc);

        gbc.gridx = 1;
        mainPanel.add(manageUsersBtn, gbc);

        // Add main panel to the frame
        add(mainPanel, BorderLayout.CENTER);

        // Add action listeners to buttons
        addBookBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddBookFrame(); // Opens Add Book Form
            }
        });

        viewBooksBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewBooksFrame(); // Opens View Books Form
            }
        });

        addUserBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddUserFrame(); // Opens Add User Form
            }
        });

        manageUsersBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TakeReturnBookFrame(); // Opens Take/Return Book Form
            }
        });

        // Adjust components dynamically based on window size
        adjustComponentSizes();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustComponentSizes(); // Adjust sizes when the window is resized
            }
        });
    }

    // Adjust button sizes dynamically
    private void adjustComponentSizes() {
        int width = getWidth();
        int height = getHeight();
        // Adjust title font size based on window size
        title.setFont(new Font("Arial", Font.BOLD, width / 25));
        booksSection.setFont(new Font("Arial", Font.BOLD, width / 35));
        usersSection.setFont(new Font("Arial", Font.BOLD, width / 35));

        // Adjust button size dynamically
        Dimension buttonSize = new Dimension(width / 3, height / 10);
        addBookBtn.setPreferredSize(buttonSize);
        viewBooksBtn.setPreferredSize(buttonSize);
        addUserBtn.setPreferredSize(buttonSize);
        manageUsersBtn.setPreferredSize(buttonSize);

        revalidate(); // Re-layout the components
        repaint();    // Redraw the UI
    }

    // Method to style the buttons (color, size, font)
    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HomePage homePage = new HomePage();
            homePage.setVisible(true);
        });
    }
}
