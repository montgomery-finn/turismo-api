package br.edu.utfp.turismoapi.controllers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfp.turismoapi.dto.AuthDTO;
import br.edu.utfp.turismoapi.security.JwtUtil;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
 
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<Object> auth(@Valid @RequestBody AuthDTO authDTO) {

        System.out.println(authDTO);

        var payload = new HashMap<String, Object>();
        payload.put("username", authDTO.username);

        var now = Instant.now();

        var token = jwtUtil.generateToken(payload, "1234", 3600);

        var res = new HashMap<String, Object>();
        res.put("token", token);
        res.put("issuedIn", now);        
        res.put("expiresIn", now.plus(3600, ChronoUnit.SECONDS));

        return ResponseEntity.ok().body(res);
    }

}
