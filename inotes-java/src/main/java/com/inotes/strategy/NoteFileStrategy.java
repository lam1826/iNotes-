package com.inotes.strategy;

import com.inotes.model.Note;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Lưu ra một file text. Mỗi ghi chú là một dòng:
 * id|title|content|typeId
 * Dấu | hoặc xuống dòng trong nội dung được viết thành \| và \n.
 */
public class NoteFileStrategy implements NoteStrategy {
    private final Path file;

    public NoteFileStrategy() {
        this(Path.of(System.getProperty("user.home"), "inotes.txt"));
    }

    public NoteFileStrategy(Path file) {
        this.file = file;
    }

    @Override
    public Note save(Note note) {
        List<Note> notes = readAll();
        if (note.getId() <= 0) {
            note.setId(nextId(notes));
            notes.add(note);
        } else {
            replace(notes, note);
        }
        writeAll(notes);
        return note;
    }

    @Override
    public void delete(int id) {
        List<Note> notes = readAll();
        notes.removeIf(note -> note.getId() == id);
        writeAll(notes);
    }

    @Override
    public List<Note> findAll() {
        List<Note> notes = readAll();
        notes.sort((a, b) -> Integer.compare(b.getId(), a.getId()));
        return notes;
    }

    @Override
    public List<Note> search(String keyword) {
        String q = keyword.trim().toLowerCase();
        List<Note> found = new ArrayList<>();
        for (Note note : findAll()) {
            String title = note.getTitle() == null ? "" : note.getTitle().toLowerCase();
            String content = note.getContent() == null ? "" : note.getContent().toLowerCase();
            if (title.contains(q) || content.contains(q)) {
                found.add(note);
            }
        }
        return found;
    }

    private int nextId(List<Note> notes) {
        int max = 0;
        for (Note note : notes) {
            if (note.getId() > max) max = note.getId();
        }
        return max + 1;
    }

    private void replace(List<Note> notes, Note updated) {
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId() == updated.getId()) {
                notes.set(i, updated);
                return;
            }
        }
        notes.add(updated);
    }

    private List<Note> readAll() {
        List<Note> notes = new ArrayList<>();
        if (!Files.exists(file)) return notes;
        try {
            for (String raw : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                if (raw.isBlank() || raw.startsWith("#")) continue;
                notes.add(parseLine(raw));
            }
            return notes;
        } catch (Exception e) {
            throw new RuntimeException("Không đọc được file " + file, e);
        }
    }

    private void writeAll(List<Note> notes) {
        StringBuilder text = new StringBuilder("# id|title|content|typeId\n");
        for (Note note : notes) {
            text.append(note.getId()).append('|')
                    .append(escape(note.getTitle())).append('|')
                    .append(escape(note.getContent())).append('|')
                    .append(note.getTypeId()).append('\n');
        }
        try {
            Files.writeString(file, text.toString(), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Không ghi được file " + file, e);
        }
    }

    private Note parseLine(String line) {
        String[] parts = splitByPipe(line);
        Note note = new Note();
        note.setId(Integer.parseInt(parts[0]));
        note.setTitle(unescape(parts[1]));
        note.setContent(unescape(parts[2]));
        note.setTypeId(Integer.parseInt(parts[3]));
        return note;
    }

    private String[] splitByPipe(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\\' && i + 1 < line.length()) {
                field.append(c).append(line.charAt(++i));
            } else if (c == '|') {
                parts.add(field.toString());
                field.setLength(0);
            } else {
                field.append(c);
            }
        }
        parts.add(field.toString());
        return parts.toArray(new String[0]);
    }

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("|", "\\|").replace("\n", "\\n");
    }

    private String unescape(String value) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '\\' && i + 1 < value.length()) {
                char next = value.charAt(++i);
                out.append(next == 'n' ? '\n' : next);
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }
}
