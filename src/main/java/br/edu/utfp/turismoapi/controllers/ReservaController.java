package br.edu.utfp.turismoapi.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.utfp.turismoapi.models.Reserva;
import br.edu.utfp.turismoapi.models.Pacote;
import br.edu.utfp.turismoapi.models.Person;
import br.edu.utfp.turismoapi.repositories.ReservaRepository;
import br.edu.utfp.turismoapi.repositories.PacoteRepository;
import br.edu.utfp.turismoapi.repositories.PersonRepository;
import br.edu.utfp.turismoapi.repositories.PagamentoRepository;
import br.edu.utfp.turismoapi.repositories.AvaliacaoRepository;

@RestController
@RequestMapping("/reserva")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private PacoteRepository pacoteRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @GetMapping("")
    public List<Reserva> getAll() {
        return reservaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        Optional<Reserva> reservaOptional = reservaRepository.findById(UUID.fromString(id));

        return reservaOptional.isPresent()
                ? ResponseEntity.ok(reservaOptional.get())
                : ResponseEntity.notFound().build();
    }

    @PostMapping("")
    public ResponseEntity<Object> create(@RequestBody Reserva reserva) {
        // Certifique-se de que o pacote associado à reserva existe
        Optional<Pacote> pacoteOptional = pacoteRepository.findById(reserva.getPacote().getId());
        if (pacoteOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Pacote não encontrado");
        }

        // Certifique-se de que o cliente (Person) associado à reserva existe
        Optional<Person> personOptional = personRepository.findById(reserva.getPerson().getId());
        if (personOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Cliente não encontrado");
        }

        // Defina o pacote e o cliente associados à reserva
        reserva.setPacote(pacoteOptional.get());
        reserva.setPerson(personOptional.get());

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(reservaRepository.save(reserva));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao criar reserva");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody Reserva updatedReserva) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("Formato de UUID inválido");
        }

        Optional<Reserva> reservaOptional = reservaRepository.findById(uuid);

        if (reservaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reserva reservaToUpdate = reservaOptional.get();
        BeanUtils.copyProperties(updatedReserva, reservaToUpdate);

        // Certifique-se de que o pacote associado à reserva existe
        Optional<Pacote> pacoteOptional = pacoteRepository.findById(reservaToUpdate.getPacote().getId());
        if (pacoteOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Pacote não encontrado");
        }

        // Certifique-se de que o cliente (Person) associado à reserva existe
        Optional<Person> personOptional = personRepository.findById(reservaToUpdate.getPerson().getId());
        if (personOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Cliente não encontrado");
        }

        // Defina o pacote e o cliente associados à reserva
        reservaToUpdate.setPacote(pacoteOptional.get());
        reservaToUpdate.setPerson(personOptional.get());

        try {
            return ResponseEntity.ok().body(reservaRepository.save(reservaToUpdate));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao atualizar reserva");
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

        Optional<Reserva> reservaOptional = reservaRepository.findById(uuid);

        if (reservaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            reservaRepository.delete(reservaOptional.get());
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao excluir reserva");
        }
    }
}
