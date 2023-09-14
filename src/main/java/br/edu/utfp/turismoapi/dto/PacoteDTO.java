package br.edu.utfp.turismoapi.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PacoteDTO {
    private String descricao;

    private Double preco;
    
    private List<UUID> passeiosIds;
}
