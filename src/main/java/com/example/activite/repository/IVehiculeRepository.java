package com.example.activite.repository;
import com.example.activite.domain.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface IVehiculeRepository extends JpaRepository<Vehicule, Long> {
    java.util.List<Vehicule> findDistinctByReservations_Contrat_IdContrat(Long contratId);
    @Query("""
        SELECT DISTINCT v
        FROM Vehicule v
        JOIN v.maintenances m
        WHERE m.idMaintenance = :maintenanceId
        """)
    java.util.List<Vehicule> findVehiculesByMaintenanceJPQL(
            @Param("maintenanceId") Long maintenanceId);
    // MySQL and XAMPP MariaDB both support this locking syntax.
    @org.springframework.data.jpa.repository.Query(value = "select * from vehicule where id_vehicule = :id for update", nativeQuery = true)
    java.util.Optional<Vehicule> findForAssignment(@org.springframework.data.repository.query.Param("id") Long id);
}
