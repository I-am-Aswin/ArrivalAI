package org.broz.arrivalai.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.broz.arrivalai.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {

    private final String secret;

    public JwtService() {
        secret = generateKey();
    }

    public String generateJwToken( User user ) {

        HashMap< String, Object > claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject( user.getUsername() )
                .setIssuedAt( new Date( System.currentTimeMillis() ) )
                .setExpiration( new Date( System.currentTimeMillis() + 1000 * 60 * 30 ))
                .signWith( getSecretKey(), SignatureAlgorithm.HS256 )
                .compact();
    }

    private String generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey key = keyGen.generateKey();
            return Base64.getEncoder().encodeToString( key.getEncoded() );
        } catch ( NoSuchAlgorithmException e ) {
            e.printStackTrace();
            return null;
        }
    }

    private Key getSecretKey() {
        System.out.println(secret);
        byte[] keyBytes = Decoders.BASE64.decode( secret );
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T extractClaims(String  token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims( String token ) {
        return Jwts.parser()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validate( String token, UserDetails userDetails ) {
        String uname = extractUsername(token);
        return ( uname.equals( userDetails.getUsername() ) && ! isExpired(token) );
    }

    public String extractUsername( String token ) {
        return extractClaims( token, Claims::getSubject);
    }

    public boolean isExpired( String token ) {
        return extractExpiration(token).before(new Date( System.currentTimeMillis() ));
    }

    public Date extractExpiration( String token ) {
        return extractClaims( token, Claims::getExpiration );
    }
}
