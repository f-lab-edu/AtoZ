package com.atoz.user;

import com.atoz.user.entity.Authority;
import com.atoz.user.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@Slf4j
@TestPropertySource(locations = "/application-test.yaml")
@MybatisTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    private final UserEntity signedUpUser = UserEntity.builder()
            .userId("testUserId")
            .password("testPassword")
            .nickname("testNickname")
            .email("test@test.com")
            .authorities(Set.of(Authority.ROLE_USER))
            .build();

    @BeforeEach
    private void setUp() {
        userMapper.addUser(signedUpUser);
        userMapper.addAuthority(signedUpUser);
    }

    @Test
    void addUser_회원가입에_성공해야한다() {
        UserEntity newUser = UserEntity.builder()
                .userId("newUserId")
                .password("newPassword")
                .nickname("newNickname")
                .email("newEmail@test.com")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();


        userMapper.addUser(newUser);
        userMapper.addAuthority(newUser);
        Optional<UserEntity> addedUser = userMapper.findById(newUser.getUserId());


        assertThat(addedUser.isPresent()).isTrue();
        assertThat(addedUser.get().getUserId()).isEqualTo(newUser.getUserId());
    }

    @Test
    void addUser_이미_가입되어있다면_회원가입에_실패해야한다() {
        UserEntity duplicatedUser = UserEntity.builder()
                .userId("testUserId")
                .build();


        Throwable thrown = catchThrowable(() -> {
            userMapper.addUser(duplicatedUser);
        });


        assertThat(thrown).isInstanceOf(DataAccessException.class);
    }

    @Test
    void findById_사용자정보를_조회할수있다() {
        String targetUserId = signedUpUser.getUserId();


        Optional<UserEntity> foundUser = userMapper.findById(targetUserId);


        assertThat(foundUser.isPresent()).isTrue();
        assertThat(foundUser.get().getUserId()).isEqualTo(targetUserId);
    }

    @Test
    void findById_가입되지않은_사용자정보를_조회할수없다() {
        String targetUserId = "wrongUserId";


        Optional<UserEntity> foundUser = userMapper.findById(targetUserId);


        assertThat(foundUser.isEmpty()).isTrue();
    }
}