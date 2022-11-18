package com.atoz.authentication;

import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface RefreshTokenMapper {
    void saveToken(RefreshToken refreshToken);

    void updateToken(RefreshToken refreshToken);

    Optional<RefreshToken> findTokenByKey(String tokenKey);

    void deleteToken(String tokenKey);
}