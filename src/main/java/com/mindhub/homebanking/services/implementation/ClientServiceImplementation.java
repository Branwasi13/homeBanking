package com.mindhub.homebanking.services.implementation;


import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ClientServiceImplementation implements ClientService{
    @Autowired
    private ClientRepository clientRepository;
    @Override
    public List<Client> getAllClients(){
        return clientRepository.findAll();
    }

    @Override
    public Client getClientById(long id){
        return clientRepository.findById(id).get();
    }

    @Override
    public Client getClientByEmail(String email){
        return  clientRepository.findByEmail(email);
    }

    @Override
    public void saveClient(Client client){
        clientRepository.save(client);
    }
}
