package com.example.activite.service;
import com.example.activite.domain.Client;
import com.example.activite.exception.ResourceNotFoundException;
import com.example.activite.repository.IClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service @RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {
    private final IClientRepository clientRepository;
    public Client create(Client c) { if(c.getIdClient()!=null) throw new IllegalArgumentException("Un nouveau client ne doit pas avoir d'identifiant."); validate(c); return clientRepository.save(c); }
    public Client findById(Long id) { return clientRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Client",id)); }
    public List<Client> findAll() { return clientRepository.findAll(); }
    public Client update(Long id, Client c) { validate(c); Client x=findById(id); x.setNom(c.getNom()); x.setPrenom(c.getPrenom()); x.setEmail(c.getEmail()); x.setTelephone(c.getTelephone()); x.setNumPermis(c.getNumPermis()); x.setDateInscription(c.getDateInscription()); return clientRepository.save(x); }
    public void deleteById(Long id) { if(!clientRepository.existsById(id)) throw new ResourceNotFoundException("Client",id); clientRepository.deleteById(id); }
    private void validate(Client c) { ServiceValidation.requireNotNull(c,"client"); ServiceValidation.requireText(c.getNom(),"nom"); ServiceValidation.requireText(c.getEmail(),"email"); ServiceValidation.requireText(c.getPrenom(),"prenom"); ServiceValidation.requireText(c.getTelephone(),"telephone"); ServiceValidation.requireText(c.getNumPermis(),"numPermis"); ServiceValidation.requireNotNull(c.getDateInscription(),"dateInscription"); }
}
