package br.com.kelvin.persistence.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardColumnEntity {

    public enum ColumnType {
        INITIAL,
        PENDING,
        FINAL,
        CANCELLED
    }

    private Long id;
    private String name;
    private Integer colOrder;
    private ColumnType colType;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private BoardEntity board = new BoardEntity();

    private List<CardEntity> cards = new ArrayList<>();
}
