package br.edu.utfp.turismoapi.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.utfp.turismoapi.dto.PagamentoDTO;
import br.edu.utfp.turismoapi.models.Pagamento;
import br.edu.utfp.turismoapi.models.Reserva;
import br.edu.utfp.turismoapi.repositories.PagamentoRepository;
import br.edu.utfp.turismoapi.repositories.ReservaRepository;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/pagamento")
public class PagamentoController {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping("")
    public List<Pagamento> getAll() {
        return pagamentoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        Optional<Pagamento> pagamentoOptional = pagamentoRepository.findById(UUID.fromString(id));

        return pagamentoOptional.isPresent()
                ? ResponseEntity.ok(pagamentoOptional.get())
                : ResponseEntity.notFound().build();
    }

    @PostMapping("")
    public ResponseEntity<Object> create(@RequestBody Pagamento pagamento) {
        // Certifique-se de que a reserva associada ao pagamento existe
        Optional<Reserva> reservaOptional = reservaRepository.findById(pagamento.getReserva().getId());
        if (reservaOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Reserva não encontrada");
        }

        // Defina a reserva associada ao pagamento
        pagamento.setReserva(reservaOptional.get());

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoRepository.save(pagamento));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao criar pagamento");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody PagamentoDTO pagamentoDTO) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("Formato de UUID inválido");
        }

        Optional<Pagamento> pagamentoOptional = pagamentoRepository.findById(uuid);

        if (pagamentoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pagamento pagamentoToUpdate = pagamentoOptional.get();
        BeanUtils.copyProperties(pagamentoDTO, pagamentoToUpdate);
        pagamentoToUpdate.setUpdatedAt(LocalDateTime.now());

        // Certifique-se de que a reserva associada ao pagamento existe
        Optional<Reserva> reservaOptional = reservaRepository.findById(pagamentoToUpdate.getReserva().getId());
        if (reservaOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Reserva não encontrada");
        }

        try {
            return ResponseEntity.ok().body(pagamentoRepository.save(pagamentoToUpdate));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao atualizar pagamento");
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

        Optional<Pagamento> pagamentoOptional = pagamentoRepository.findById(uuid);

        if (pagamentoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            pagamentoRepository.delete(pagamentoOptional.get());
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao excluir pagamento");
        }
    }
}
