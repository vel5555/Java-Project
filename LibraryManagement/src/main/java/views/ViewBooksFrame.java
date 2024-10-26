package views;

import dbconnect.dbconnect;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ViewBooksFrame extends JFrame {

    private JTable booksTable;
    private DefaultTableModel tableModel;

    public ViewBooksFrame() {
        setTitle("View Books");
        setSize(700, 500); // Adjust the window size as needed
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
        
        // Set layout
        setLayout(new BorderLayout());

        // Table model to hold book data
        String[] columns = {"ID", "Title", "Author", "ISBN", "Published Year"};
        tableModel = new DefaultTableModel(columns, 0);
        booksTable = new JTable(tableModel);
        booksTable.setRowHeight(30); // Increase row height
        booksTable.setFont(new Font("Arial", Font.PLAIN, 16)); // Set custom font
        booksTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16)); // Custom header font
        
        // Adding table to scroll pane for better UI
        JScrollPane scrollPane = new JScrollPane(booksTable);
        add(scrollPane, BorderLayout.CENTER);

        // Fetch book data from database
        loadBooksFromDatabase();

        // Display the frame
        setVisible(true);
    }

    // Method to load book data from the database
    private void loadBooksFromDatabase() {
        Connection conn = dbconnect.connect();
        if (conn != null) {
            String query = "SELECT book_id, title, author, isbn, published_year FROM Books where status='available'";

            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    // Get data from the current row
                    int id = rs.getInt("book_id");
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    String isbn = rs.getString("isbn");
                    String publishedYear = rs.getString("published_year");

                    // Add the row to the table model
                    Object[] row = {id, title, author, isbn, publishedYear};
                    tableModel.addRow(row);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching book data.");
            }
        }
    }
}
