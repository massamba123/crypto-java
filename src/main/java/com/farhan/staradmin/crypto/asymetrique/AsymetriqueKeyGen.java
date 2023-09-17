package com.farhan.staradmin.crypto.asymetrique;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class AsymetriqueKeyGen {
    public static void genKey(String algorithme,int taille,String path) throws Exception{
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(algorithme);
        kpg.initialize(taille);
        KeyPair keyPair = kpg.generateKeyPair();
        saveKey(keyPair.getPublic(),path+"pubProf.txt");
        saveKey(keyPair.getPrivate(),path+"privProf.txt");
    }
    public static void saveKey(Key key,String filename) throws Exception{
        FileOutputStream fos = new FileOutputStream(filename);
        if (key.getFormat().equalsIgnoreCase("X.509")){
            fos.write(key.getEncoded());
        }
        else if (key.getFormat().equalsIgnoreCase("PKCS#8")){
            fos.write(key.getEncoded());
        }
        fos.close();
        System.out.println("Clé de format : "+key.getFormat()+" bien enregistrée");
    }

    public static PublicKey getPub(String path,String algo) throws IOException {
        PublicKey pub = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            byte[] b = new byte[fis.available()];
            fis.read(b);
            KeyFactory kf = KeyFactory.getInstance(algo);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(b);
            pub = kf.generatePublic(spec);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        fis.close();
        return pub;
    }
    public static PrivateKey getPrivate(String path,String algo) throws IOException {
        PrivateKey priv = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            byte[] b = new byte[fis.available()];
            fis.read(b);
            KeyFactory kf = KeyFactory.getInstance(algo);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b);
            priv = kf.generatePrivate(spec);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        fis.close();
        return priv;
    }
}