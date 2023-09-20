package br.edu.utfp.turismoapi.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.utfp.turismoapi.models.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {
    public boolean existsByIdAndEmail(UUID id, String email);

    public boolean existsByEmail(String email);

    public Optional<Person> findByEmail(String email);
    
    // public boolean existsByEmailAndPassword(String email, String password);

    // public Optional<Person> findByEmailAndPassword(String email, String password);

    // public List<Person> findByName(String name);
}
