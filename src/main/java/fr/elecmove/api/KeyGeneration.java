package fr.elecmove.api;

import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class KeyGeneration {


    public static void main(String[] args) throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = generator.generateKeyPair();
        FileOutputStream pubFile = new FileOutputStream("public.key");
        FileOutputStream privFile = new FileOutputStream("private.key");
        pubFile.write(keyPair.getPublic().getEncoded());
        privFile.write(keyPair.getPrivate().getEncoded());
        pubFile.close();
        privFile.close();

    }

}