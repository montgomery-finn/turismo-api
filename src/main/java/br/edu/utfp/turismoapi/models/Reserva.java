package br.edu.utfp.turismoapi.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "tb_reserva")
public class Reserva extends BaseEntity {
    
    //quando o passeio será realizado
    private LocalDateTime data;

    @ManyToOne
    @JoinColumn(name = "pacote_id")
    private Pacote pacote;

    //cliente que fez a reserva
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @JsonIgnore
    @OneToOne(mappedBy = "reserva")
    private Pagamento pagamento;

    @JsonIgnore
    @OneToOne(mappedBy = "reserva")
    private Avaliacao avaliacao;
}
