-- changeset kelvin:202508241700
-- comment blocks table create

CREATE TABLE BLOCKS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_id BIGINT NOT NULL,
    block_reason TEXT NOT NULL,
    blocked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    unblocked_reason TEXT NOT NULL,
    unblocked_at TIMESTAMP NULL,
    CONSTRAINT fk_card_block FOREIGN KEY (card_id ) REFERENCES CARDS (id) ON DELETE CASCADE
) ENGINE=InnoDB;
