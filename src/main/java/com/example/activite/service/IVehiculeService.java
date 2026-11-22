package com.example.activite.service;
import com.example.activite.domain.Vehicule;
import java.util.List;
public interface IVehiculeService {
    Vehicule create(Vehicule vehicule);
    Vehicule findById(Long id);
    List<Vehicule> findAll();
    Vehicule update(Long id, Vehicule vehicule);
    void deleteById(Long id);
    List<com.example.activite.web.dto.VehiculeRechercheResponse> findVehiculesByContrat(Long contratId);
    List<com.example.activite.web.dto.VehiculeRechercheResponse> findVehiculesByMaintenance(Long maintenanceId);
}
