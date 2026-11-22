package com.example.activite.service;
import com.example.activite.domain.Vehicule;
import com.example.activite.exception.ResourceNotFoundException;
import com.example.activite.repository.IVehiculeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service @RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {
    private final IVehiculeRepository vehiculeRepository;
    private final com.example.activite.repository.IContratRepository contratRepository;
    private final com.example.activite.repository.IMaintenanceRepository maintenanceRepository;

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<com.example.activite.web.dto.VehiculeRechercheResponse> findVehiculesByContrat(Long contratId) {
        if (!contratRepository.existsById(contratId)) throw new ResourceNotFoundException("Contrat", contratId);
        return vehiculeRepository.findDistinctByReservations_Contrat_IdContrat(contratId).stream()
                .map(com.example.activite.web.dto.VehiculeRechercheResponse::from).toList();
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<com.example.activite.web.dto.VehiculeRechercheResponse> findVehiculesByMaintenance(Long maintenanceId) {
        if (!maintenanceRepository.existsById(maintenanceId)) throw new ResourceNotFoundException("Maintenance", maintenanceId);
        return vehiculeRepository.findDistinctByMaintenances_IdMaintenance(maintenanceId).stream()
                .map(com.example.activite.web.dto.VehiculeRechercheResponse::from).toList();
    }
    public Vehicule create(Vehicule v) { if(v.getIdVehicule()!=null) throw new IllegalArgumentException("Un nouveau véhicule ne doit pas avoir d'identifiant."); validate(v); return vehiculeRepository.save(v); }
    public Vehicule findById(Long id) { return vehiculeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Véhicule",id)); }
    public List<Vehicule> findAll() { return vehiculeRepository.findAll(); }
    public Vehicule update(Long id, Vehicule v) { validate(v); Vehicule x=findById(id); x.setImmatriculation(v.getImmatriculation()); x.setMarque(v.getMarque()); x.setModele(v.getModele()); x.setCategorie(v.getCategorie()); x.setTarifJournalier(v.getTarifJournalier()); x.setStatut(v.getStatut()); x.setAgence(v.getAgence()); x.setEquipements(v.getEquipements()); return vehiculeRepository.save(x); }
    public void deleteById(Long id) { if(!vehiculeRepository.existsById(id)) throw new ResourceNotFoundException("Véhicule",id); vehiculeRepository.deleteById(id); }
    private void validate(Vehicule v) { ServiceValidation.requireNotNull(v,"véhicule"); ServiceValidation.requireText(v.getImmatriculation(),"immatriculation"); ServiceValidation.requireText(v.getMarque(),"marque"); ServiceValidation.requireText(v.getModele(),"modele"); ServiceValidation.requireNotNull(v.getCategorie(),"categorie"); ServiceValidation.requireNotNull(v.getStatut(),"statut"); ServiceValidation.requireNotNull(v.getTarifJournalier(),"tarifJournalier"); if(v.getTarifJournalier().signum()<0) throw new IllegalArgumentException("Le tarif journalier ne peut pas être négatif."); }
}
