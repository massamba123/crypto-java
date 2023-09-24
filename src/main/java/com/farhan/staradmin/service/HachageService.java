package com.farhan.staradmin.service;

import org.springframework.stereotype.Service;
import utils.Utils;

import java.security.MessageDigest;
import java.util.Arrays;

@Service
public class HachageService {

    public String hash(String algo,byte[]  message) throws Exception{
        System.out.println("algo "+ algo);
        MessageDigest md = MessageDigest.getInstance(algo);
        byte[] out = md.digest(message);
        return Utils.toHex(out);
    }
    public boolean match(String algo,String plain,String hash) throws Exception{
        MessageDigest md = MessageDigest.getInstance(algo);
        byte[] out = md.digest(plain.getBytes());
        return Arrays.equals(out,hash.getBytes());
    }
}
