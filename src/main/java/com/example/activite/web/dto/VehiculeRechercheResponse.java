package com.example.activite.web.dto;

import com.example.activite.domain.Vehicule;
import com.example.activite.domain.CategorieVehicule;
import com.example.activite.domain.StatutVehicule;
import java.math.BigDecimal;

public record VehiculeRechercheResponse(Long idVehicule, String immatriculation, String marque,
        String modele, CategorieVehicule categorie, BigDecimal tarifJournalier, StatutVehicule statut) {
    public static VehiculeRechercheResponse from(Vehicule v) {
        return new VehiculeRechercheResponse(v.getIdVehicule(), v.getImmatriculation(), v.getMarque(),
                v.getModele(), v.getCategorie(), v.getTarifJournalier(), v.getStatut());
    }
}
