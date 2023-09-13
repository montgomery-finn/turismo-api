package br.edu.utfp.turismoapi.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.utfp.turismoapi.models.Passeio;
import br.edu.utfp.turismoapi.repositories.PasseioRepository;
import br.edu.utfp.turismoapi.repositories.PacoteRepository;

@RestController
@RequestMapping("/passeio")
public class PasseioController {

    @Autowired
    private PasseioRepository passeioRepository;

    @Autowired
    private PacoteRepository pacoteRepository;

    @GetMapping("")
    public List<Passeio> getAll() {
        return passeioRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        Optional<Passeio> passeioOptional = passeioRepository.findById(UUID.fromString(id));

        return passeioOptional.isPresent()
                ? ResponseEntity.ok(passeioOptional.get())
                : ResponseEntity.notFound().build();
    }

    @PostMapping("")
    public ResponseEntity<Object> create(@RequestBody Passeio passeio) {
        // Certifique-se de que os pacotes associados ao passeio existem
        passeio.getPacotes().forEach(pacote -> {
            if (!pacoteRepository.existsById(pacote.getId())) {
                ResponseEntity.badRequest().body("Pacote não encontrado: " + pacote.getId());
            }
        });

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(passeioRepository.save(passeio));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao criar passeio");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody Passeio updatedPasseio) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("Formato de UUID inválido");
        }

        Optional<Passeio> passeioOptional = passeioRepository.findById(uuid);

        if (passeioOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Passeio passeioToUpdate = passeioOptional.get();
        BeanUtils.copyProperties(updatedPasseio, passeioToUpdate);

        // Certifique-se de que os pacotes associados ao passeio existem
        passeioToUpdate.getPacotes().forEach(pacote -> {
            if (!pacoteRepository.existsById(pacote.getId())) {
                ResponseEntity.badRequest().body("Pacote não encontrado: " + pacote.getId());
            }
        });

        try {
            return ResponseEntity.ok().body(passeioRepository.save(passeioToUpdate));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao atualizar passeio");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("Formato de UUID inválido");
        }

        Optional<Passeio> passeioOptional = passeioRepository.findById(uuid);

        if (passeioOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            passeioRepository.delete(passeioOptional.get());
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao excluir passeio");
        }
    }
}
