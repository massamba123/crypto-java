package com.farhan.staradmin.service;

import com.farhan.staradmin.entity.Algorithme;
import com.farhan.staradmin.repository.AlgorithmeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlgorithmeService {

    private final AlgorithmeRepository algorithmeRepository;

    @Autowired
    public AlgorithmeService(AlgorithmeRepository algorithmeRepository) {
        this.algorithmeRepository = algorithmeRepository;
    }

    public List<Algorithme> getAllAlgorithmes() {
        if (algorithmeRepository.findAll().isEmpty()){
            return  null;
        }
        return algorithmeRepository.findAll();
    }
    public List<Algorithme> getAllAlgoCipher(){
        List<Algorithme> algorithmes = new ArrayList<>();
        getAllAlgorithmes().forEach(algorithme -> {
            if (algorithme.getType().equals("asymetrique") || algorithme.getType().equals("symetrique")){
                algorithmes.add(algorithme);
            }
        });
        return algorithmes;
    }
    public List<Algorithme> getAllAlgoSignature(){
        List<Algorithme> algorithmes = new ArrayList<>();
        getAllAlgorithmes().forEach(algorithme -> {
            if (algorithme.getType().equals("signature")){
                algorithmes.add(algorithme);
            }
        });
        return algorithmes;
    }
    public Algorithme getTypeAlgo(String algo){
        Algorithme algorithme = algorithmeRepository.findByName(algo);
        return algorithme;
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
