package QuanLyDonHang;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static OrderManager manager = new OrderManager();

    public static void main(String[] args) {
        int choice = 0;
        while (choice != 7) {
            System.out.println("\n=== QUẢN LÝ CỬA HÀNG ===");
            System.out.println("1. Thêm sản phẩm mới");
            System.out.println("2. Thêm khách hàng (Mở rộng để có Data)");
            System.out.println("3. Cập nhật thông tin khách hàng");
            System.out.println("4. Tạo đơn hàng mới");
            System.out.println("5. Hiển thị danh sách đơn hàng");
            System.out.println("6. Tìm đơn hàng theo khách hàng");
            System.out.println("7. Thoát");
            System.out.print("Chọn chức năng (1-7): ");

            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1: addProductMenu(); break;
                    case 2: addCustomerMenu(); break;
                    case 3: updateCustomerMenu(); break;
                    case 4: createOrderMenu(); break;
                    case 5: manager.listAllOrders(); break;
                    case 6: searchOrdersMenu(); break;
                    case 7: System.out.println("Tạm biệt!"); break;
                    default: System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập số nguyên!");
            }
        }
    }

    private static void addProductMenu() {
        System.out.println("\n--- THÊM SẢN PHẨM ---");
        String name = inputString("Nhập tên sản phẩm: ");
        double price = inputDouble("Nhập giá sản phẩm: ");
        manager.addProduct(new Product(name, price));
    }

    private static void addCustomerMenu() {
        System.out.println("\n--- THÊM KHÁCH HÀNG ---");
        String name = inputString("Nhập tên KH: ");
        String email = inputString("Nhập email: ");
        manager.addCustomer(new Customer(name, email));
    }

    private static void updateCustomerMenu() {
        System.out.println("\n--- CẬP NHẬT KHÁCH HÀNG ---");
        int id = inputInt("Nhập ID khách hàng cần cập nhật: ");
        String name = inputString("Nhập tên mới: ");
        String email = inputString("Nhập email mới: ");
        manager.updateCustomer(id, new Customer(name, email));
    }

    private static void createOrderMenu() {
        System.out.println("\n--- TẠO ĐƠN HÀNG ---");
        int customerId = inputInt("Nhập ID khách hàng mua: ");
        int productId = inputInt("Nhập ID sản phẩm mua: ");

        double price = manager.getProductPrice(productId);
        if (price < 0) {
            System.out.println("Lỗi: Không tìm thấy sản phẩm có ID này!");
            return;
        }

        int quantity = inputInt("Nhập số lượng: ");
        double totalAmount = price * quantity; // Tính tổng tiền
        Date orderDate = Date.valueOf(LocalDate.now()); // Lấy ngày hiện tại

        Order newOrder = new Order(customerId, orderDate, totalAmount);
        manager.createOrder(newOrder);
    }

    private static void searchOrdersMenu() {
        System.out.println("\n--- TÌM KIẾM ĐƠN HÀNG ---");
        int customerId = inputInt("Nhập ID khách hàng: ");
        manager.getOrdersByCustomer(customerId);
    }

    // --- HÀM KIỂM TRA NHẬP LIỆU ---
    private static String inputString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Lỗi: Không được để trống!");
        }
    }

    private static int inputInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập số nguyên!");
            }
        }
    }

    private static double inputDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập số hợp lệ!");
            }
        }
    }
}
