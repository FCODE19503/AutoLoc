package com.example.activite.web.dto;

import java.time.LocalDate;

public record MaintenanceVehiculeResponse(Long maintenanceId, String description,
        LocalDate dateDebut, LocalDate dateFin, Long vehiculeId, String immatriculation) { }
