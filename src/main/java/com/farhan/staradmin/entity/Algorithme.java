package com.farhan.staradmin.entity;

import javax.persistence.*;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "ALGORITHME")
public class Algorithme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = true)
    private String provider = "BC";
    @Column(nullable = true)
    private String padding = "BCPadding";
    private String type;
    @ManyToOne
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Algorithme(String name) {
        this.name = name;
    }

    public Algorithme() {
    }
    public List<String> getTypes(){
        return Arrays.asList("symetrique","asymetrique","hachage","signature","mac");
    }
    public  List<String> getAlgoTypes(){
        switch (this.getType()) {
            case "symetrique":
                return Arrays.asList("AES", "DES");
            case "asymetrique":
                return List.of("RSA");
            case "signature":
                return Arrays.asList("RSA","DSA","ECDSA","EdDSA");
            case "mac":
                return Arrays.asList(  "HmacMD5",
                        "HmacSHA1",
                        "HmacSHA224",
                        "HmacSHA256",
                        "HmacSHA384",
                        "HmacSHA512");
            case "partage cle":
                return Collections.singletonList("Diffie-Hellman");
        }
        return null;
    }    public List<String> getProviders() {
        List<String> providerList = new ArrayList<>();

        // Obtenir la liste de tous les fournisseurs de sécurité installés
        Provider[] providers = Security.getProviders();

        for (Provider provider : providers) {
            providerList.add(provider.getName());
        }

        return providerList;
    }
    public Algorithme(String name, String provider, String padding) {
        this.name = name;
        this.provider = provider;
        this.padding = padding;
    }
    public Algorithme(String name, String provider) {
        this.name = name;
        this.provider = provider;
    }
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getPadding() {
        return padding;
    }

    public void setPadding(String padding) {
        this.padding = padding;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
