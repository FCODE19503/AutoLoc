package com.example.activite.service;
import com.example.activite.domain.Equipement;
import java.util.List;
public interface IEquipementService { Equipement create(Equipement equipement); Equipement findById(Long id); List<Equipement> findAll(); Equipement update(Long id, Equipement equipement); void deleteById(Long id); }
