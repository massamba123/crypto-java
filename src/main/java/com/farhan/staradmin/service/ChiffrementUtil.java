package com.farhan.staradmin.service;

import com.farhan.staradmin.entity.Key;
import com.farhan.staradmin.repository.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChiffrementUtil {
    @Autowired
    private KeyRepository keyRepository;


    public List<Key> getKeysByAttribut(String type,String name,int size){
        System.out.println("chiffment "+ type);
        System.out.println("chiffment "+ name);
        System.out.println("chiffment "+ size);
        List<Key> keys = keyRepository.findByTypeAndNameAndSize(type,name,size);
        return keys;
    }
}
