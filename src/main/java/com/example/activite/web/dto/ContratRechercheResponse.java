package com.example.activite.web.dto;

import com.example.activite.domain.Contrat;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratRechercheResponse(Long idContrat, LocalDate dateSignature,
        BigDecimal montantTotal, boolean valide, Long idReservation) {
    public static ContratRechercheResponse from(Contrat c) {
        return new ContratRechercheResponse(c.getIdContrat(), c.getDateSignature(), c.getMontantTotal(),
                c.isValide(), c.getReservation() == null ? null : c.getReservation().getIdReservation());
    }
}
