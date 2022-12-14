package com.atoz.user;

import com.atoz.user.dto.request.ChangePasswordRequestDto;
import com.atoz.user.dto.request.UpdateUserRequestDto;
import com.atoz.user.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {

    void addUser(UserDto userDto);

    void addAuthority(UserDto userDto);

    Optional<UserDto> findUserByUserId(String userId);

    void updateUser(UpdateUserRequestDto updateUserRequestDto);

    void changePassword(ChangePasswordRequestDto changePasswordRequestDto);

    void deleteUser(String userId);
}
