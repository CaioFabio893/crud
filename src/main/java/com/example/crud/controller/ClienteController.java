package com.example.crud.controller;

import com.example.crud.dto.ClienteDTO;
import com.example.crud.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;


    // GET - Buscar todos
    // Retorna 200 OK
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> findAll() {

        List<ClienteDTO> list = service.findAll();

        return ResponseEntity.ok().body(list);
    }


    // GET - Buscar por ID
    // Retorna 200 OK
    @GetMapping(value = "/{id}")
    public ResponseEntity<ClienteDTO> findById(@PathVariable Long id) {

        ClienteDTO dto = service.findById(id);

        return ResponseEntity.ok().body(dto);
    }


    // POST - Cadastrar cliente
    // Retorna 201 Created
    @PostMapping
    public ResponseEntity<ClienteDTO> insert(
            @Valid @RequestBody ClienteDTO dto) {

        dto = service.insert(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(dto);
    }


    // PUT - Atualizar cliente
    // Retorna 200 OK
    @PutMapping(value = "/{id}")
    public ResponseEntity<ClienteDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ClienteDTO dto) {

        dto = service.update(id, dto);

        return ResponseEntity.ok().body(dto);
    }


    // DELETE - Deletar cliente
    // Retorna 204 No Content
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
