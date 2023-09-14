package br.edu.utfp.turismoapi.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import java.time.LocalDateTime;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.utfp.turismoapi.dto.PacoteDTO;
import br.edu.utfp.turismoapi.models.Pacote;
import br.edu.utfp.turismoapi.models.Passeio;
import br.edu.utfp.turismoapi.repositories.PacoteRepository;
import br.edu.utfp.turismoapi.repositories.PasseioRepository;

@RestController
@RequestMapping("/pacote")
public class PacoteController {

    @Autowired
    private PacoteRepository pacoteRepository;

    @Autowired
    private PasseioRepository passeioRepository;

    @GetMapping("")
    public List<Pacote> getAll() {
        return pacoteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        Optional<Pacote> pacoteOptional = pacoteRepository.findById(UUID.fromString(id));

        return pacoteOptional.isPresent()
                ? ResponseEntity.ok(pacoteOptional.get())
                : ResponseEntity.notFound().build();
    }

    @PostMapping("")
    public ResponseEntity<Object> create(@RequestBody PacoteDTO pacoteDTO) {
            Pacote pacote = new Pacote();
            BeanUtils.copyProperties(pacoteDTO, pacote);

            List<Passeio> passeios = passeioRepository.findAllById(pacoteDTO.getPasseiosIds());
            pacote.setPasseios(passeios);

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(pacoteRepository.save(pacote));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao criar pacote");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody Pacote updatedPacote) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("Formato de UUID inválido");
        }

        Optional<Pacote> pacoteOptional = pacoteRepository.findById(uuid);

        if (pacoteOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pacote pacoteToUpdate = pacoteOptional.get();
        BeanUtils.copyProperties(updatedPacote, pacoteToUpdate);
        pacoteToUpdate.setUpdatedAt(LocalDateTime.now());

        // Certifique-se de que os passeios associados ao pacote existem
        pacoteToUpdate.getPasseios().forEach(passeio -> {
            if (!passeioRepository.existsById(passeio.getId())) {
                ResponseEntity.badRequest().body("Passeio não encontrado: " + passeio.getId());
            }
        });

        try {
            return ResponseEntity.ok().body(pacoteRepository.save(pacoteToUpdate));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao atualizar pacote");
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

        Optional<Pacote> pacoteOptional = pacoteRepository.findById(uuid);

        if (pacoteOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            pacoteRepository.delete(pacoteOptional.get());
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao excluir pacote");
        }
    }
}
