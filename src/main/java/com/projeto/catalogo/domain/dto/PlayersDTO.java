package com.projeto.catalogo.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@With
public class PlayersDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    @Id
    private Integer id;

    @NotBlank(message = "the name of this players cannot be empty")
    @JsonProperty
    private String name;

}
