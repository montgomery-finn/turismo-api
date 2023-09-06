package br.edu.utfp.turismoapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.utfp.turismoapi.models.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, UUID> {
    
}
