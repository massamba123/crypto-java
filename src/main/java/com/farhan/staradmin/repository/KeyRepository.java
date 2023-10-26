package com.farhan.staradmin.repository;

import com.farhan.staradmin.entity.Key;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeyRepository extends JpaRepository<Key,Integer> {
    public List<Key> findByTypeAndNameAndSize(String type,String name,int size);
    public List<Key> findByUserId(int id);
}
