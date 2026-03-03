package QuanLyPhim;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static MovieManagement manager = new MovieManagement();

    public static void main(String[] args) {
        int choice = 0;
        do {
            System.out.println("\n=== CHƯƠNG TRÌNH QUẢN LÝ PHIM ===");
            System.out.println("1. Thêm phim");
            System.out.println("2. Liệt kê phim");
            System.out.println("3. Sửa phim");
            System.out.println("4. Xóa phim");
            System.out.println("5. Thoát");
            System.out.print("Vui lòng chọn chức năng (1-5): ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        addMovieMenu();
                        break;
                    case 2:
                        manager.listMovies();
                        break;
                    case 3:
                        updateMovieMenu();
                        break;
                    case 4:
                        deleteMovieMenu();
                        break;
                    case 5:
                        System.out.println("Đang thoát chương trình. Tạm biệt!");
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn từ 1 đến 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập một số nguyên!");
            }
        } while (choice != 5);
    }

    private static void addMovieMenu() {
        System.out.println("\n--- THÊM PHIM MỚI ---");
        String title = inputString("Nhập tiêu đề phim: ");
        String director = inputString("Nhập đạo diễn: ");
        int year = inputYear("Nhập năm phát hành: ");

        manager.addMovie(title, director, year);
    }

    private static void updateMovieMenu() {
        System.out.println("\n--- CẬP NHẬT PHIM ---");
        int id = inputInt("Nhập ID phim cần sửa: ");
        String title = inputString("Nhập tiêu đề mới: ");
        String director = inputString("Nhập đạo diễn mới: ");
        int year = inputYear("Nhập năm phát hành mới: ");

        manager.updateMovie(id, title, director, year);
    }

    private static void deleteMovieMenu() {
        System.out.println("\n--- XÓA PHIM ---");
        int id = inputInt("Nhập ID phim cần xóa: ");
        manager.deleteMovie(id);
    }

    // Hàm hỗ trợ nhập chuỗi và kiểm tra rỗng
    private static String inputString(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Lỗi: Không được bỏ trống trường này!");
        }
    }

    // Hàm hỗ trợ nhập năm và bắt ngoại lệ không phải là số
    private static int inputYear(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int year = Integer.parseInt(scanner.nextLine().trim());
                if(year > 1800 && year <= 2100) {
                    return year;
                } else {
                    System.out.println("Lỗi: Năm phát hành không hợp lý!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Năm phát hành phải là số nguyên!");
            }
        }
    }

    // Hàm hỗ trợ nhập ID
    private static int inputInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: ID phải là số nguyên!");
            }
        }
    }
}
