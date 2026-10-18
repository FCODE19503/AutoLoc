package com.example.activite.service;
import com.example.activite.domain.Reservation;
import com.example.activite.exception.ResourceNotFoundException;
import com.example.activite.repository.IReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service @RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {
    private final IReservationRepository reservationRepository;
    public Reservation create(Reservation r) { if(r.getIdReservation()!=null) throw new IllegalArgumentException("Une nouvelle réservation ne doit pas avoir d'identifiant."); validate(r); return reservationRepository.save(r); }
    public Reservation findById(Long id) { return reservationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Réservation",id)); }
    public List<Reservation> findAll() { return reservationRepository.findAll(); }
    public Reservation update(Long id, Reservation r) { validate(r); Reservation x=findById(id); x.setDateDebut(r.getDateDebut()); x.setDateFin(r.getDateFin()); x.setStatut(r.getStatut()); x.setVehicule(r.getVehicule()); x.setClient(r.getClient()); return reservationRepository.save(x); }
    public void deleteById(Long id) { if(!reservationRepository.existsById(id)) throw new ResourceNotFoundException("Réservation",id); reservationRepository.deleteById(id); }
    private void validate(Reservation r) { ServiceValidation.requireNotNull(r,"réservation"); ServiceValidation.requireNotNull(r.getDateDebut(),"dateDebut"); ServiceValidation.requireNotNull(r.getDateFin(),"dateFin"); ServiceValidation.requireNotNull(r.getStatut(),"statut"); if(r.getDateFin().isBefore(r.getDateDebut())) throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début."); }
}
