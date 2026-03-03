package QuanLySach;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static BookManager manager = new BookManager();

    public static void main(String[] args) {
        int choice = 0;
        while (choice != 6) {
            System.out.println("\n=== CHƯƠNG TRÌNH QUẢN LÝ THƯ VIỆN ===");
            System.out.println("1. Thêm sách");
            System.out.println("2. Cập nhật thông tin sách");
            System.out.println("3. Xóa sách");
            System.out.println("4. Tìm kiếm sách theo tác giả");
            System.out.println("5. Hiển thị tất cả sách");
            System.out.println("6. Thoát");
            System.out.print("Vui lòng chọn chức năng (1-6): ");

            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1:
                        addBookMenu();
                        break;
                    case 2:
                        updateBookMenu();
                        break;
                    case 3:
                        deleteBookMenu();
                        break;
                    case 4:
                        searchBookMenu();
                        break;
                    case 5:
                        manager.listAllBooks();
                        break;
                    case 6:
                        System.out.println("Đã thoát chương trình.");
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ, vui lòng nhập lại.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Bạn phải nhập vào một số nguyên!");
            }
        }
    }

    private static void addBookMenu() {
        System.out.println("\n--- THÊM SÁCH MỚI ---");
        String title = inputString("Nhập tiêu đề sách: ");
        String author = inputString("Nhập tên tác giả: ");
        int year = inputInt("Nhập năm xuất bản: ");
        double price = inputDouble("Nhập giá sách: ");

        Book newBook = new Book(title, author, year, price);
        manager.addBook(newBook);
    }

    private static void updateBookMenu() {
        System.out.println("\n--- CẬP NHẬT THÔNG TIN SÁCH ---");
        int id = inputInt("Nhập ID sách cần sửa: ");
        System.out.println("- Nhập thông tin mới -");
        String title = inputString("Nhập tiêu đề sách: ");
        String author = inputString("Nhập tên tác giả: ");
        int year = inputInt("Nhập năm xuất bản: ");
        double price = inputDouble("Nhập giá sách: ");

        Book updatedBook = new Book(title, author, year, price);
        manager.updateBook(id, updatedBook);
    }

    private static void deleteBookMenu() {
        System.out.println("\n--- XÓA SÁCH ---");
        int id = inputInt("Nhập ID sách cần xóa: ");
        manager.deleteBook(id);
    }

    private static void searchBookMenu() {
        System.out.println("\n--- TÌM KIẾM THEO TÁC GIẢ ---");
        String author = inputString("Nhập tên (hoặc một phần tên) tác giả: ");
        manager.findBooksByAuthor(author);
    }

    // --- CÁC HÀM HỖ TRỢ XỬ LÝ NHẬP LIỆU ---

    private static String inputString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Lỗi: Không được để trống dữ liệu!");
        }
    }

    private static int inputInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập số nguyên hợp lệ!");
            }
        }
    }

    private static double inputDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập số thập phân hợp lệ (ví dụ: 150000.0)!");
            }
        }
    }
}
