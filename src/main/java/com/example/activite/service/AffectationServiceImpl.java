package com.example.activite.service;

import com.example.activite.exception.ResourceNotFoundException;
import com.example.activite.repository.IEquipementRepository;
import com.example.activite.repository.IMaintenanceRepository;
import com.example.activite.repository.IVehiculeRepository;
import com.example.activite.web.dto.MaintenanceVehiculeResponse;
import com.example.activite.web.dto.VehiculeEquipementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AffectationServiceImpl implements IAffectationService {
    private final IMaintenanceRepository maintenanceRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IEquipementRepository equipementRepository;

    @Override
    @Transactional
    public MaintenanceVehiculeResponse affecterMaintenance(Long maintenanceId, Long vehiculeId) {
        var maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance", maintenanceId));
        var vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule", vehiculeId));
        maintenance.setVehicule(vehicule);
        maintenanceRepository.save(maintenance);
        return new MaintenanceVehiculeResponse(maintenance.getIdMaintenance(), maintenance.getDescription(),
                maintenance.getDateDebut(), maintenance.getDateFin(), vehicule.getIdVehicule(), vehicule.getImmatriculation());
    }

    @Override
    @Transactional
    public VehiculeEquipementResponse affecterEquipement(Long vehiculeId, Long equipementId) {
        // Serialize concurrent assignments to the same vehicle to avoid duplicate join rows.
        var vehicule = vehiculeRepository.findForAssignment(vehiculeId)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule", vehiculeId));
        var equipement = equipementRepository.findById(equipementId)
                .orElseThrow(() -> new ResourceNotFoundException("Équipement", equipementId));
        if (vehicule.getEquipements() == null) {
            vehicule.setEquipements(new ArrayList<>());
        }
        if (vehicule.getEquipements().stream().noneMatch(e -> equipementId.equals(e.getIdEquipement()))) {
            vehicule.getEquipements().add(equipement);
        }
        vehiculeRepository.save(vehicule);
        return new VehiculeEquipementResponse(vehicule.getIdVehicule(), vehicule.getImmatriculation(),
                equipement.getIdEquipement(), equipement.getLibelle());
    }
}
