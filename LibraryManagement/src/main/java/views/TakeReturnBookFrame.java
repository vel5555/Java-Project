package views;

import dbconnect.dbconnect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TakeReturnBookFrame extends JFrame {

    private JTextField bookIDField, emailField;
    private JPasswordField passwordField;
    private JRadioButton takeBook, returnBook;
    private JButton submitButton;

    public TakeReturnBookFrame() {
        setTitle("Take or Return Book");
        setSize(500, 400); // Adjusted size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        // Labels and fields
        JLabel bookIDLabel = new JLabel("Book ID:");
        bookIDField = new JTextField(15);

        JLabel emailLabel = new JLabel("Email (User):");
        emailField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);

        takeBook = new JRadioButton("Take Book");
        returnBook = new JRadioButton("Return Book");

        ButtonGroup group = new ButtonGroup();
        group.add(takeBook);
        group.add(returnBook);

        // Arrange components
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(bookIDLabel, gbc);

        gbc.gridx = 1;
        add(bookIDField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(emailLabel, gbc);

        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(takeBook, gbc);

        gbc.gridx = 1;
        add(returnBook, gbc);

        // Submit button
        submitButton = new JButton("Submit");
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(submitButton, gbc);

        // Add action listener for submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleTransaction();
            }
        });

        setVisible(true);
    }

    // Method to handle transaction logic
    private void handleTransaction() {
        String bookID = bookIDField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String transactionType = takeBook.isSelected() ? "take" : "return";

        if (bookID.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out!");
            return;
        }

        int userID = getUserIDByEmailAndPassword(email, password);
        if (userID == -1) {
            JOptionPane.showMessageDialog(this, "Invalid email or password!");
            return;
        }

        // Perform the transaction (Take or Return Book)
        performTransaction(bookID, userID, transactionType);
    }

    // Method to get user ID by Gmail and password
    private int getUserIDByEmailAndPassword(String email, String password) {
        String sql = "SELECT user_id FROM Users WHERE email = ? AND password = ?";

        try (Connection conn = dbconnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching user ID: " + e.getMessage());
        }

        return -1; // Return -1 if user not found
    }

// Method to perform the transaction
private void performTransaction(String bookID, int userID, String transactionType) {
    String transactionSql = "INSERT INTO Transactions (book_id, user_id, transaction_type, date) VALUES (?, ?, ?, CURRENT_DATE)";
    String checkBookSql = "SELECT status FROM Books WHERE book_id = ?";
    String updateBookStatusSql;

    // Set the correct status based on the transaction type
    if (transactionType.equals("take")) {
        updateBookStatusSql = "UPDATE Books SET status = 'unavailable' WHERE book_id = ? AND status = 'available'";
    } else {
        updateBookStatusSql = "UPDATE Books SET status = 'available' WHERE book_id = ? AND status = 'unavailable'";
    }

    try (Connection conn = dbconnect.connect();
         PreparedStatement checkBookPstmt = conn.prepareStatement(checkBookSql);
         PreparedStatement transactionPstmt = conn.prepareStatement(transactionSql);
         PreparedStatement updateBookStatusPstmt = conn.prepareStatement(updateBookStatusSql)) {

        // Check if the book exists in the library
        checkBookPstmt.setInt(1, Integer.parseInt(bookID));
        ResultSet rs = checkBookPstmt.executeQuery();

        if (!rs.next()) {
            // If no result, the book doesn't exist in the library
            JOptionPane.showMessageDialog(this, "Transaction failed: Book does not exist in the library.");
            return;
        }

        // Check the current status of the book
        String currentStatus = rs.getString("status");

        // Validation for book status based on transaction type
        if (transactionType.equals("take") && currentStatus.equals("unavailable")) {
            JOptionPane.showMessageDialog(this, "Transaction failed: Book is already taken.");
            return;
        } else if (transactionType.equals("return") && currentStatus.equals("available")) {
            JOptionPane.showMessageDialog(this, "Transaction failed: Book is already available.");
            return;
        }

        // Begin transaction
        conn.setAutoCommit(false);

        // Insert into Transactions table
        transactionPstmt.setInt(1, Integer.parseInt(bookID));
        transactionPstmt.setInt(2, userID);
        transactionPstmt.setString(3, transactionType);
        transactionPstmt.executeUpdate();

        // Update the book status
        updateBookStatusPstmt.setInt(1, Integer.parseInt(bookID));
        int rowsAffected = updateBookStatusPstmt.executeUpdate();

        // Check if the status was updated
        if (rowsAffected > 0) {
            conn.commit(); // Commit both actions
            JOptionPane.showMessageDialog(this, "Transaction successful!");
        // Clear the form fields
        bookIDField.setText("");
        emailField.setText("");
        passwordField.setText("");
        takeBook.setSelected(false);
        returnBook.setSelected(false);
        } else {
            conn.rollback(); // Rollback if book status wasn't updated
            JOptionPane.showMessageDialog(this, "Transaction failed: Unable to update book status.");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error performing transaction: " + e.getMessage());
    }
}

}
