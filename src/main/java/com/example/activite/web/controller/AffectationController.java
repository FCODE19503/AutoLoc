package com.example.activite.web.controller;

import com.example.activite.service.IAffectationService;
import com.example.activite.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Affectations", description = "Associations maintenance, véhicule et équipement")
public class AffectationController {
    private final IAffectationService affectationService;

    @PutMapping("/maintenances/{maintenanceId}/vehicule")
    @Operation(summary = "Affecter une maintenance existante à un véhicule")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Affectation enregistrée"),
            @ApiResponse(responseCode = "400", description = "Identifiants invalides"),
            @ApiResponse(responseCode = "404", description = "Maintenance ou véhicule introuvable")})
    public MaintenanceVehiculeResponse affecterMaintenance(
            @Parameter(description = "Identifiant positif de la maintenance") @PathVariable @Positive Long maintenanceId,
            @Valid @RequestBody AffectationVehiculeRequest request) {
        return affectationService.affecterMaintenance(maintenanceId, request.vehiculeId());
    }

    @PutMapping("/vehicules/{vehiculeId}/equipements")
    @Operation(summary = "Associer un véhicule existant à un équipement, sans doublon")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Association enregistrée"),
            @ApiResponse(responseCode = "400", description = "Identifiants invalides"),
            @ApiResponse(responseCode = "404", description = "Véhicule ou équipement introuvable")})
    public VehiculeEquipementResponse affecterEquipement(
            @Parameter(description = "Identifiant positif du véhicule") @PathVariable @Positive Long vehiculeId,
            @Valid @RequestBody AffectationEquipementRequest request) {
        return affectationService.affecterEquipement(vehiculeId, request.equipementId());
    }
}
