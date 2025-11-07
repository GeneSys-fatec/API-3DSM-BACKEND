package com.servico_auth.servico_auth.service.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.servico_auth.servico_auth.utils.CryptoUtils;

@Service
public class CookieService {

    @Autowired
    private Environment env;

    @Value("${crypto.secret}")
    private String secret;

    public String getSecret() {
        return secret;
    }

    public ResponseCookie createJWTCookie(String token) {
        String encryptedToken = CryptoUtils.encrypt(token, secret);

        boolean isProd = !env.acceptsProfiles(org.springframework.core.env.Profiles.of("dev"));

        return ResponseCookie.from("jwt-token", encryptedToken) 
            .httpOnly(true) 
            .secure(isProd)
            .path("/")     
            .maxAge(2 * 60 * 60) 
            .build();
    }    

    public ResponseCookie createExpiredCookie() {
        boolean isProd = !env.acceptsProfiles(org.springframework.core.env.Profiles.of("dev"));

        return ResponseCookie.from("jwt-token", "")
            .httpOnly(true)
            .secure(isProd)
            .path("/")
            .maxAge(0) 
            .build();
    }

}
