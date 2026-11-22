package com.example.activite.repository;
import com.example.activite.domain.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IVehiculeRepository extends JpaRepository<Vehicule, Long> {
    java.util.List<Vehicule> findDistinctByReservations_Contrat_IdContrat(Long contratId);
    java.util.List<Vehicule> findDistinctByMaintenances_IdMaintenance(Long maintenanceId);
    // MySQL and XAMPP MariaDB both support this locking syntax.
    @org.springframework.data.jpa.repository.Query(value = "select * from vehicule where id_vehicule = :id for update", nativeQuery = true)
    java.util.Optional<Vehicule> findForAssignment(@org.springframework.data.repository.query.Param("id") Long id);
}
