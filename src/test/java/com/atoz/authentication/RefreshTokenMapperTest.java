package com.atoz.authentication;

import com.atoz.user.UserMapper;
import com.atoz.user.entity.Authority;
import com.atoz.security.token.RefreshTokenEntity;
import com.atoz.security.token.RefreshTokenMapper;
import com.atoz.user.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "/application-test.yaml")
@MybatisTest
class RefreshTokenMapperTest {

    @Autowired
    private RefreshTokenMapper refreshTokenMapper;

    @Autowired
    private UserMapper userMapper;

    private UserEntity signedUpUser = UserEntity.builder()
            .userId("testUserId")
            .password("testPassword")
            .nickname("testNickname")
            .email("test@test.com")
            .authorities(Set.of(Authority.ROLE_USER))
            .build();

    @BeforeEach
    void setUp() {
        // 외래키 제약조건 때문에 회원가입이 되어 있어야 토큰을 조작할 수 있습니다.
        userMapper.addUser(signedUpUser);
        userMapper.addAuthority(signedUpUser);
    }

    @Test
    void saveToken_리프레시토큰을_저장할수있다() {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .tokenKey(signedUpUser.getUserId())
                .tokenValue("testRefreshToken")
                .build();


        refreshTokenMapper.saveToken(refreshTokenEntity);
        Optional<RefreshTokenEntity> foundRefreshToken = refreshTokenMapper.findTokenByKey(refreshTokenEntity.getTokenKey());


        assertThat(foundRefreshToken.isPresent()).isTrue();
        assertThat(foundRefreshToken.get().getTokenKey()).isEqualTo(refreshTokenEntity.getTokenKey());
        assertThat(foundRefreshToken.get().getTokenValue()).isEqualTo(refreshTokenEntity.getTokenValue());
    }

    @Test
    void findByKey_리프레시토큰이_없는경우_null을_반환한다() {
        String wrongUserId = "wrongUserId";


        Optional<RefreshTokenEntity> foundRefreshToken = refreshTokenMapper.findTokenByKey(wrongUserId);


        assertThat(foundRefreshToken.isEmpty()).isTrue();
    }

    @Test
    void updateToken_저장된_리프레시토큰을_업데이트_할수있다() {
        RefreshTokenEntity saveRequest = RefreshTokenEntity.builder()
                .tokenKey(signedUpUser.getUserId())
                .tokenValue("testRefreshToken")
                .build();
        RefreshTokenEntity updateRequest = RefreshTokenEntity.builder()
                .tokenKey(signedUpUser.getUserId())
                .tokenValue("updateRefreshToken")
                .build();


        refreshTokenMapper.saveToken(saveRequest);
        refreshTokenMapper.updateToken(updateRequest);
        Optional<RefreshTokenEntity> updatedTokenEntity = refreshTokenMapper.findTokenByKey(signedUpUser.getUserId());


        assertThat(updatedTokenEntity.isPresent()).isTrue();
        assertThat(updatedTokenEntity.get().getTokenKey()).isEqualTo(updateRequest.getTokenKey());
        assertThat(updatedTokenEntity.get().getTokenValue()).isEqualTo(updateRequest.getTokenValue());
    }

    @Test
    void deleteToken_저장된_리프레시토큰을_삭제할수있다() {
        RefreshTokenEntity saveRequest = RefreshTokenEntity.builder()
                .tokenKey(signedUpUser.getUserId())
                .tokenValue("testRefreshToken")
                .build();


        refreshTokenMapper.saveToken(saveRequest);
        refreshTokenMapper.deleteToken(signedUpUser.getUserId());
        Optional<RefreshTokenEntity> deletedTokenEntity = refreshTokenMapper.findTokenByKey(signedUpUser.getUserId());


        assertThat(deletedTokenEntity.isEmpty()).isTrue();
    }
}