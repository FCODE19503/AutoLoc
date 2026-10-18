package com.example.activite.service;
import com.example.activite.domain.Agence;
import com.example.activite.exception.ResourceNotFoundException;
import com.example.activite.repository.IAgenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service @RequiredArgsConstructor
public class AgenceServiceImpl implements IAgenceService {
    private final IAgenceRepository agenceRepository;
    public Agence create(Agence a) { if (a.getIdAgence()!=null) throw new IllegalArgumentException("Une nouvelle agence ne doit pas avoir d'identifiant."); validate(a); return agenceRepository.save(a); }
    public Agence findById(Long id) { return agenceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Agence", id)); }
    public List<Agence> findAll() { return agenceRepository.findAll(); }
    public Agence update(Long id, Agence a) { validate(a); Agence e=findById(id); e.setNom(a.getNom()); e.setVille(a.getVille()); e.setAdresse(a.getAdresse()); e.setTelephone(a.getTelephone()); return agenceRepository.save(e); }
    public void deleteById(Long id) { if(!agenceRepository.existsById(id)) throw new ResourceNotFoundException("Agence", id); agenceRepository.deleteById(id); }
    private void validate(Agence a) { ServiceValidation.requireNotNull(a,"agence"); ServiceValidation.requireText(a.getNom(),"nom"); ServiceValidation.requireText(a.getVille(),"ville"); ServiceValidation.requireText(a.getAdresse(),"adresse"); ServiceValidation.requireText(a.getTelephone(),"telephone"); }
}
