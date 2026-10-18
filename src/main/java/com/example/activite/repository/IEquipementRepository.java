package com.example.activite.repository;
import com.example.activite.domain.Equipement;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IEquipementRepository extends JpaRepository<Equipement, Long> { }
