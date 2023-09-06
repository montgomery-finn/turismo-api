package br.edu.utfp.turismoapi.models;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfp.turismoapi.enums.ETipoPessoa;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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

    private ETipoPessoa tipo;
    
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "birth")
    private LocalDateTime nascimento;

    @Column(name = "password")
    private String Senha;

    @Column(name = "phone")
    private String Telefone;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "person_id")
    private List<Reserva> reservas = new ArrayList<Reserva>();
}
