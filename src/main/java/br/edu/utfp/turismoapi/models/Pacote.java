package br.edu.utfp.turismoapi.models;

import java.util.ArrayList;
import java.util.List;

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

//passeio e pacote é a mesma coisa

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_pacote")
public class Pacote extends BaseEntity {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "pacote")
    private List<Reserva> reservas = new ArrayList<Reserva>();

    @ManyToMany
    @JoinTable(name = "tb_pacote_passeio",
        joinColumns = @JoinColumn(name = "pacote_id"),
        inverseJoinColumns = @JoinColumn(name = "passeio_id"))
    private List<Passeio> passeios;

}
