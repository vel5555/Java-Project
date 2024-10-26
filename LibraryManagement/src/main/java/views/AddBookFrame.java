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

public class AddBookFrame extends JFrame {
    private JTextField titleField, authorField, isbnField, yearField;
    private JButton submitButton, cancelButton;

    public AddBookFrame() {
        setTitle("Add New Book");
        setSize(700, 500); // Adjust as needed
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
        
        // Set layout for the frame
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // Title Label and Field
        JLabel titleLabel = new JLabel("Book Title:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(titleLabel, gbc);

        titleField = new JTextField(15); // Smaller input field
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(titleField, gbc);

        // Author Label and Field
        JLabel authorLabel = new JLabel("Author:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        add(authorLabel, gbc);

        authorField = new JTextField(15);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(authorField, gbc);

        // ISBN Label and Field
        JLabel isbnLabel = new JLabel("ISBN:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        add(isbnLabel, gbc);

        isbnField = new JTextField(15);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(isbnField, gbc);

        // Published Year Label and Field
        JLabel yearLabel = new JLabel("Published Year:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        add(yearLabel, gbc);

        yearField = new JTextField(15);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(yearField, gbc);

        // Submit Button
        submitButton = new JButton("Add Book");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        add(submitButton, gbc);

        // Cancel Button
        cancelButton = new JButton("Cancel");
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.NONE;
        add(cancelButton, gbc);

        // Action listeners
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBookToDatabase();
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
        titleField.setPreferredSize(new Dimension(width / 3, height / 15));
        authorField.setPreferredSize(new Dimension(width / 3, height / 15));
        isbnField.setPreferredSize(new Dimension(width / 3, height / 15));
        yearField.setPreferredSize(new Dimension(width / 3, height / 15));

        revalidate();
        repaint();
    }

    // Method to add book data to the database
    private void addBookToDatabase() {
        String title = titleField.getText();
        String author = authorField.getText();
        String isbn = isbnField.getText();
        String publishedYear = yearField.getText();

        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || publishedYear.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        Connection conn = dbconnect.connect();
        if (conn != null) {
            String sql = "INSERT INTO Books (title, author, isbn, published_year) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, title);
                pstmt.setString(2, author);
                pstmt.setString(3, isbn);
                pstmt.setString(4, publishedYear);

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Book added successfully!");
                    // Clear the form fields after successful insertion
                    titleField.setText("");
                    authorField.setText("");
                    isbnField.setText("");
                    yearField.setText("");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error adding book: " + ex.getMessage());
            }
        }
    }
}
