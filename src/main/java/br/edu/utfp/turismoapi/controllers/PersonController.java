package br.edu.utfp.turismoapi.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfp.turismoapi.dto.PersonDTO;
import br.edu.utfp.turismoapi.models.Person;
import br.edu.utfp.turismoapi.repositories.PersonRepository;

@RestController
@RequestMapping("/pessoa")
public class PersonController {
    
    @Autowired
    PersonRepository personRepository;

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
    public ResponseEntity<Object> create(@RequestBody PersonDTO personDTO) {
        var person = new Person(); //pessoa para persistir no DB
        BeanUtils.copyProperties(personDTO, person);
        

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
}
