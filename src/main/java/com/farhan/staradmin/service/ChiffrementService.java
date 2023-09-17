package com.farhan.staradmin.service;

import com.farhan.staradmin.crypto.symetrique.SecretKeyImpl;
import com.farhan.staradmin.entity.Chiffrement;
import com.farhan.staradmin.repository.ChiffrementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

@Service
public class ChiffrementService {
    @Autowired
    private ChiffrementRepository chiffrementRepository;

    public byte[] createChiffrement(Chiffrement chiffrement,byte[] file,String filename,SecretKey sk) throws Exception{
        String directoryKeys = "src/main/resources/cipher/";
        chiffrement.setFileInPath(directoryKeys+chiffrement.getId()+"-"+filename);
//        SecretKey sk = SecretKeyImpl.getKey(chiffrement.getKeyPath());
        Cipher cipher = Cipher.getInstance(chiffrement.getAlgorithme());
        if (chiffrement.getMode().equals("Chiffrement")){
            cipher.init(Cipher.ENCRYPT_MODE,sk);
        }
        else  if (chiffrement.getMode().equals("Dechiffrement")){
            cipher.init(Cipher.DECRYPT_MODE,sk);
        }
        byte[] out = cipher.doFinal(file);
        System.out.println(Utils.toHex(out));
        chiffrementRepository.save(chiffrement);
        return out;
    }
}
