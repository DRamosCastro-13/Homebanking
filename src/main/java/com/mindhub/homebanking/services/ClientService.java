package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;
import java.util.Set;

public interface ClientService {
    //permite repartir funciones y cumplir con la absracción
    List<ClientDTO> getAllClientDTO();

    List<Client> getAllClients();

    Client getAuthenticatedClient(String email);

    ClientDTO getAuthenticatedClientDTO(String email);

    Client getOneClientById(Long id);

    ClientDTO getOneClientDTOById(Long id);

    void saveClient(Client client);
}
