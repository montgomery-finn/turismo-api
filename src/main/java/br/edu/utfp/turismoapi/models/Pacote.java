package br.edu.utfp.turismoapi.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "tb_pacote")
public class Pacote extends BaseEntity {

    private String descricao;

    private Double preco;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "pacote")
    private List<Reserva> reservas = new ArrayList<Reserva>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tb_pacote_passeio",
        joinColumns = @JoinColumn(name = "pacote_id"),
        inverseJoinColumns = @JoinColumn(name = "passeio_id"))
    private List<Passeio> passeios;

}
