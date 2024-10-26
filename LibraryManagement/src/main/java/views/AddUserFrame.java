package views;

import dbconnect.dbconnect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddUserFrame extends JFrame {
    
    private JTextField userNameField, emailField, passwordField;
    private JButton submitButton, cancelButton;
    private   JLabel userNameLabel = new JLabel("Name:");
    private   JLabel emailLabel = new JLabel("Email :");
    private   JLabel passwordLabel = new JLabel("Password:");

    
    public AddUserFrame() {
        setTitle("Add New User");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500); // Adjust the size as per your need
        setLocationRelativeTo(null); // Center the frame

        // Set the layout manager
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding for elements

        // User Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(userNameLabel, gbc);
        
        userNameField = new JTextField(15); // Smaller text field
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(userNameField, gbc);

        // Email (Gmail)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        add(emailLabel, gbc);
        
        emailField = new JTextField(15);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        add(passwordLabel, gbc);
        
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(passwordField, gbc);

        // Submit Button
        submitButton = new JButton("Submit");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(submitButton, gbc);

        // Cancel Button
        cancelButton = new JButton("Cancel");
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        add(cancelButton, gbc);

        // Action Listeners
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = userNameField.getText();
                String email = emailField.getText();
                String password = passwordField.getText();
                
                if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(AddUserFrame.this, "All fields must be filled out!");
                } else {
                    // Add user to the database
                    addUserToDatabase(userName, email, password);
                    
                    // Clear the form
                    userNameField.setText("");
                    emailField.setText("");
                    passwordField.setText("");

                    JOptionPane.showMessageDialog(AddUserFrame.this, "User added successfully!");
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the frame
            }
        });
        adjustComponentSizes();
        setVisible(true);
                // Add ComponentListener for resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustComponentSizes();
            }
        });
    }

    private void adjustComponentSizes() {
        int width = getWidth();
        int height = getHeight();

        // Adjust field sizes dynamically
        userNameField.setPreferredSize(new Dimension(width / 2, height / 20));
        emailField.setPreferredSize(new Dimension(width / 2, height / 20));
        passwordField.setPreferredSize(new Dimension(width / 2, height / 20));

        // Adjust label font sizes dynamically based on frame width
        int fontSize = Math.max(15, width / 40); // Font size scaling
        Font newFont = new Font("Arial", Font.BOLD, fontSize);

        userNameLabel.setFont(newFont);
        emailLabel.setFont(newFont);
        passwordLabel.setFont(newFont);
        revalidate();
        repaint();
    }
    // Method to insert user data into the database
    private void addUserToDatabase(String userName, String email, String password) {
        String sql = "INSERT INTO users (username, email ,password) VALUES (?, ?, ?)";

        try (Connection conn = dbconnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userName);
            pstmt.setString(2, email);
            pstmt.setString(3, password);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding user to the database: " + e.getMessage());
        }
    }
}
