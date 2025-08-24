-- changeset kelvin:202508241700
-- comment columns table create

CREATE TABLE BOARD_COLUMNS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    board_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    col_order INT NOT NULL,
    col_type ENUM('INITIAL', 'PENDING', 'FINAL', 'CANCELLED') NOT NULL,
    CONSTRAINT fk_columns_board FOREIGN KEY (board_id) REFERENCES BOARDS(id) ON DELETE CASCADE,
    CONSTRAINT id_order_uk UNIQUE KEY unique_board_id_order (board_id, col_order)
)ENGINE=InnoDB;
