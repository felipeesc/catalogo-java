package com.projeto.catalogo.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@With
@Table("players")
public class Players {
    @Id
    private Integer id;

    private String name;

}
