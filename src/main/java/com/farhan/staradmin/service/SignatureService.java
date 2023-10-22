package com.farhan.staradmin.service;

import org.springframework.stereotype.Service;
import utils.Utils;

import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

@Service
public class SignatureService {
    public String sign(String algo, PrivateKey key, byte[]  message) throws Exception{
        Signature signature = Signature.getInstance(algo);
        signature.initSign(key);
        signature.update(message);
        byte[] out = signature.sign();
        return Utils.toHex(out);
    }
    public boolean verifySign(String algo, PublicKey key, byte[]  message,byte[] signatureMessage) throws Exception{
        Signature signature = Signature.getInstance(algo);
        signature.initVerify(key);
        signature.update(message);
        return signature.verify(signatureMessage);
    }
}
