package com.example.activite.repository;
import com.example.activite.domain.Contrat;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IContratRepository extends JpaRepository<Contrat, Long> { }
