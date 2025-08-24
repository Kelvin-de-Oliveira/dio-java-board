-- changeset kelvin:202508241700
-- comment cards table create

CREATE TABLE CARDS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    colum_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_column_card FOREIGN KEY ( colum_id ) REFERENCES BOARD_COLUMNS (id) ON DELETE CASCADE
)ENGINE=InnoDB;
