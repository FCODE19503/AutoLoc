package com.example.activite.web.dto;

import com.example.activite.domain.Maintenance;
import java.time.LocalDate;

public record MaintenanceRechercheResponse(Long idMaintenance, LocalDate dateDebut,
        LocalDate dateFin, String description, Long idVehicule) {
    public static MaintenanceRechercheResponse from(Maintenance m) {
        return new MaintenanceRechercheResponse(m.getIdMaintenance(), m.getDateDebut(), m.getDateFin(),
                m.getDescription(), m.getVehicule() == null ? null : m.getVehicule().getIdVehicule());
    }
}
