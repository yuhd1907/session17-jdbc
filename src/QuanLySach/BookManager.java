package QuanLySach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookManager {
    // Cấu hình PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5432/library_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1907"; // Nhập mật khẩu của bạn

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 1. Thêm sách (kiểm tra trùng tên + tác giả)
    public void addBook(Book book) {
        String checkQuery = "SELECT COUNT(*) FROM books WHERE title = ? AND author = ?";
        String insertQuery = "INSERT INTO books (title, author, published_year, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            // Kiểm tra trùng lặp
            checkStmt.setString(1, book.getTitle());
            checkStmt.setString(2, book.getAuthor());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                System.out.println("=> Lỗi: Sách này (cùng tên và tác giả) đã tồn tại trong thư viện!");
                return;
            }

            // Tiến hành thêm mới
            insertStmt.setString(1, book.getTitle());
            insertStmt.setString(2, book.getAuthor());
            insertStmt.setInt(3, book.getPublishedYear());
            insertStmt.setDouble(4, book.getPrice());
            insertStmt.executeUpdate();
            System.out.println("=> Thêm sách thành công!");

        } catch (SQLException e) {
            System.out.println("Lỗi CSDL khi thêm sách: " + e.getMessage());
        }
    }

    // 2. Cập nhật thông tin sách
    public void updateBook(int id, Book book) {
        String updateQuery = "UPDATE books SET title = ?, author = ?, published_year = ?, price = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getPublishedYear());
            stmt.setDouble(4, book.getPrice());
            stmt.setInt(5, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("=> Cập nhật sách thành công!");
            } else {
                System.out.println("=> Lỗi: Không tìm thấy sách với ID " + id + " để cập nhật.");
            }

        } catch (SQLException e) {
            System.out.println("Lỗi CSDL khi cập nhật: " + e.getMessage());
        }
    }

    // 3. Xóa sách
    public void deleteBook(int id) {
        String deleteQuery = "DELETE FROM books WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("=> Đã xóa sách thành công!");
            } else {
                System.out.println("=> Lỗi: Không tìm thấy sách với ID " + id + " để xóa.");
            }

        } catch (SQLException e) {
            System.out.println("Lỗi CSDL khi xóa sách: " + e.getMessage());
        }
    }

    // 4. Tìm kiếm sách theo tác giả
    public void findBooksByAuthor(String author) {
        String searchQuery = "SELECT * FROM books WHERE author ILIKE ?"; // ILIKE giúp tìm không phân biệt hoa/thường

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(searchQuery)) {

            stmt.setString(1, "%" + author + "%"); // Tìm kiếm tương đối
            ResultSet rs = stmt.executeQuery();

            boolean hasData = false;
            System.out.println("\n--- KẾT QUẢ TÌM KIẾM ---");
            while (rs.next()) {
                hasData = true;
                printBookRow(rs);
            }
            if (!hasData) {
                System.out.println("Không tìm thấy cuốn sách nào của tác giả: " + author);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi CSDL khi tìm kiếm: " + e.getMessage());
        }
    }

    // 5. Hiển thị tất cả sách
    public void listAllBooks() {
        String query = "SELECT * FROM books ORDER BY id ASC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n--- DANH SÁCH TOÀN BỘ SÁCH ---");
            System.out.printf("%-5s | %-30s | %-20s | %-10s | %-10s\n", "ID", "Tên sách", "Tác giả", "Năm XB", "Giá");
            System.out.println("-------------------------------------------------------------------------------------");
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                printBookRow(rs);
            }
            if (!hasData) {
                System.out.println("Thư viện hiện đang trống.");
            }

        } catch (SQLException e) {
            System.out.println("Lỗi CSDL khi lấy danh sách: " + e.getMessage());
        }
    }

    // Hàm phụ trợ in ra 1 dòng sách
    private void printBookRow(ResultSet rs) throws SQLException {
        System.out.printf("%-5d | %-30s | %-20s | %-10d | %-10.2f\n",
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getInt("published_year"),
                rs.getDouble("price"));
    }
}
