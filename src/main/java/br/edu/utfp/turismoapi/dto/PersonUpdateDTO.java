package br.edu.utfp.turismoapi.dto;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import br.edu.utfp.turismoapi.enums.ETipoPessoa;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonUpdateDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    @PastOrPresent
    private LocalDateTime nascimento;

    @NotBlank(message = "Selecione o tipo")
    private ETipoPessoa tipo;
}
