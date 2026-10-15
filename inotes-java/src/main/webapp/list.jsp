<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>iNotes</title>
  <style>
    body { font-family: sans-serif; max-width: 880px; margin: 32px auto; color: #1c1814; }
    table { width: 100%; border-collapse: collapse; }
    th, td { border-bottom: 1px solid #e3d8c8; text-align: left; padding: 10px; }
    a, button { font: inherit; }
  </style>
</head>
<body>
  <h1>Danh sách ghi chú</h1>
  <p>Tầng lưu trữ hiện tại: <strong>${storage}</strong></p>

  <form method="get" action="${pageContext.request.contextPath}/notes">
    <input type="search" name="keyword" value="<c:out value='${keyword}'/>" placeholder="Tìm tiêu đề hoặc nội dung">
    <button type="submit">Tìm kiếm</button>
    <a href="${pageContext.request.contextPath}/notes?action=new">Thêm mới</a>
  </form>

  <form method="post" action="${pageContext.request.contextPath}/notes">
    <input type="hidden" name="action" value="strategy">
    <button type="submit" name="kind" value="db">Cơ sở dữ liệu</button>
    <button type="submit" name="kind" value="file">Tập tin</button>
  </form>

  <table>
    <thead>
      <tr><th>#</th><th>Tiêu đề</th><th>Phân loại</th><th>Thao tác</th></tr>
    </thead>
    <tbody>
      <c:forEach var="note" items="${notes}" varStatus="st">
        <tr>
          <td>${st.count}</td>
          <td><c:out value="${note.title}"/></td>
          <td><c:out value="${note.typeName}"/></td>
          <td>
            <a href="${pageContext.request.contextPath}/notes?action=edit&id=${note.id}">Sửa</a>
            <form method="post" action="${pageContext.request.contextPath}/notes" style="display:inline">
              <input type="hidden" name="action" value="delete">
              <input type="hidden" name="id" value="${note.id}">
              <button type="submit">Xóa</button>
            </form>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
</body>
</html>
