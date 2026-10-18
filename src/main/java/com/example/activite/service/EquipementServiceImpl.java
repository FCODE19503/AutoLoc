package com.example.activite.service;
import com.example.activite.domain.Equipement;
import com.example.activite.exception.ResourceNotFoundException;
import com.example.activite.repository.IEquipementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service @RequiredArgsConstructor
public class EquipementServiceImpl implements IEquipementService {
    private final IEquipementRepository equipementRepository;
    public Equipement create(Equipement e) { if(e.getIdEquipement()!=null) throw new IllegalArgumentException("Un nouvel équipement ne doit pas avoir d'identifiant."); validate(e); return equipementRepository.save(e); }
    public Equipement findById(Long id) { return equipementRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Équipement",id)); }
    public List<Equipement> findAll() { return equipementRepository.findAll(); }
    public Equipement update(Long id, Equipement e) { validate(e); Equipement x=findById(id); x.setLibelle(e.getLibelle()); x.setVehicules(e.getVehicules()); return equipementRepository.save(x); }
    public void deleteById(Long id) { if(!equipementRepository.existsById(id)) throw new ResourceNotFoundException("Équipement",id); equipementRepository.deleteById(id); }
    private void validate(Equipement e) { ServiceValidation.requireNotNull(e,"équipement"); ServiceValidation.requireText(e.getLibelle(),"libelle"); }
}
