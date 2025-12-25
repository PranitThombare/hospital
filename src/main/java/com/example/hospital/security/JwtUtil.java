package com.example.hospital.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 🔐 secret key (must be 32+ chars)
    private static final String SECRET = "Lnedofase0eEQG4Qv8r0EqcVf8dXskAUCqozIJz4FuC";

    // ⏰ 4 hours
    private static final long EXPIRATION = 1000 * 60 * 60 * 4;

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // ✅ CREATE TOKEN (email + role)
    public String generateToken(UserDetailsImpl user) {

        return Jwts.builder().setSubject(user.getUsername())      // email
                .claim("role", user.getRole())       // 👈 ROLE ADDED
                .setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)).signWith(key, SignatureAlgorithm.HS256).compact();
    }

    // ✅ READ EMAIL FROM TOKEN
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    // ✅ READ ROLE FROM TOKEN
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // ✅ CHECK TOKEN VALID OR NOT
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // 🔧 INTERNAL METHOD
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}


//package com.example.hospital.security;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Date;
//
//@Component
//public class JwtUtil {
//
//    private final String SECRET = "Lnedofase0eEQG4Qv8r0EqcVf8dXskAUCqozIJz4FuC";
//    private final long EXPIRATION = 1000 * 60 * 60 * 4; // 4 hour
//
//    public String generateToken(String email) {
//        return Jwts.builder()
//                .setSubject(email)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
//                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public String extractEmail(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(SECRET.getBytes())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    public boolean isTokenValid(String token) {
//        try {
//            extractEmail(token);
//            return true;
//        } catch (JwtException e) {
//            return false;
//        }
//    }
//}
