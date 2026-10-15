<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Thêm mới ghi chú</title>
  <style>
    body { font-family: sans-serif; max-width: 640px; margin: 32px auto; }
    label { display: block; margin-top: 12px; }
    input, select, textarea { width: 100%; box-sizing: border-box; margin-top: 4px; }
  </style>
</head>
<body>
  <h1>${empty note ? "Thêm mới ghi chú" : "Sửa ghi chú"}</h1>
  <p style="color:#9c3a28">${error}</p>
  <form method="post" action="${pageContext.request.contextPath}/notes">
    <input type="hidden" name="action" value="save">
    <input type="hidden" name="id" value="${note.id}">
    <label>Tiêu đề
      <input type="text" name="title" value="<c:out value='${note.title}'/>" required>
    </label>
    <label>Phân loại
      <select name="typeId">
        <option value="1" ${note.typeId == 1 ? "selected" : ""}>Cá nhân</option>
        <option value="2" ${note.typeId == 2 ? "selected" : ""}>Công việc</option>
        <option value="3" ${note.typeId == 3 ? "selected" : ""}>Học tập</option>
      </select>
    </label>
    <label>Nội dung
      <textarea name="content" rows="8"><c:out value="${note.content}"/></textarea>
    </label>
    <p>
      <button type="submit">Lưu ghi chú</button>
      <a href="${pageContext.request.contextPath}/notes">Hủy bỏ</a>
    </p>
  </form>
</body>
</html>
