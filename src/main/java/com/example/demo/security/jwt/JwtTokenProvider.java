package com.example.demo.security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1h


    @Autowired
    private UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() throws Exception {
        KeyFactory factory = KeyFactory.getInstance("RSA");
        privateKey = getPrivateKey(factory);
        publicKey = getPublicKey(factory);
    }

    public String createToken(String username, List<String> roles) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.RS512, privateKey)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
        }
    }

    private PrivateKey getPrivateKey(KeyFactory factory) throws Exception {
        ClassPathResource resource = new ClassPathResource("key.pem");
        String content = new String(Files.readAllBytes(Paths.get(resource.getURI().getPath())));
        content = content.replace("-----BEGIN PRIVATE KEY-----\n", "");
        content = content.replace("-----END PRIVATE KEY-----\n", "");


        System.out.println(content);

        byte[] encoded = DatatypeConverter.parseBase64Binary(content);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        PrivateKey privateKey = factory.generatePrivate(keySpec);
        return privateKey;

    }

    private PublicKey getPublicKey(KeyFactory factory) throws IOException, InvalidKeySpecException {
        ClassPathResource resource = new ClassPathResource("public.pem");
        String content = new String(Files.readAllBytes(Paths.get(resource.getURI().getPath())));
        content = content.replace("-----BEGIN PUBLIC KEY-----\n", "");
        content = content.replace("-----END PUBLIC KEY-----\n", "");

        byte[] encoded = DatatypeConverter.parseBase64Binary(content);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        PublicKey publicKey = factory.generatePublic(keySpec);
        return publicKey;
    }

}
