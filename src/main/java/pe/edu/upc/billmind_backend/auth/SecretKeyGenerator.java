package pe.edu.upc.billmind_backend.auth;

import java.security.Key;
import javax.crypto.KeyGenerator;

public class SecretKeyGenerator {

    public static void main(String[] args) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            keyGen.init(256); // Tamaño de la clave
            Key secretKey = keyGen.generateKey();

            String encodedKey = java.util.Base64.getEncoder().encodeToString(secretKey.getEncoded());
            System.out.println("Generated Secret Key: " + encodedKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

