package br.edu.utfp.turismoapi.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.utfp.turismoapi.models.Person;
import br.edu.utfp.turismoapi.models.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, UUID> {
    List<Reserva> findByPerson(Person person);
}
