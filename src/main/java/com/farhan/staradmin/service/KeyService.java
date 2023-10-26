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
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        if (!key.isSave()){
            keyscrete = SecretKeyImpl.genKey(key.getName(),key.getSize(),key.getType());
            return keyscrete;
        } else {
            path += "symetrique/" + key.getPath() +".key";
            keyscrete = SecretKeyImpl.genKey(key.getName(),key.getSize(),key.getType());
            SecretKeyImpl.saveKey(keyscrete,path);
            key.setPath(path);
            keyRepository.save(key);
            return null;
        }
    }
    public KeyPair saveAsymetrique(Key key) throws Exception{
        String name = key.getPath();
        String directoryKeys = "src/main/resources/keys/asymetrique/";
        // Obtenez le chemin complet du répertoire "resources"
//        File directoryFile = resource.getFile();
//        String fullPath = "/src/main/resources/keys/";
        SecretKey keyscrete = null;
        String path = directoryKeys;
        KeyPair kp = AsymetriqueKeyGen.genKey(key.getName(),key.getSize());
        if (!key.isSave()){
            return kp;
        } else {
            PublicKey publicKey = kp.getPublic();
            PrivateKey privateKey = kp.getPrivate();
            AsymetriqueKeyGen.saveKey(publicKey,path+key.getPath()+".pub");
            AsymetriqueKeyGen.saveKey(privateKey,path+key.getPath()+".priv");
            key.setPath(path+name);
            key.setLocalDate(LocalDate.now());
            keyRepository.save(key);
            return null;
        }
    }
    public List<Key> getKeyByUser(int id){
        return keyRepository.findByUserId(id);
    }

    public void deleteKey(int id) {
        keyRepository.deleteById(id);
    }
}
