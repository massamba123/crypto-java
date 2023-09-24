package com.farhan.staradmin.crypto.asymetrique;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignatureAlgorithms {
    public static Map<String, Map<String, Integer>> getSignatureAlgorithms() {
        Map<String, Map<String, Integer>> algorithms = new HashMap<>();

        // RSA
        Map<String, Integer> rsaOptions = new HashMap<>();
        rsaOptions.put("KeySize", 2048); // Taille de la clé en bits
        rsaOptions.put("HashAlgorithm", HashAlgorithms.SHA256); // Algorithme de hachage
        algorithms.put("RSA", rsaOptions);

        // DSA
        Map<String, Integer> dsaOptions = new HashMap<>();
        dsaOptions.put("KeySize", 1024); // Taille de la clé en bits
        dsaOptions.put("HashAlgorithm", HashAlgorithms.SHA1); // Algorithme de hachage
        algorithms.put("DSA", dsaOptions);

        // ECDSA
        Map<String, Integer> ecdsaOptions = new HashMap<>();
        ecdsaOptions.put("Curve", ECCurves.P256); // Courbe elliptique
        ecdsaOptions.put("HashAlgorithm", HashAlgorithms.SHA256); // Algorithme de hachage
        algorithms.put("ECDSA", ecdsaOptions);

        // EdDSA
        Map<String, Integer> eddsaOptions = new HashMap<>();
        eddsaOptions.put("Curve", ECCurves.Ed25519); // Courbe elliptique
        // Pas d'option de hachage spécifique
        algorithms.put("EdDSA", eddsaOptions);

        return algorithms;
    }

    public static void main(String[] args) {
        Map<String, Map<String, Integer>> signatureAlgorithms = getSignatureAlgorithms();

        for (Map.Entry<String, Map<String, Integer>> entry : signatureAlgorithms.entrySet()) {
            String algorithm = entry.getKey();
            Map<String, Integer> options = entry.getValue();
            System.out.println(options);
        }
    }
}

class HashAlgorithms {
    public static final int SHA1 = 1;
    public static final int SHA256 = 2;
    // Ajoutez d'autres algorithmes de hachage selon vos besoins

    public static String getHashAlgorithmName(int algorithm) {
        return switch (algorithm) {
            case SHA1 -> "SHA-1";
            case SHA256 -> "SHA-256";
            // Ajoutez d'autres correspondances d'algorithmes de hachage
            default -> "Inconnu";
        };
    }
}

class ECCurves {
    public static final int P256 = 1;
    public static final int P384 = 2;
    public static final int P521 = 3;
    public static final int Ed25519 = 4;
    public static final int Ed448 = 5;
    // Ajoutez d'autres courbes elliptiques selon vos besoins
}
