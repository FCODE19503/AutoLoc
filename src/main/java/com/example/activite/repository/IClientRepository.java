package com.example.activite.repository;
import com.example.activite.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IClientRepository extends JpaRepository<Client, Long> { }
