package com.bank.cardManagementSystem.utils;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Component
@Converter
public class CardNumberEncryptorUtils implements AttributeConverter<String, String> {

    @Value("${card.encryption.key}")
    private String secret_key;

    @Value("${card.encryption.algorithm}")
    private String algorithm;

    @Override
    public String convertToDatabaseColumn(String cardNumber) {

        try {
            byte[] keyBytes = Arrays.copyOf(secret_key.getBytes(StandardCharsets.UTF_8), 32);
            SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(cardNumber.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt card number", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String encryptedCardNumber) {

        try {
            byte[] keyBytes = Arrays.copyOf(secret_key.getBytes(StandardCharsets.UTF_8), 32);
            SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decoded = Base64.getDecoder().decode(encryptedCardNumber);
            byte[] decrypted = cipher.doFinal(decoded);

            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt card number", e);
        }
    }

}