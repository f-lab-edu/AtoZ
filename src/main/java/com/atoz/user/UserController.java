package com.atoz.user;

import com.atoz.user.dto.request.ChangePasswordRequestDto;
import com.atoz.user.dto.request.SignupRequestDto;
import com.atoz.user.dto.request.UpdateUserRequestDto;
import com.atoz.user.dto.response.UserResponseDto;
import com.atoz.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @PostMapping("/signup")
    public UserResponseDto signup(@Validated @RequestBody SignupRequestDto signupRequestDto) {
        UserDto userDto = UserDto.builder()
                .userId(signupRequestDto.getUserId())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .email(signupRequestDto.getEmail())
                .nickname(signupRequestDto.getNickname())
                .authorities(Set.of(Authority.ROLE_USER))
                .build();

        return userService.signup(userDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping
    public void update(@Validated @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        userService.update(updateUserRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/password")
    public void changePassword(@Validated @RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        ChangePasswordRequestDto encodedPasswordDto = changePasswordRequestDto.builder()
                .password(passwordEncoder.encode(changePasswordRequestDto.getPassword()))
                .build();

        userService.changePassword(encodedPasswordDto);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping
    public void delete() {
        userService.delete();
    }
}