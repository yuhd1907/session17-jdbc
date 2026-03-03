package ToDoList;

import java.sql.*;

public class TaskManagement {
    // Thông tin kết nối PostgreSQL (đổi DB, user, password tương ứng)
    private static final String URL = "jdbc:postgresql://localhost:5432/todo_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1907"; // Nhập mật khẩu của bạn

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void addTask(String taskName, String status) {
        // Dùng CALL không có ngoặc nhọn để PostgreSQL nhận đúng là Procedure
        String query = "CALL add_task(?, ?)";
        try (Connection conn = getConnection();
             CallableStatement stmt = conn.prepareCall(query)) {
            stmt.setString(1, taskName);
            stmt.setString(2, status);
            stmt.execute();
            System.out.println("=> Đã thêm công việc thành công!");
        } catch (SQLException e) {
            System.out.println("Lỗi CSDL: " + e.getMessage());
        }
    }

    public void listTasks() {
        // Dùng {call ...} cho Function trả về bảng
        String query = "{call list_tasks()}";
        try (Connection conn = getConnection();
             CallableStatement stmt = conn.prepareCall(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n--- DANH SÁCH CÔNG VIỆC ---");
            System.out.printf("%-5s | %-30s | %-20s\n", "ID", "Tên công việc", "Trạng thái");
            System.out.println("--------------------------------------------------------------");
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.printf("%-5d | %-30s | %-20s\n", rs.getInt("id"), rs.getString("task_name"), rs.getString("status"));
            }
            if (!hasData) System.out.println("Chưa có công việc nào trong danh sách.");
        } catch (SQLException e) {
            System.out.println("Lỗi CSDL: " + e.getMessage());
        }
    }

    public void updateTaskStatus(int taskId, String status) {
        String query = "CALL update_task_status(?, ?)";
        try (Connection conn = getConnection();
             CallableStatement stmt = conn.prepareCall(query)) {
            stmt.setInt(1, taskId);
            stmt.setString(2, status);
            stmt.execute();
            System.out.println("=> Lệnh cập nhật trạng thái đã được thực thi (nếu ID tồn tại)!");
        } catch (SQLException e) {
            System.out.println("Lỗi CSDL: " + e.getMessage());
        }
    }

    public void deleteTask(int taskId) {
        String query = "CALL delete_task(?)";
        try (Connection conn = getConnection();
             CallableStatement stmt = conn.prepareCall(query)) {
            stmt.setInt(1, taskId);
            stmt.execute();
            System.out.println("=> Lệnh xóa công việc đã được thực thi (nếu ID tồn tại)!");
        } catch (SQLException e) {
            System.out.println("Lỗi CSDL: " + e.getMessage());
        }
    }

    public void searchTaskByName(String taskName) {
        String query = "{call search_task_by_name(?)}";
        try (Connection conn = getConnection();
             CallableStatement stmt = conn.prepareCall(query)) {
            stmt.setString(1, taskName);
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("\n--- KẾT QUẢ TÌM KIẾM ---");
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("ID: %d | Tên: %s | Trạng thái: %s\n", rs.getInt("id"), rs.getString("task_name"), rs.getString("status"));
                }
                if (!hasData) System.out.println("Không tìm thấy công việc nào phù hợp.");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi CSDL: " + e.getMessage());
        }
    }

    public void taskStatistics() {
        String query = "{call task_statistics()}";
        try (Connection conn = getConnection();
             CallableStatement stmt = conn.prepareCall(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n--- THỐNG KÊ CÔNG VIỆC ---");
            while (rs.next()) {
                System.out.println("- " + rs.getString("status") + ": " + rs.getInt("total") + " công việc");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi CSDL: " + e.getMessage());
        }
    }
}
