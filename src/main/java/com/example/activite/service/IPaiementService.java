package com.example.activite.service;
import com.example.activite.domain.Paiement;
import java.util.List;
public interface IPaiementService { Paiement findById(Long id); List<Paiement> findAll(); }
