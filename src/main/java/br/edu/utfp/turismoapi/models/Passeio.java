package br.edu.utfp.turismoapi.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "tb_passeio")
public class Passeio extends BaseEntity {
    private String destino;

    private String itinerario;

    private Double preco;

    @ManyToMany
    @JoinTable(name = "tb_pacote_passeio",
        joinColumns = @JoinColumn(name = "passeio_id"),
        inverseJoinColumns = @JoinColumn(name = "pacote_id"))
    private List<Pacote> pacotes;
}
