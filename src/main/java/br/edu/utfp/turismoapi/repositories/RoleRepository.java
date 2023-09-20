package br.edu.utfp.turismoapi.repositories;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.edu.utfp.turismoapi.models.Role;
import br.edu.utfp.turismoapi.models.RoleName;


public interface RoleRepository extends JpaRepository<Role, UUID> {
        @Query("FROM Role r WHERE r.name = :name")
        public Optional<Role> findByName(RoleName name);
}