package com.farhan.staradmin.crypto.asymetrique;

import com.farhan.staradmin.entity.Signature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SignatureMapper {
    public static List<Signature> mapToSignatures(Map<String, Map<String, Integer>> algorithms) {
        List<Signature> signatures = new ArrayList<>();

        for (Map.Entry<String, Map<String, Integer>> entry : algorithms.entrySet()) {
            String algorithmName = entry.getKey();
            Map<String, Integer> options = entry.getValue();

            // Obtenez les options de clés à partir de la carte
            int keySize = options.get("KeySize");
            int hashAlgorithm = options.get("HashAlgorithm");

            // Créez un objet Signature et ajoutez-le à la liste
            Signature signature = new Signature(algorithmName, getHashAlgorithmName(hashAlgorithm), keySize);
            signatures.add(signature);
        }

        return signatures;
    }

    // Méthode utilitaire pour obtenir le nom de l'algorithme de hachage
    private static String getHashAlgorithmName(int algorithm) {
        switch (algorithm) {
            case HashAlgorithms.SHA1:
                return "SHA-1";
            case HashAlgorithms.SHA256:
                return "SHA-256";
            // Ajoutez d'autres correspondances d'algorithmes de hachage ici
            default:
                return "Inconnu";
        }
    }
}
