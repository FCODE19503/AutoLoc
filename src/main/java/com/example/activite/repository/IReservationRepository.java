package com.example.activite.repository;
import com.example.activite.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IReservationRepository extends JpaRepository<Reservation, Long> { }
