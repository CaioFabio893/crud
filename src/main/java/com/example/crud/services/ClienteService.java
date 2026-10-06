package com.example.crud.services;

import com.example.crud.dto.ClienteDTO;
import com.example.crud.entities.Cliente;
import com.example.crud.repositories.ClienteRepository;
import com.example.crud.services.exceptions.DatabaseException;
import com.example.crud.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;


    // Buscar todos os clientes
    @Transactional(readOnly = true)
    public List<ClienteDTO> findAll() {

        List<Cliente> list = repository.findAll();

        return list.stream()
                .map(x -> new ClienteDTO(x))
                .toList();
    }


    // Buscar cliente por ID
    @Transactional(readOnly = true)
    public ClienteDTO findById(Long id) {

        Optional<Cliente> obj = repository.findById(id);

        Cliente entity = obj.orElseThrow(
                () -> new ResourceNotFoundException("Cliente não encontrado")
        );

        return new ClienteDTO(entity);
    }


    // Inserir cliente
    @Transactional
    public ClienteDTO insert(ClienteDTO dto) {

        Cliente entity = new Cliente();

        copyDtoToEntity(dto, entity);

        entity = repository.save(entity);

        return new ClienteDTO(entity);
    }


    // Atualizar cliente
    @Transactional
    public ClienteDTO update(Long id, ClienteDTO dto) {

        try {

            Cliente entity = repository.getReferenceById(id);

            copyDtoToEntity(dto, entity);

            entity = repository.save(entity);

            return new ClienteDTO(entity);

        } catch (EntityNotFoundException e) {

            throw new ResourceNotFoundException(
                    "Cliente não encontrado. Id: " + id
            );
        }
    }


    // Deletar cliente
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Cliente não encontrado. Id: " + id
            );
        }

        try {

            repository.deleteById(id);

        } catch (DataIntegrityViolationException e) {

            throw new DatabaseException(
                    "Falha de integridade referencial"
            );
        }
    }


    // Copia os dados do DTO para a entidade
    private void copyDtoToEntity(ClienteDTO dto, Cliente entity) {

        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
    }
}