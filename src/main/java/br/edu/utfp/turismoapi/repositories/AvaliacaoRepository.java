package br.edu.utfp.turismoapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.utfp.turismoapi.models.Avaliacao;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, UUID> {
    
}
