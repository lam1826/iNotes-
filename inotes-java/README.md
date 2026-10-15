# iNotes

Quản lý ghi chú: Servlet + JSP + JDBC + Strategy Pattern.

Giao diện không biết dữ liệu đang nằm ở MySQL hay ở file. Nó chỉ gọi `NoteManagement`. Muốn đổi chỗ lưu thì gọi `setStrategy(...)`.

## Ai làm việc gì

| Lớp | Việc |
| --- | --- |
| `Note` | Dữ liệu: title, content, typeId. `save()` / `delete()` nhờ NoteManagement |
| `NoteStrategy` | Hợp đồng: save, delete, findAll, search |
| `NoteDBStrategy` | Làm hợp đồng đó bằng MySQL + PreparedStatement |
| `NoteFileStrategy` | Làm hợp đồng đó bằng file `id\|title\|content\|typeId` |
| `NoteManagement` | Nghiệp vụ: thêm, sửa, xóa, tìm. Giữ strategy hiện tại |
| `NoteServlet` | Nhận form, gọi NoteManagement, forward sang JSP |

## Chạy

1. Chạy `sql/schema.sql` trên MySQL.
2. Sửa user/password trong `DBUtil`, hoặc đặt `INOTES_DB_URL`, `INOTES_DB_USER`, `INOTES_DB_PASS`.
3. `mvn clean package` rồi copy `target/inotes.war` vào Tomcat 10+.
4. Mở `/inotes/notes`.

File lưu trữ mặc định là `~/inotes.txt`.
