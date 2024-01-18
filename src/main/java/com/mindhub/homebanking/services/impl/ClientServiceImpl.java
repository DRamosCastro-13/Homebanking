package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
//Esta clase implementa los métodos de la interfaz ClientService. Con esto se puede hacer el @Autowired
//en el controlador ya que se le está indicando a Spring que está en su contexto
@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public List<ClientDTO> getAllClientDTO() {
        return getAllClients().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }


    @Override
    public Client getAuthenticatedClient(String email) {
        return clientRepository.findByEmail(email);
    }


    @Override
    public ClientDTO getAuthenticatedClientDTO(String email) {
        return new ClientDTO(getAuthenticatedClient(email));
    }

    @Override
    public Boolean existsByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }

    @Override
    public Client getOneClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public ClientDTO getOneClientDTOById(Long id) {
        return new ClientDTO(getOneClientById(id));
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client); //Devuelve el client como entity
    }


}
