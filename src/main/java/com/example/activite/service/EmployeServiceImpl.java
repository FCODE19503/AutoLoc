package com.example.activite.service;
import com.example.activite.domain.Employe;
import com.example.activite.exception.ResourceNotFoundException;
import com.example.activite.repository.IEmployeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service @RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {
    private final IEmployeRepository employeRepository;
    public Employe create(Employe e) { if(e.getIdEmploye()!=null) throw new IllegalArgumentException("Un nouvel employé ne doit pas avoir d'identifiant."); validate(e); return employeRepository.save(e); }
    public Employe findById(Long id) { return employeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employé",id)); }
    public List<Employe> findAll() { return employeRepository.findAll(); }
    public Employe update(Long id, Employe e) { validate(e); Employe x=findById(id); x.setNom(e.getNom()); x.setPrenom(e.getPrenom()); x.setRole(e.getRole()); x.setAgence(e.getAgence()); return employeRepository.save(x); }
    public void deleteById(Long id) { if(!employeRepository.existsById(id)) throw new ResourceNotFoundException("Employé",id); employeRepository.deleteById(id); }
    private void validate(Employe e) { ServiceValidation.requireNotNull(e,"employé"); ServiceValidation.requireText(e.getNom(),"nom"); ServiceValidation.requireText(e.getPrenom(),"prenom"); ServiceValidation.requireNotNull(e.getRole(),"role"); }
}
