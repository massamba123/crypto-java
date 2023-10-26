package com.farhan.staradmin.crypto.symetrique;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class SecretKeyImpl implements TestSecretKey {
    public static SecretKey genKey(String algorithm, int taille,String type) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(algorithm);
        if (type.equals("symetrique")){
            kg.init(taille);
        }
        return kg.generateKey();
    }
    public static void saveKey(SecretKey key,String path) throws Exception{
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
        out.writeObject(key);
        out.close();
        fileOutputStream.close();
        String outPut = "Cle d'Algo "+key.getAlgorithm()+" bien sauvegardé";
        System.out.println(outPut);
    }
    public static SecretKey getKeyFromBytes(byte[] keyBytes,String algorithm) throws Exception {
        // Vous devez déterminer le type d'algorithme et la taille de la clé utilisés
        // par exemple, AES avec une clé de 256 bits

        // Vous devez utiliser un algorithme de dérivation de clé approprié si votre clé n'est pas directement utilisable
        // Par exemple, pour AES, vous pouvez utiliser PBKDF2 pour dériver une clé utilisable

        // Créez une clé secrète à partir des octets fournis
        // Si vous avez utilisé un algorithme de dérivation de clé, vous pouvez le faire ici

        // Retournez la clé secrète créée
        return new SecretKeySpec(keyBytes,algorithm) ;
    }
    public static SecretKey getKey(String path) throws Exception{
        FileInputStream in = new FileInputStream(path);
        ObjectInputStream file = new ObjectInputStream(in);
        SecretKey sk = (SecretKey) file.readObject();
        file.close();
        in.close();
        return sk;
    }
}