package com.farhan.staradmin.service;

import com.farhan.staradmin.crypto.asymetrique.AsymetriqueKeyGen;
import com.farhan.staradmin.crypto.symetrique.SecretKeyImpl;
import com.farhan.staradmin.entity.Key;
import com.farhan.staradmin.repository.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.persistence.Table;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

@Service
@Table(name = "Key")
public class KeyService {

    @Autowired
    private ResourceLoader resourceLoader;

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

    public SecretKey saveKey(Key key) throws Exception{
        String directoryKeys = "src/main/resources/keys/";
        // Obtenez le chemin complet du répertoire "resources"
//        File directoryFile = resource.getFile();
//        String fullPath = "/src/main/resources/keys/";
        SecretKey keyscrete = null;
        String path = directoryKeys;
        if (key.getType().equals("symetrique")){
            path += "symetrique/" + "secret-key-"+key.getName() + "-"+key.getSize()+".key";
             keyscrete = SecretKeyImpl.genKey(key.getName(),key.getSize());
            SecretKeyImpl.saveKey(keyscrete,path);
        } else if (key.getType().equals("asymetrique")) {
             path  += "asymetrique/" + "key-" + key.getId() + ".sk";
            AsymetriqueKeyGen.genKey(key.getName(),key.getSize(),path);
        }
        key.setPath(path);
        return keyscrete;
    }

    public void deleteKey(int id) {
        keyRepository.deleteById(id);
    }
}
