package com.example.activite.service;
import com.example.activite.domain.Maintenance;
import com.example.activite.exception.ResourceNotFoundException;
import com.example.activite.repository.IMaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service @RequiredArgsConstructor
public class MaintenanceServiceImpl implements IMaintenanceService {
    private final IMaintenanceRepository maintenanceRepository;
    public Maintenance create(Maintenance m) { if(m.getIdMaintenance()!=null) throw new IllegalArgumentException("Une nouvelle maintenance ne doit pas avoir d'identifiant."); validate(m); return maintenanceRepository.save(m); }
    public Maintenance findById(Long id) { return maintenanceRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Maintenance",id)); }
    public List<Maintenance> findAll() { return maintenanceRepository.findAll(); }
    public Maintenance update(Long id, Maintenance m) { validate(m); Maintenance x=findById(id); x.setDateDebut(m.getDateDebut()); x.setDateFin(m.getDateFin()); x.setDescription(m.getDescription()); x.setVehicule(m.getVehicule()); return maintenanceRepository.save(x); }
    public void deleteById(Long id) { if(!maintenanceRepository.existsById(id)) throw new ResourceNotFoundException("Maintenance",id); maintenanceRepository.deleteById(id); }
    private void validate(Maintenance m) { ServiceValidation.requireNotNull(m,"maintenance"); ServiceValidation.requireNotNull(m.getDateDebut(),"dateDebut"); ServiceValidation.requireNotNull(m.getDateFin(),"dateFin"); ServiceValidation.requireText(m.getDescription(),"description"); if(m.getDateFin().isBefore(m.getDateDebut())) throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début."); }
}
