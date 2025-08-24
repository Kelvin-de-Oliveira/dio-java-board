package br.com.kelvin.persistence.entity;


import lombok.Data;


import java.sql.Timestamp;

@Data
public class BlockEntity {

    private Long id;
    private String blockReason;
    private Timestamp blockedAt;
    private String unblockedReason;
    private Timestamp unblockedAt;

}
