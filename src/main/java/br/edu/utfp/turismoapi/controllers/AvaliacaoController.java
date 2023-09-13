package br.edu.utfp.turismoapi.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.utfp.turismoapi.models.Avaliacao;
import br.edu.utfp.turismoapi.models.Reserva;
import br.edu.utfp.turismoapi.repositories.AvaliacaoRepository;
import br.edu.utfp.turismoapi.repositories.ReservaRepository;

@RestController
@RequestMapping("/avaliacao")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping("")
    public List<Avaliacao> getAll() {
        return avaliacaoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        Optional<Avaliacao> avaliacaoOptional = avaliacaoRepository.findById(UUID.fromString(id));

        return avaliacaoOptional.isPresent()
                ? ResponseEntity.ok(avaliacaoOptional.get())
                : ResponseEntity.notFound().build();
    }

    @PostMapping("")
    public ResponseEntity<Object> create(@RequestBody Avaliacao avaliacao) {
        // Certifique-se de que a reserva associada à avaliação existe
        Optional<Reserva> reservaOptional = reservaRepository.findById(avaliacao.getReserva().getId());
        if (reservaOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Reserva não encontrada");
        }

        // Defina a reserva associada à avaliação
        avaliacao.setReserva(reservaOptional.get());

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(avaliacaoRepository.save(avaliacao));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao criar avaliação");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody Avaliacao updatedAvaliacao) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("Formato de UUID inválido");
        }

        Optional<Avaliacao> avaliacaoOptional = avaliacaoRepository.findById(uuid);

        if (avaliacaoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Avaliacao avaliacaoToUpdate = avaliacaoOptional.get();
        BeanUtils.copyProperties(updatedAvaliacao, avaliacaoToUpdate);
        avaliacaoToUpdate.setUpdatedAt(LocalDateTime.now());

        try {
            return ResponseEntity.ok().body(avaliacaoRepository.save(avaliacaoToUpdate));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao atualizar avaliação");
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

        Optional<Avaliacao> avaliacaoOptional = avaliacaoRepository.findById(uuid);

        if (avaliacaoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            avaliacaoRepository.delete(avaliacaoOptional.get());
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao excluir avaliação");
        }
    }
}
