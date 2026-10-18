package com.example.activite.service;
import com.example.activite.domain.Contrat;
import com.example.activite.exception.ResourceNotFoundException;
import com.example.activite.repository.IContratRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service @RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {
    private final IContratRepository contratRepository;
    public Contrat create(Contrat c) { if(c.getIdContrat()!=null) throw new IllegalArgumentException("Un nouveau contrat ne doit pas avoir d'identifiant."); validate(c); return contratRepository.save(c); }
    public Contrat findById(Long id) { return contratRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Contrat",id)); }
    public List<Contrat> findAll() { return contratRepository.findAll(); }
    public Contrat update(Long id, Contrat c) { validate(c); Contrat x=findById(id); x.setDateSignature(c.getDateSignature()); x.setMontantTotal(c.getMontantTotal()); x.setValide(c.isValide()); x.setReservation(c.getReservation()); return contratRepository.save(x); }
    public void deleteById(Long id) { if(!contratRepository.existsById(id)) throw new ResourceNotFoundException("Contrat",id); contratRepository.deleteById(id); }
    private void validate(Contrat c) { ServiceValidation.requireNotNull(c,"contrat"); ServiceValidation.requireNotNull(c.getDateSignature(),"dateSignature"); ServiceValidation.requireNotNull(c.getMontantTotal(),"montantTotal"); if(c.getMontantTotal().signum()<0) throw new IllegalArgumentException("Le montant total ne peut pas être négatif."); }
}
