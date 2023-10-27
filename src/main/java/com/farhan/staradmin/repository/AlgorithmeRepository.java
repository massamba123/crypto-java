package com.farhan.staradmin.repository;

import com.farhan.staradmin.entity.Algorithme;
import com.farhan.staradmin.entity.Key;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlgorithmeRepository extends JpaRepository<Algorithme,Integer> {
    Algorithme findByName(String name);
    Algorithme findByNameAndType(String name,String type);
    List<Algorithme> findByUserId(int id);
}
