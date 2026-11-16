package com.example.activite.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AffectationEquipementRequest(
        @NotNull(message = "equipementId est obligatoire")
        @Positive(message = "equipementId doit être positif") Long equipementId) { }
