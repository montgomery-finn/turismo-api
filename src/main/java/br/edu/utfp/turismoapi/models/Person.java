package br.edu.utfp.turismoapi.models;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_person")
public class Person extends BaseEntity {
    
    @Column(name = "name", length = 150, nullable = false)
    private String nome;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "birth")
    private LocalDateTime nascimento;

    @Column(name = "password")
    private String Senha;

    @Column(name = "phone")
    private String Telefone;
}
