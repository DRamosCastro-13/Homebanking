package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController // para que sepa que es un controlador
@RequestMapping ("/api/clients") //ruta a la que le va a hacer el control. Asociada a la petición
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/all") //Escucha un get
    public List<ClientDTO> getAllClients(){
        return clientRepository.findAll()
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(Collectors.toList());
    }

    @RequestMapping("/{id}")
    public ClientDTO getOneClient(@PathVariable Long id){
        return new ClientDTO(clientRepository.findById(id).orElse(null));
    }

}
