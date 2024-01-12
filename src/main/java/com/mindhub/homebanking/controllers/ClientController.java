package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.dto.NewClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController // para que sepa que es un controlador
@RequestMapping ("/api/clients") //ruta a la que le va a hacer el control. Asociada a la petición
public class ClientController {

    @Autowired //Para evitar crear un constructor, similar a generar una instancia. Crea una inyección de dependdencia ya que las interfaces no se pueden instanciar
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired //public?
    public PasswordEncoder passwordEncoder;

    @PostMapping//Mapea métodos POST lo que esté después de @RequestParam va a ser una query param (?...) en la ruta a la que se hace la petición de tipo POST
    public ResponseEntity<?> createClient(
            @RequestBody NewClientDTO newClient)
    {

        if(newClient.firstName().isBlank() && newClient.lastName().isBlank() && newClient.email().isBlank() && newClient.password().isBlank()){
            return new ResponseEntity<>("You must fill out the form", HttpStatus.FORBIDDEN);
        }

        if(newClient.firstName().isBlank()){
            return new ResponseEntity<>("You must fill your First Name", HttpStatus.FORBIDDEN);
        } //Verifica que el formulario enviado no esté vacío o con un espacio en blanco

        if(newClient.lastName().isBlank()){
            return new ResponseEntity<>("You must fill your Last Name", HttpStatus.FORBIDDEN);
        } //Se hace por cada parámetro para tener más control

        if(newClient.email().isBlank()){
            return new ResponseEntity<>("You must fill your Email", HttpStatus.FORBIDDEN);
        }

        if(newClient.password().isBlank()){
            return new ResponseEntity<>("You must fill the password", HttpStatus.FORBIDDEN);
        }
        //Estos errores van a llegar al catch dentro del axios para ser capturados

//        if(clientRepository.findByEmail(email) != null){
//            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
//        } // este método traería TODA la info del client, el objeto completo,
//         para hacer la verificación por esto es mejor usar un booleano definido en el clientRepository

        if(clientService.getAuthenticatedClient(newClient.email()) != null){
            return new ResponseEntity<>("This email is already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(newClient.firstName().substring(0,1).toUpperCase() + newClient.firstName().substring(1).toLowerCase(),
                newClient.lastName().substring(0,1).toUpperCase() + newClient.lastName().substring(1).toLowerCase(),
                newClient.email(), passwordEncoder.encode(newClient.password()));

        clientService.saveClient(client);

        if(client.getAccounts().isEmpty()){

            String number;

            do {
                number = "VIN-" + getRandomNumber(100000, 99999999);
            } while (accountService.existsByNumber(number));

            Account account = new Account(number, LocalDate.now(), 0.0);
            client.addAccount(account);
            accountService.saveAccount(account);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/current") //El authentication tiene la cookie que contiene la info de la sesión
    public ResponseEntity<ClientDTO> getAuthenticatedClient(Authentication authentication){

        return new ResponseEntity<>(clientService.getAuthenticatedClientDTO(authentication.getName()),HttpStatus.OK);
    }

    @GetMapping("/all") //Escucha un get
    public List<ClientDTO> getAllClients(){
        return clientService.getAllClientDTO();
    }

    @GetMapping("{id}")
    public ClientDTO getOneClientById(@PathVariable Long id){
        return clientService.getOneClientDTOById(id);
    }


    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


}
