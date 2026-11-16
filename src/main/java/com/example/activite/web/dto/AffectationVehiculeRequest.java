package com.example.activite.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AffectationVehiculeRequest(
        @NotNull(message = "vehiculeId est obligatoire")
        @Positive(message = "vehiculeId doit être positif") Long vehiculeId) { }
