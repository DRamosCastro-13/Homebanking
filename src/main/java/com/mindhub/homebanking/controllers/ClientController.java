package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController // para que sepa que es un controlador
@RequestMapping ("/api/clients") //ruta a la que le va a hacer el control. Asociada a la petición
public class ClientController {

    @Autowired //Para evitar crear un constructor, similar a generar una instancia. Crea una inyección de dependdencia ya que las interfaces no se pueden instanciar
    private ClientRepository clientRepository;

    @Autowired //public?
    public PasswordEncoder passwordEncoder;

    @PostMapping//Mapea métodos POST lo que esté después de @RequestParam va a ser una query param (?...) en la ruta a la que se hace la petición de tipo POST
    public ResponseEntity<?> createClient(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password)
    {

        if(firstName.isBlank() && lastName.isBlank() && email.isBlank() && password.isBlank()){
            return new ResponseEntity<>("You must fill out the form to proceed", HttpStatus.FORBIDDEN);
        }

        if(firstName.isBlank()){
            return new ResponseEntity<>("You must fill out your First Name", HttpStatus.FORBIDDEN);
        } //Verifica que el formulario enviado no esté vacío o con un espacio en blanco

        if(lastName.isBlank()){
            return new ResponseEntity<>("You must fill out your Last Name", HttpStatus.FORBIDDEN);
        } //Se hace por cada parámetro para tener más control

        if(email.isBlank()){
            return new ResponseEntity<>("You must fill out your Email", HttpStatus.FORBIDDEN);
        }

        if(password.isBlank()){
            return new ResponseEntity<>("You must fill out the password", HttpStatus.FORBIDDEN);
        }
        //Estos errores van a llegar al catch dentro del axios para ser capturados

//        if(clientRepository.findByEmail(email) != null){
//            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
//        } // este método traería TODA la info del client, el objeto completo,
//         para hacer la verificación por esto es mejor usar un booleano definido en el clientRepository

        if(clientRepository.existsByEmail(email)){
            return new ResponseEntity<>("Email already in use, please login or set up your account with a different email", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));

        clientRepository.save(client);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/current") //El authentication tiene la cookie que contiene la info de la sesión
    public ResponseEntity<ClientDTO> getOneClient(Authentication authentication){

        Client client = clientRepository.findByEmail(authentication.getName()); //Obtiene el mail con el cual el client está loggeado, solo que para spring security es el nombre de usuario de la sesión

        return new ResponseEntity<>(new ClientDTO(client),HttpStatus.OK);
    }

    @RequestMapping("/all") //Escucha un get
    public Set<ClientDTO> getAllClients(){
        return clientRepository.findAll()
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(Collectors.toSet());
    }

    @RequestMapping("/{id}")
    public ClientDTO getOneClient(@PathVariable Long id){
        return new ClientDTO(clientRepository.findById(id).orElse(null));
    }

}
