package QuanLyDonHang;

import java.sql.*;

public class OrderManager {
    // Thông tin kết nối PostgreSQL (Thay đổi password cho phù hợp)
    private static final String URL = "jdbc:postgresql://localhost:5432/shop_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "your_password";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // --- CHỨC NĂNG SẢN PHẨM ---
    public void addProduct(Product product) {
        String checkQuery = "SELECT COUNT(*) FROM products WHERE name = ?";
        String insertQuery = "INSERT INTO products (name, price) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            checkStmt.setString(1, product.getName());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                System.out.println("Lỗi: Sản phẩm '" + product.getName() + "' đã tồn tại!");
                return;
            }

            insertStmt.setString(1, product.getName());
            insertStmt.setDouble(2, product.getPrice());
            insertStmt.executeUpdate();
            System.out.println("=> Đã thêm sản phẩm thành công!");

        } catch (SQLException e) {
            System.out.println("Lỗi CSDL: " + e.getMessage());
        }
    }

    // Hàm phụ trợ để Main gọi khi tính tiền đơn hàng
    public double getProductPrice(int productId) {
        String query = "SELECT price FROM products WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble("price");
        } catch (SQLException e) {
            System.out.println("Lỗi CSDL: " + e.getMessage());
        }
        return -1;
    }

    // --- CHỨC NĂNG KHÁCH HÀNG ---
    // Thêm khách hàng (Mở rộng thêm để tạo Data test)
    public void addCustomer(Customer customer) {
        String query = "INSERT INTO customers (name, email) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.executeUpdate();
            System.out.println("=> Đã thêm khách hàng thành công!");
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // Mã lỗi UNIQUE VIOLATION của PostgreSQL
                System.out.println("Lỗi: Email này đã được sử dụng!");
            } else {
                System.out.println("Lỗi CSDL: " + e.getMessage());
            }
        }
    }

    public void updateCustomer(int customerId, Customer customer) {
        String query = "UPDATE customers SET name = ?, email = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setInt(3, customerId);

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("=> Cập nhật khách hàng thành công!");
            else System.out.println("=> Không tìm thấy khách hàng với ID: " + customerId);

        } catch (SQLException e) {
            System.out.println("Lỗi CSDL (có thể do trùng email): " + e.getMessage());
        }
    }

    // --- CHỨC NĂNG ĐƠN HÀNG ---
    public void createOrder(Order order) {
        String query = "INSERT INTO orders (customer_id, order_date, total_amount) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, order.getCustomerId());
            stmt.setDate(2, order.getOrderDate());
            stmt.setDouble(3, order.getTotalAmount());
            stmt.executeUpdate();
            System.out.println("=> Tạo đơn hàng thành công!");
        } catch (SQLException e) {
            System.out.println("Lỗi CSDL khi tạo đơn (kiểm tra lại ID khách hàng): " + e.getMessage());
        }
    }

    public void listAllOrders() {
        // JOIN để lấy tên khách hàng thay vì chỉ lấy ID
        String query = "SELECT o.id, c.name, o.order_date, o.total_amount " +
                "FROM orders o JOIN customers c ON o.customer_id = c.id";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n--- TẤT CẢ ĐƠN HÀNG ---");
            System.out.printf("%-5s | %-25s | %-12s | %-15s\n", "ID", "Tên Khách Hàng", "Ngày Đặt", "Tổng Tiền");
            System.out.println("-------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5d | %-25s | %-12s | %-15.2f\n",
                        rs.getInt("id"), rs.getString("name"), rs.getDate("order_date"), rs.getDouble("total_amount"));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi CSDL: " + e.getMessage());
        }
    }

    public void getOrdersByCustomer(int customerId) {
        String query = "SELECT * FROM orders WHERE customer_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n--- ĐƠN HÀNG CỦA KHÁCH HÀNG ID " + customerId + " ---");
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.printf("Order ID: %d | Ngày Đặt: %s | Tổng Tiền: %.2f\n",
                        rs.getInt("id"), rs.getDate("order_date"), rs.getDouble("total_amount"));
            }
            if (!hasData) System.out.println("Không có đơn hàng nào.");
        } catch (SQLException e) {
            System.out.println("Lỗi CSDL: " + e.getMessage());
        }
    }
}
