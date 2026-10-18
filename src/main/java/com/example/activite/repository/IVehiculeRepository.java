package com.example.activite.repository;
import com.example.activite.domain.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IVehiculeRepository extends JpaRepository<Vehicule, Long> { }
