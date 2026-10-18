package com.example.activite.repository;
import com.example.activite.domain.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IEmployeRepository extends JpaRepository<Employe, Long> { }
