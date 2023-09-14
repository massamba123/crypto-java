package com.farhan.staradmin.service;

import com.farhan.staradmin.entity.Key;
import com.farhan.staradmin.repository.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeyService {

    private final KeyRepository keyRepository;

    @Autowired
    public KeyService(KeyRepository keyRepository) {
        this.keyRepository = keyRepository;
    }

    public List<Key> getAllKeys() {
        return keyRepository.findAll();
    }

    public Key getKeyById(int id) {
        return keyRepository.findById(id).orElse(null);
    }

    public Key saveKey(Key key) {
        return keyRepository.save(key);
    }

    public void deleteKey(int id) {
        keyRepository.deleteById(id);
    }
}
