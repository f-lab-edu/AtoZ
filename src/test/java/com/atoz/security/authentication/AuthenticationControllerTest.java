package com.atoz.security.authentication;

import com.atoz.security.authentication.helper.StubAuthenticationService;
import com.atoz.error.GlobalExceptionAdvice;
import com.atoz.security.authentication.dto.request.SigninRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc sut;

    @BeforeEach
    public void beforeEach() {
        sut = MockMvcBuilders.standaloneSetup(new AuthenticationController(new StubAuthenticationService()))
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .setControllerAdvice(GlobalExceptionAdvice.class)
                .build();
    }

    @Test
    void signin_로그인에_성공한다() throws Exception {
        SigninRequestDto testSigninRequestDto = SigninRequestDto.builder()
                .userId("testId")
                .password("testPassword")
                .build();


        ResultActions resultActions = sut.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSigninRequestDto)));


        resultActions.andExpect(status().isOk());
    }

    @Test
    void signin_아이디를_입력하지않아_로그인에_실패한다() throws Exception {
        SigninRequestDto testSigninRequestDto = SigninRequestDto.builder()
                .password("testPassword")
                .build();


        ResultActions resultActions = sut.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSigninRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signin_패스워드를_입력하지않아_로그인에_실패한다() throws Exception {
        SigninRequestDto testSigninRequestDto = SigninRequestDto.builder()
                .userId("testId")
                .build();


        ResultActions resultActions = sut.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSigninRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signin_아이디와패스워드를_입력하지않아_로그인에_실패한다() throws Exception {
        SigninRequestDto testSigninRequestDto = SigninRequestDto.builder()
                .build();


        ResultActions resultActions = sut.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSigninRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }
}