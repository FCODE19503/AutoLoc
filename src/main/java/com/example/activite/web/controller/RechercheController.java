package com.example.activite.web.controller;

import com.example.activite.service.IContratService;
import com.example.activite.service.IMaintenanceService;
import com.example.activite.service.IVehiculeService;
import com.example.activite.web.dto.ContratRechercheResponse;
import com.example.activite.web.dto.MaintenanceRechercheResponse;
import com.example.activite.web.dto.VehiculeRechercheResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Recherches KeyWords", description = "Recherches dérivées Spring Data JPA")
public class RechercheController {
    private final IVehiculeService vehiculeService;
    private final IContratService contratService;
    private final IMaintenanceService maintenanceService;

    @GetMapping("/contrats/{contratId}/vehicules")
    @Operation(summary = "Véhicules par contrat (zéro ou un)")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Liste retournée, éventuellement vide"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource source introuvable")})
    public List<VehiculeRechercheResponse> findVehiculesByContrat(
            @Parameter(description = "Identifiant strictement positif", required = true)
            @PathVariable @Positive Long contratId) {
        return vehiculeService.findVehiculesByContrat(contratId);
    }

    @GetMapping("/vehicules/{vehiculeId}/contrats")
    @Operation(summary = "Contrats par véhicule")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Liste retournée, éventuellement vide"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource source introuvable")})
    public List<ContratRechercheResponse> findContratsByVehicule(
            @Parameter(description = "Identifiant strictement positif", required = true)
            @PathVariable @Positive Long vehiculeId) {
        return contratService.findContratsByVehicule(vehiculeId);
    }

    @GetMapping("/vehicules/{vehiculeId}/maintenances")
    @Operation(summary = "Maintenances par véhicule")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Liste retournée, éventuellement vide"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource source introuvable")})
    public List<MaintenanceRechercheResponse> findMaintenancesByVehicule(
            @Parameter(description = "Identifiant strictement positif", required = true)
            @PathVariable @Positive Long vehiculeId) {
        return maintenanceService.findMaintenancesByVehicule(vehiculeId);
    }

    @GetMapping("/maintenances/{maintenanceId}/vehicules")
    @Operation(summary = "Véhicules par maintenance (zéro ou un)")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Liste retournée, éventuellement vide"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource source introuvable")})
    public List<VehiculeRechercheResponse> findVehiculesByMaintenance(
            @Parameter(description = "Identifiant strictement positif", required = true)
            @PathVariable @Positive Long maintenanceId) {
        return vehiculeService.findVehiculesByMaintenance(maintenanceId);
    }
}
