package com.inotes.strategy;

import com.inotes.model.Note;
import com.inotes.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Lưu vào MySQL.
 * Luôn dùng PreparedStatement và dấu ? — không cộng chuỗi SQL.
 */
public class NoteDBStrategy implements NoteStrategy {

    @Override
    public Note save(Note note) {
        if (note.getId() <= 0) {
            return insert(note);
        }
        return update(note);
    }

    private Note insert(Note note) {
        String sql = "INSERT INTO notes(title, content, type_id) VALUES (?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, note.getTitle());
            ps.setString(2, note.getContent());
            ps.setInt(3, note.getTypeId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    note.setId(keys.getInt(1));
                }
            }
            return note;
        } catch (Exception e) {
            throw new RuntimeException("Không thêm được ghi chú vào MySQL", e);
        }
    }

    private Note update(Note note) {
        String sql = "UPDATE notes SET title = ?, content = ?, type_id = ? WHERE id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, note.getTitle());
            ps.setString(2, note.getContent());
            ps.setInt(3, note.getTypeId());
            ps.setInt(4, note.getId());
            ps.executeUpdate();
            return note;
        } catch (Exception e) {
            throw new RuntimeException("Không sửa được ghi chú", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM notes WHERE id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Không xóa được ghi chú", e);
        }
    }

    @Override
    public List<Note> findAll() {
        String sql = baseSelect() + " ORDER BY n.id DESC";
        return query(sql, null);
    }

    @Override
    public List<Note> search(String keyword) {
        String sql = baseSelect()
                + " WHERE n.title LIKE ? OR n.content LIKE ?"
                + " ORDER BY n.id DESC";
        return query(sql, "%" + keyword.trim() + "%");
    }

    private String baseSelect() {
        return "SELECT n.id, n.title, n.content, n.type_id, t.name AS type_name "
                + "FROM notes n LEFT JOIN note_type t ON t.id = n.type_id";
    }

    private List<Note> query(String sql, String like) {
        List<Note> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (like != null) {
                ps.setString(1, like);
                ps.setString(2, like);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(readNote(rs));
                }
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Không đọc được ghi chú từ MySQL", e);
        }
    }

    private Note readNote(ResultSet rs) throws Exception {
        Note note = new Note();
        note.setId(rs.getInt("id"));
        note.setTitle(rs.getString("title"));
        note.setContent(rs.getString("content"));
        note.setTypeId(rs.getInt("type_id"));
        note.setTypeName(rs.getString("type_name"));
        return note;
    }
}
