package com.farhan.staradmin.service;

import com.farhan.staradmin.entity.Algorithme;
import com.farhan.staradmin.repository.AlgorithmeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlgorithmeService {

    private final AlgorithmeRepository algorithmeRepository;

    @Autowired
    public AlgorithmeService(AlgorithmeRepository algorithmeRepository) {
        this.algorithmeRepository = algorithmeRepository;
    }

    public List<Algorithme> getAllAlgorithmes() {
        return algorithmeRepository.findAll();
    }

    public Algorithme getAlgorithmeById(int id) {
        return algorithmeRepository.findById(id).orElse(null);
    }

    public Algorithme saveAlgorithme(Algorithme algorithme) {
        return algorithmeRepository.save(algorithme);
    }

    public void deleteAlgorithme(int id) {
        algorithmeRepository.deleteById(id);
    }
}
