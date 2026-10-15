package com.inotes.strategy;

import com.inotes.model.Note;
import java.util.List;

/**
 * Hợp đồng lưu trữ. Giao diện và NoteManagement chỉ biết interface này.
 * Muốn đổi MySQL <-> file thì chỉ cần lớp khác implements NoteStrategy.
 */
public interface NoteStrategy {
    Note save(Note note);
    void delete(int id);
    List<Note> findAll();
    List<Note> search(String keyword);
}
