package br.com.kelvin.persistence.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
public class BoardEntity {
    private Long id;
    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BoardColumnEntity> columns  = new ArrayList<>();
}
