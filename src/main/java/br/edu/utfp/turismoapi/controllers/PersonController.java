package br.edu.utfp.turismoapi.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfp.turismoapi.models.Person;
import br.edu.utfp.turismoapi.repositories.PersonRepository;

@RestController
@RequestMapping("/pessoa")
public class PersonController {
    
    @Autowired
    PersonRepository personRepository;

    @GetMapping(value = {"", "/"})
    public String getAll() {
        return "Aqui estão todas as pessoas";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id) {
        return "Aqui está a pessoa " + id;
    }

    @PostMapping("/")
    public Person create() {
        var person = new Person();
        person.setNome("Luca da Silva");
        person.setEmail("luca@email.com");
        person.setNascimento(LocalDateTime.now());

        personRepository.save(person);
        return person;
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id) {
        return "Pessoa " + id + " atualizada";
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return "Pessoa " + id + " excluida";
    }
}
