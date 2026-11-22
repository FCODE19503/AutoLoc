package com.example.activite.repository;
import com.example.activite.domain.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IMaintenanceRepository extends JpaRepository<Maintenance, Long> {
    java.util.List<Maintenance> findByVehicule_IdVehicule(Long vehiculeId);
}
