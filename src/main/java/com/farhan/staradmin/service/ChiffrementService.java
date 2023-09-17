package com.farhan.staradmin.service;

import com.farhan.staradmin.crypto.symetrique.SecretKeyImpl;
import com.farhan.staradmin.entity.Chiffrement;
import com.farhan.staradmin.repository.ChiffrementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.KeyLoader;
import utils.Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

@Service
public class ChiffrementService {
    @Autowired
    private ChiffrementRepository chiffrementRepository;

    public byte[] createChiffrement(Chiffrement chiffrement, byte[] file, String filename, Key sk) throws Exception{
        String directoryKeys = "src/main/resources/cipher/";
        chiffrement.setFileInPath(directoryKeys+chiffrement.getId()+"-"+filename);
//        SecretKey sk = SecretKeyImpl.getKey(chiffrement.getKeyPath());
        Cipher cipher = Cipher.getInstance(chiffrement.getAlgorithme(),"BC");
        if (chiffrement.getMode().equals("Chiffrement")){
            cipher.init(Cipher.ENCRYPT_MODE,sk);
//            if (sk.getFormat().equalsIgnoreCase("X.509")){
//                PublicKey pub = KeyLoader.deserializePublicKey(sk.getEncoded(),chiffrement.getAlgorithme());
//                cipher.init(Cipher.ENCRYPT_MODE,pub);
//            }else {
//                SecretKey secretKey = SecretKeyImpl.getKeyFromBytes(sk.getEncoded(), chiffrement.getAlgorithme());
//                cipher.init(Cipher.ENCRYPT_MODE,secretKey);
//            }
        }
        else  if (chiffrement.getMode().equals("Dechiffrement")) {
            cipher.init(Cipher.DECRYPT_MODE,sk);
//            if (sk.getFormat().equalsIgnoreCase("PKCS#8")) {
//                PrivateKey priv = KeyLoader.deserializePrivateKey(sk.getEncoded(), chiffrement.getAlgorithme());
//                cipher.init(Cipher.DECRYPT_MODE, priv);
//            }
//            else {
//                SecretKey secretKey = SecretKeyImpl.getKeyFromBytes(sk.getEncoded(), chiffrement.getAlgorithme());
//                cipher.init(Cipher.DECRYPT_MODE,secretKey);
//            }
        }
        byte[] out = cipher.doFinal(file);
        System.out.println(Utils.toHex(out));
        chiffrementRepository.save(chiffrement);
        return out;
    }
}
