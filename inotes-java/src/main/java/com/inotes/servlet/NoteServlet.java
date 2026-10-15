package com.inotes.servlet;

import com.inotes.model.Note;
import com.inotes.service.NoteManagement;
import com.inotes.strategy.NoteDBStrategy;
import com.inotes.strategy.NoteFileStrategy;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Cửa vào HTTP.
 * GET  -> hiện danh sách hoặc form.
 * POST -> lưu, xóa, hoặc đổi chiến lược lưu trữ.
 * Servlet không viết SQL. Nó chỉ gọi NoteManagement.
 */
@WebServlet("/notes")
public class NoteServlet extends HttpServlet {
    private final NoteManagement notes = NoteManagement.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = text(req, "action");

        if ("new".equals(action) || "edit".equals(action)) {
            if ("edit".equals(action)) {
                int id = Integer.parseInt(text(req, "id"));
                req.setAttribute("note", findById(id));
            }
            req.getRequestDispatcher("/form.jsp").forward(req, resp);
            return;
        }

        String keyword = text(req, "keyword");
        req.setAttribute("notes", notes.searchNotes(keyword));
        req.setAttribute("keyword", keyword);
        req.setAttribute("storage", notes.tenLuuTru());
        req.getRequestDispatcher("/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = text(req, "action");

        if ("save".equals(action)) {
            saveFromForm(req, resp);
            return;
        }
        if ("delete".equals(action)) {
            notes.deleteNote(Integer.parseInt(text(req, "id")));
        }
        if ("strategy".equals(action)) {
            doiLuuTru(text(req, "kind"));
        }
        resp.sendRedirect(req.getContextPath() + "/notes");
    }

    private void saveFromForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String title = text(req, "title").trim();
        String content = text(req, "content");
        int typeId = Integer.parseInt(text(req, "typeId"));
        String idRaw = text(req, "id");

        if (title.isEmpty()) {
            req.setAttribute("error", "Tiêu đề không được để trống.");
            req.getRequestDispatcher("/form.jsp").forward(req, resp);
            return;
        }
        if (idRaw.isEmpty() || "0".equals(idRaw)) {
            notes.addNote(title, content, typeId);
        } else {
            notes.updateNote(Integer.parseInt(idRaw), title, content, typeId);
        }
        resp.sendRedirect(req.getContextPath() + "/notes");
    }

    private void doiLuuTru(String kind) {
        if ("file".equals(kind)) {
            notes.setStrategy(new NoteFileStrategy());
        } else {
            notes.setStrategy(new NoteDBStrategy());
        }
    }

    private Note findById(int id) {
        for (Note note : notes.searchNotes("")) {
            if (note.getId() == id) return note;
        }
        return null;
    }

    private String text(HttpServletRequest req, String name) {
        String value = req.getParameter(name);
        return value == null ? "" : value;
    }
}
