package com.farhan.staradmin.entity;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.*;

@Entity
@Table(name = "key_option")
public class Key {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;
    private String type;
    @Transient
    private boolean save;

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public List<String> getTypes(){
        return Arrays.asList("symetrique","asymetrique","partage cle");
    }
    public  List<String> getAlgoTypes(){
        switch (this.getType()) {
            case "symetrique":
                return Arrays.asList("AES", "DES");
            case "asymetrique":
                return Arrays.asList("RSA", "ECDSA");
            case "partage cle":
                return Collections.singletonList("Diffie-Hellman");
        }
        return null;
    }
    public Map<String, List<Integer>> getAllSizeAlgorithm() {
        Map<String, List<Integer>> types = new HashMap<>();

        // AES key sizes
        types.put("AES", Arrays.asList(128, 192, 256));
        types.put("DES", Collections.singletonList(56));
        types.put("DESede", Arrays.asList(112, 168)); // Separate values with commas
        types.put("Blowfish", Arrays.asList(32, 448)); // Separate values with commas
        types.put("IDEA", Collections.singletonList(128));
        types.put("Camellia", Arrays.asList(128, 192, 256));
        types.put("SEED", Collections.singletonList(128));
        types.put("Serpent", Arrays.asList(128, 192, 256));
        types.put("RC4", Arrays.asList(40, 128, 256, 2048)); // Separate values with commas
        types.put("ChaCha20", Collections.singletonList(256));

        // Algorithmes de chiffrement asymétrique
        types.put("RSA", Arrays.asList(1024, 2048, 3072, 4096));
        types.put("ECDSA", Arrays.asList(256, 384,521)); // Adjust as needed
        types.put("ElGamal", Arrays.asList(1024, 2048, 3072, 4096));


//        // Algorithmes de hachage
//        algorithmsWithKeyOptions.put("SHA-1", Collections.singletonList("160"));
//        algorithmsWithKeyOptions.put("SHA-256", Collections.singletonList("256"));
//        algorithmsWithKeyOptions.put("SHA-384", Collections.singletonList("384"));
//        algorithmsWithKeyOptions.put("SHA-512", Collections.singletonList("512"));
//        algorithmsWithKeyOptions.put("SHA-3-256", Collections.singletonList("256"));
//        algorithmsWithKeyOptions.put("MD5", Collections.singletonList("128"));
//        algorithmsWithKeyOptions.put("Whirlpool", Collections.singletonList("512"));
//        algorithmsWithKeyOptions.put("GOST R 34.11-94", Collections.singletonList("256"));


        return types;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "path")
    private String path;

    @Column(name = "size")
    private int size;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
