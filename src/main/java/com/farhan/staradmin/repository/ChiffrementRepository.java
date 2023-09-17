package com.farhan.staradmin.repository;

import com.farhan.staradmin.entity.Chiffrement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChiffrementRepository extends JpaRepository<Chiffrement,Integer> {
}
