package br.edu.utfp.turismoapi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "tb_pagamento")
public class Pagamento extends BaseEntity  {
    private Double valor;


    @OneToOne()
    @JoinColumn(name = "reserva_id")
    private Reserva reserva;
}
