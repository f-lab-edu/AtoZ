package com.atoz.user;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void addUser(UserEntity signupEntity);

    UserEntity findById(String userId);

}
