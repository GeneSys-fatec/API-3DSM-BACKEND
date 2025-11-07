package com.servico_google.servico_google.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.servico_google.servico_google.model.entidade.GoogleOAuthToken;

public interface GoogleOAuthTokenRepository extends MongoRepository<GoogleOAuthToken, String> {
    Optional<GoogleOAuthToken> findByUserKey(String userKey);
    boolean existsByUserKey(String userKey);
}