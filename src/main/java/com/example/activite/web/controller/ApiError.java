package com.example.activite.web.controller;

import java.time.Instant;

public record ApiError(int statut, String message, Instant timestamp) {
}
