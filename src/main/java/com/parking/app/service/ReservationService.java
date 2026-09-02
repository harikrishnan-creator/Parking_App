package com.parking.app.service;

import com.parking.app.entity.Reservation;
import com.parking.app.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository repository;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    public List<Reservation> findAll() {
        return repository.findAll();
    }

    public Reservation create(Reservation r) {
        return repository.save(r);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
