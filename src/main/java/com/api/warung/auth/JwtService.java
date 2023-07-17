package com.api.warung.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "337436773547A256872646294A404E635266556A586E5A7234753778214125442A";

    /**
     * Mendapatkan kunci rahasia yang digunakan untuk menandatangani token JWT
     * @return SECRET_KEY kunci rahasia
     */
    private Key getSignInKey(){
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
                .setSigningKey(getSignInKey())
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
    /**
     * Mengestrak nama pengguna dari token JWT
     * @param token TOKEN JWT
     * @return nama pengguna
     */
    public String extractUsername(String token){
        return extractClaims(token,Claims::getSubject);
    }
    /**
     * Menghasilkan token JWT untuk pengguna yang terauntentikasi
     * @param extractClaims Claim tambahan yang akan dimasukkan ke dalam token
     * @param userDetails Detail pengguna yang terautentikasi
     * @return Token JWT yang dihasilkan
     */
    public String generateToken(Map<String,Object> extractClaims, UserDetails userDetails){
        return  Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * menghasilkan token untuk pengguna
     * @param userDetails mengambil informasi pengguna
     * @return mengembalikan nilai true and false
     */
    public String generateUserToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }
    private Date extractExpiration(String token){
        return extractClaims(token,Claims::getExpiration);
    }
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    /**
     * Digunakan untuk memvalidasi token JWT yang diberikan masih berlaku atau tidak
     * @param token yang berisi token jwt
     * @param userDetails yang berisi informasi pengguna
     * @return untuk mengembalikan nilai true and false
     */
    public boolean validToken(String token,UserDetails userDetails){
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername())&& !isTokenExpired(token);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Date expiration = new Date(System.currentTimeMillis() + 1000*60*60);
        Map<String, Object> claims = new HashMap<>();
        claims.put("refreshToken", true);

        // Menghasilkan refresh token
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiration)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

}
