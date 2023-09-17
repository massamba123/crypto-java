package utils;

import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyLoader {

    // Load a SecretKey from a file
    public static SecretKey loadSecretKeyFromFile(String keyFilePath) throws Exception {
        Path path = Paths.get(keyFilePath);
        byte[] keyBytes = Files.readAllBytes(path);
        // Assume the keyBytes contain a serialized SecretKey (e.g., encoded using Base64)
        // You may need to adapt this part depending on how the key is stored
        return deserializeSecretKey(keyBytes);
    }

    // Load a PublicKey from a file
    public static PublicKey loadPublicKeyFromFile(String keyFilePath,String algo) throws Exception {
        Path path = Paths.get(keyFilePath);
        byte[] keyBytes = Files.readAllBytes(path);
        // Assume the keyBytes contain a serialized PublicKey (e.g., encoded using Base64)
        // You may need to adapt this part depending on how the key is stored
        return deserializePublicKey(keyBytes,algo);
    }

    // Load a PrivateKey from a file
    public static PrivateKey loadPrivateKeyFromFile(String keyFilePath,String algo) throws Exception {
        Path path = Paths.get(keyFilePath);
        byte[] keyBytes = Files.readAllBytes(path);
        // Assume the keyBytes contain a serialized PrivateKey (e.g., encoded using Base64)
        // You may need to adapt this part depending on how the key is stored
        return deserializePrivateKey(keyBytes,algo);
    }

    // Deserialize a SecretKey from bytes (you'll need to implement this)
    private static SecretKey deserializeSecretKey(byte[] keyBytes) {
        // Implement your deserialization logic here
        // Example: Use Base64 or another serialization format
        // to convert keyBytes back to a SecretKey object
        // Return the SecretKey
        // Make sure to handle exceptions properly
        return null; // Replace with your implementation
    }

    // Deserialize a PublicKey from bytes (you'll need to implement this)
    public static PublicKey deserializePublicKey(byte[] keyBytes, String algo) throws Exception {
        try {
            KeyFactory kf = KeyFactory.getInstance(algo,"BC");
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            PublicKey pub = kf.generatePublic(spec);
            return pub;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // Handle exceptions appropriately, e.g., log or throw a custom exception
            throw new Exception("Error deserializing public key: " + e.getMessage(), e);
        }
    }


    // Deserialize a PrivateKey from bytes (you'll need to implement this)
    public static PrivateKey deserializePrivateKey(byte[] keyBytes,String algo) throws Exception {
        KeyFactory kf = KeyFactory.getInstance(algo);
        PrivateKey priv = null;
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        priv = kf.generatePrivate(spec);
        // Implement your deserialization logic here
        // Example: Use Base64 or another serialization format
        // to convert keyBytes back to a PrivateKey object
        // Return the PrivateKey
        // Make sure to handle exceptions properly
        return priv; // Replace with your implementation
    }
}
