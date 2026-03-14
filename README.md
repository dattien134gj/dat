# Advanced Task Management System

Console application quản lý công việc (task) với lưu/đọc JSON, phù hợp cho portfolio sinh viên.

## Yêu cầu

- **JDK 11+**
- **Maven 3.6+** (để build và chạy)

## Cấu trúc project (Layered Architecture)

```
src/main/java/com/taskmanagement/
├── model/          # Task, Status, Priority (enum)
├── repository/     # TaskRepository (ArrayList in-memory)
├── service/        # TaskService (business logic)
├── util/           # FileUtil (JSON), InputUtil (console input)
└── main/           # Main.java (menu & UI)
```

## Build & Chạy

```bash
# Biên dịch
mvn compile

# Chạy ứng dụng
mvn exec:java -Dexec.mainClass="com.taskmanagement.main.Main"

# Hoặc đóng gói rồi chạy (sau khi mvn package, copy dependencies)
mvn package
java -cp "target/advanced-task-management-1.0-SNAPSHOT.jar;target/lib/*" com.taskmanagement.main.Main
```

Nếu dùng IDE: chạy class `com.taskmanagement.main.Main`.

## Tính năng

1. Thêm task mới  
2. Xem tất cả task  
3. Cập nhật trạng thái (TODO / IN_PROGRESS / DONE)  
4. Cập nhật độ ưu tiên (LOW / MEDIUM / HIGH)  
5. Xóa task  
6. Tìm kiếm theo từ khóa  
7. Sắp xếp theo độ ưu tiên  
8. Sắp xếp theo hạn  
9. Lọc theo trạng thái  
10. Lưu ra file JSON  
11. Đọc từ file JSON  

Dữ liệu mặc định lưu tại `tasks.json` (cùng thư mục chạy ứng dụng).
