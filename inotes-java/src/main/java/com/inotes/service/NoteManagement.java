package com.inotes.service;

import com.inotes.model.Note;
import com.inotes.strategy.NoteDBStrategy;
import com.inotes.strategy.NoteStrategy;

import java.util.List;

/**
 * Lớp điều khiển nghiệp vụ.
 * Giữ một NoteStrategy và có thể đổi bằng setStrategy() khi chương trình đang chạy.
 */
public class NoteManagement {
    private static final NoteManagement INSTANCE = new NoteManagement(new NoteDBStrategy());

    private NoteStrategy strategy;

    public NoteManagement(NoteStrategy strategy) {
        this.strategy = strategy;
    }

    public static NoteManagement getInstance() {
        return INSTANCE;
    }

    public void setStrategy(NoteStrategy strategy) {
        this.strategy = strategy;
    }

    public String tenLuuTru() {
        return strategy.getClass().getSimpleName();
    }

    public Note save(Note note) {
        return strategy.save(note);
    }

    public List<Note> searchNotes(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return strategy.findAll();
        }
        return strategy.search(keyword);
    }

    public Note addNote(String title, String content, int typeId) {
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setTypeId(typeId);
        return save(note);
    }

    public Note updateNote(int id, String title, String content, int typeId) {
        Note note = new Note();
        note.setId(id);
        note.setTitle(title);
        note.setContent(content);
        note.setTypeId(typeId);
        return save(note);
    }

    public void deleteNote(int id) {
        strategy.delete(id);
    }
}
