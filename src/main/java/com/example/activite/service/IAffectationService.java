package com.example.activite.service;

import com.example.activite.web.dto.MaintenanceVehiculeResponse;
import com.example.activite.web.dto.VehiculeEquipementResponse;

public interface IAffectationService {
    MaintenanceVehiculeResponse affecterMaintenance(Long maintenanceId, Long vehiculeId);
    VehiculeEquipementResponse affecterEquipement(Long vehiculeId, Long equipementId);
}
