package com.atoz.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Autowired
    private final UserMapper userMapper;

    public void register (User user) {
        log.info("UserService register");

        userMapper.addUser(user);
    }
}
