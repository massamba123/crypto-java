package com.farhan.staradmin.crypto.symetrique;

import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CryptoPaddingLister {

    public static Map<String, List<String>> getPaddingForAlgorithms() {
        Map<String, List<String>> paddingMap = new HashMap<>();
        
        // Obtenir la liste de tous les fournisseurs de sécurité installés
        Provider[] providers = Security.getProviders();
        
        for (Provider provider : providers) {
            for (Map.Entry<Object, Object> entry : provider.entrySet()) {
                String key = entry.getKey().toString();
                
                // Vérifier si la clé contient "Cipher."
                if (key.startsWith("Cipher.")) {
                    String algorithm = key.substring("Cipher.".length());
                    List<String> paddings = paddingMap.getOrDefault(algorithm, new ArrayList<>());
                    String value = entry.getValue().toString();
                    
                    // Vérifier si la valeur contient "Padding." pour extraire le padding
                    if (value.startsWith("Padding.")) {
                        String padding = value.substring("Padding.".length());
                        paddings.add(padding);
                    }
                    
                    paddingMap.put(algorithm, paddings);
                }
            }
        }
        
        return paddingMap;
    }

    public static void main(String[] args) {
        Map<String, List<String>> paddingMap = getPaddingForAlgorithms();
        
        // Afficher les algorithmes et leurs paddings
        for (Map.Entry<String, List<String>> entry : paddingMap.entrySet()) {
            String algorithm = entry.getKey();
            List<String> paddings = entry.getValue();
            
            System.out.println("Algorithm: " + algorithm);
            System.out.println("Paddings: " + paddings);
        }
    }
}
