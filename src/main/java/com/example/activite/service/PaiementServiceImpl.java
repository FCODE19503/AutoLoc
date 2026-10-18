package com.example.activite.service;
import com.example.activite.domain.Paiement;
import com.example.activite.exception.ResourceNotFoundException;
import com.example.activite.repository.IPaiementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service @RequiredArgsConstructor
public class PaiementServiceImpl implements IPaiementService {
    private final IPaiementRepository paiementRepository;
    public Paiement findById(Long id) { return paiementRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Paiement",id)); }
    public List<Paiement> findAll() { return paiementRepository.findAll(); }
}
