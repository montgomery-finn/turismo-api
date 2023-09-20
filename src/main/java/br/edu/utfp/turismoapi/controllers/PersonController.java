package br.edu.utfp.turismoapi.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfp.turismoapi.dto.PersonDTO;
import br.edu.utfp.turismoapi.models.Person;
import br.edu.utfp.turismoapi.models.RoleName;
import br.edu.utfp.turismoapi.repositories.PersonRepository;
import br.edu.utfp.turismoapi.repositories.RoleRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pessoa")
public class PersonController {
    
    @Autowired
    PersonRepository personRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/pages")
    public ResponseEntity<Page<Person>> getAllPage(
        @PageableDefault(page = 0, size = 10, sort = "nome",
        direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok().body(personRepository.findAll(pageable));
    }


    @GetMapping(value = {"", "/"})
    public List<Person> getAll() {
        return personRepository.findAll();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        Optional<Person> personOptional =  personRepository.findById(UUID.fromString(id));
        
        return personOptional.isPresent() 
            ? ResponseEntity.ok(personOptional.get())
            : ResponseEntity.notFound().build();

    }

    @PostMapping("")
    public ResponseEntity<Object> create(@Valid @RequestBody PersonDTO personDTO) {
        
        if (this.personRepository.existsByEmail(personDTO.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.SEE_OTHER)
                    .body("Conflict: E-mail exists.");
        }
        
        var person = new Person();
        BeanUtils.copyProperties(personDTO, person);

        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        person.setCreatedAt(now);
        person.setUpdatedAt(now);
        person.setPassword(passwordEncoder.encode(personDTO.getPassword()));

        // Adicionando o papel padrão para a pessoa
        // var role = roleRepository.findByName(RoleName.USER);
        // if (role.isPresent())
        //     person.addRole(role.get());
        
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(personRepository.save(person));
        } catch(Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status((HttpStatus.BAD_REQUEST))
                .body("Falha ao criar pessoa");
        }        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody PersonDTO personDTO) {
        UUID uuid;

        try {
            uuid = UUID.fromString(id);
        }
        catch (Exception exception) {
            return ResponseEntity.badRequest().body("Formato de UUID inválido");
        }

        var person = personRepository.findById(uuid);

        if(person.isEmpty())
            return ResponseEntity.notFound().build();

        var personToUpdate = person.get();
        BeanUtils.copyProperties(personDTO, personToUpdate);
        personToUpdate.setUpdatedAt(LocalDateTime.now());

        try {
            return ResponseEntity.ok()
                .body(personRepository.save(personToUpdate));
        } catch(Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status((HttpStatus.BAD_REQUEST))
                .body("Falha ao atualizar pessoa");
        }      
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        UUID uuid;

        try {
            uuid = UUID.fromString(id);
        }
        catch (Exception exception) {
            return ResponseEntity.badRequest().body("Formato de UUID inválido");
        }

        var person = personRepository.findById(uuid);

        if(person.isEmpty())
            return ResponseEntity.notFound().build();

        try {
            personRepository.delete(person.get());
            return ResponseEntity.ok().build();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }
}
