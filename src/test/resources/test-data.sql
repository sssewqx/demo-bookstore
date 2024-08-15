-- Вставка автора
INSERT INTO Author (id, first_name, last_name)
VALUES ('123e4567-e89b-12d3-a456-426614174000', 'Имя', 'Фамилия');

-- Вставка книг
INSERT INTO Book (id, title, author_id)
VALUES ('2e79d637-a309-43c0-a59e-7018985f612f', 'Первая книга', '123e4567-e89b-12d3-a456-426614174000');