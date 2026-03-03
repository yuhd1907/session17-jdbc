package QuanLyPhim;

import java.sql.*;

public class MovieManagement {
    // Thông tin kết nối PostgreSQL (Thay đổi tên DB, user và password cho phù hợp)
    private static final String URL = "jdbc:postgresql://localhost:5432/movie_manager";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1907"; // Nhập mật khẩu pgAdmin của bạn

    // Phương thức kết nối CSDL
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void addMovie(String title, String director, int year) {
        String query = "CALL add_movie(?, ?, ?)";
        try (Connection conn = getConnection();
             CallableStatement stmt = conn.prepareCall(query)) {

            stmt.setString(1, title);
            stmt.setString(2, director);
            stmt.setInt(3, year);
            stmt.execute(); // Dùng execute() cho Procedure không trả về bảng
            System.out.println("=> Thêm phim thành công!");

        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm phim: " + e.getMessage());
        }
    }

    public void listMovies() {
        // Gọi function trả về bảng trong PostgreSQL
        String query = "{call list_movies()}";
        try (Connection conn = getConnection();
             CallableStatement stmt = conn.prepareCall(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n--- DANH SÁCH PHIM ---");
            System.out.println(String.format("%-5s | %-30s | %-20s | %-5s", "ID", "Tiêu đề", "Đạo diễn", "Năm"));
            System.out.println("-----------------------------------------------------------------------");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.println(String.format("%-5d | %-30s | %-20s | %-5d",
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("director"),
                        rs.getInt("year")));
            }
            if (!hasData) {
                System.out.println("Danh sách phim đang trống.");
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách phim: " + e.getMessage());
        }
    }

    public void updateMovie(int id, String title, String director, int year) {
        String query = "CALL update_movie(?, ?, ?, ?)";
        try (Connection conn = getConnection();
             CallableStatement stmt = conn.prepareCall(query)) {

            stmt.setInt(1, id);
            stmt.setString(2, title);
            stmt.setString(3, director);
            stmt.setInt(4, year);
            stmt.execute();
            // Lưu ý: PostgreSQL JDBC gọi Procedure bằng execute() thường không trả về số dòng bị ảnh hưởng (executeUpdate) như MySQL.
            // Để đơn giản bài tập, ta thông báo thành công luôn nếu không văng Exception.
            System.out.println("=> Đã chạy lệnh cập nhật phim (nếu ID tồn tại)!");

        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật phim: " + e.getMessage());
        }
    }

    public void deleteMovie(int id) {
        String query = "CALL delete_movie(?)";
        try (Connection conn = getConnection();
             CallableStatement stmt = conn.prepareCall(query)) {

            stmt.setInt(1, id);
            stmt.execute();
            System.out.println("=> Đã chạy lệnh xóa phim (nếu ID tồn tại)!");

        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa phim: " + e.getMessage());
        }
    }
}
