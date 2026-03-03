package ToDoList;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static TaskManagement manager = new TaskManagement();

    public static void main(String[] args) {
        int choice = 0;
        do {
            System.out.println("\n=== QUẢN LÝ TO-DO LIST ===");
            System.out.println("1. Thêm công việc");
            System.out.println("2. Liệt kê công việc");
            System.out.println("3. Cập nhật trạng thái");
            System.out.println("4. Xóa công việc");
            System.out.println("5. Tìm kiếm công việc");
            System.out.println("6. Thống kê công việc");
            System.out.println("7. Thoát");
            System.out.print("Chọn chức năng (1-7): ");

            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1:
                        addTaskMenu();
                        break;
                    case 2:
                        manager.listTasks();
                        break;
                    case 3:
                        updateStatusMenu();
                        break;
                    case 4:
                        deleteTaskMenu();
                        break;
                    case 5:
                        searchTaskMenu();
                        break;
                    case 6:
                        manager.taskStatistics();
                        break;
                    case 7:
                        System.out.println("Đã thoát ứng dụng.");
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ, vui lòng nhập 1-7.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập một số hợp lệ!");
            }
        } while (choice != 7);
    }

    private static void addTaskMenu() {
        System.out.println("\n--- THÊM CÔNG VIỆC ---");
        String name = inputString("Nhập tên công việc: ");
        String status = inputStatus("Nhập trạng thái (1: chưa hoàn thành, 2: đã hoàn thành): ");
        manager.addTask(name, status);
    }

    private static void updateStatusMenu() {
        System.out.println("\n--- CẬP NHẬT TRẠNG THÁI ---");
        int id = inputInt("Nhập ID công việc: ");
        String status = inputStatus("Nhập trạng thái mới (1: chưa hoàn thành, 2: đã hoàn thành): ");
        manager.updateTaskStatus(id, status);
    }

    private static void deleteTaskMenu() {
        System.out.println("\n--- XÓA CÔNG VIỆC ---");
        int id = inputInt("Nhập ID công việc cần xóa: ");
        manager.deleteTask(id);
    }

    private static void searchTaskMenu() {
        System.out.println("\n--- TÌM KIẾM ---");
        String name = inputString("Nhập từ khóa tên công việc: ");
        manager.searchTaskByName(name);
    }

    // Các hàm phụ trợ kiểm tra dữ liệu đầu vào chống lỗi và bỏ trống
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
                System.out.println("Lỗi: ID phải là số nguyên!");
            }
        }
    }

    private static String inputStatus(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.equals("1")) return "chưa hoàn thành";
            if (input.equals("2")) return "đã hoàn thành";
            System.out.println("Lỗi: Chỉ nhập 1 hoặc 2!");
        }
    }
}
