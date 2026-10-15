package com.inotes.model;

import com.inotes.service.NoteManagement;

/**
 * Một ghi chú. Chỉ giữ dữ liệu.
 * save() và delete() không tự nối database — chúng nhờ NoteManagement,
 * và NoteManagement sẽ dùng chiến lược lưu trữ đang được chọn.
 */
public class Note {
    private int id;          // 0 = chưa lưu
    private String title;
    private String content;
    private int typeId;      // khóa ngoại sang note_type
    private String typeName; // chỉ để hiển thị, không ghi vào file

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getTypeId() { return typeId; }
    public void setTypeId(int typeId) { this.typeId = typeId; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    /** id = 0 thì thêm mới, ngược lại thì cập nhật. */
    public void save() {
        NoteManagement.getInstance().save(this);
    }

    public static void delete(int id) {
        NoteManagement.getInstance().deleteNote(id);
    }
}
