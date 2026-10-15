CREATE DATABASE IF NOT EXISTS inotes_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE inotes_db;

CREATE TABLE IF NOT EXISTS note_type (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  name        VARCHAR(100) NOT NULL,
  description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS notes (
  id      INT AUTO_INCREMENT PRIMARY KEY,
  title   VARCHAR(255) NOT NULL,
  content TEXT,
  type_id INT,
  CONSTRAINT fk_notes_type
    FOREIGN KEY (type_id) REFERENCES note_type(id)
);

INSERT INTO note_type (name, description) VALUES
  ('Cá nhân', 'Ghi chú đời sống'),
  ('Công việc', 'Công việc và lịch họp'),
  ('Học tập', 'Bài học và ôn tập');
