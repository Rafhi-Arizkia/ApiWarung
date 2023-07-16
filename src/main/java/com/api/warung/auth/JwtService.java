package com.api.warung.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "337436773547A256872646294A404E635266556A586E5A7234753778214125442A";

    /**
     * Mendapatkan kunci rahasia yang digunakan untuk menandatangani token JWT
     * @return SECRET_KEY kunci rahasia
     */
    private Key getSigInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    /**
     * Berfungsi untuk Mengesktrak semua claim yang terkandung dalam token JWT
     * @param token  Token JWT
     * @return semua claim dari token JWT
     */
    private Claims  extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    /**
     * Mengekstrak data lain dari token jwt, Seperti waktu kadaluwarsa dan informasi lainnya
     * yang telah disimpan dalam token jwt
     * @param token Token JWT
     * @param claimsFunction fungsi resolver untuk mengambil nilai claim yang diperlukan
     * @return Nilai claims yang diperlukan
     * @param <T> Nilai claim yang diperlukan
     */
    public <T> T extractClaims(String token, Function<Claims, T> claimsFunction){
        final Claims claims = extractAllClaims(token);
        return claimsFunction.apply(claims);
    }
}
