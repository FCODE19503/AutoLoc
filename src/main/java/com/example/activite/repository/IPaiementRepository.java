package com.example.activite.repository;
import com.example.activite.domain.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IPaiementRepository extends JpaRepository<Paiement, Long> { }
