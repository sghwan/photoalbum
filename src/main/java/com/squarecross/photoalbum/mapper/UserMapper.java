package com.squarecross.photoalbum.mapper;

import com.squarecross.photoalbum.domain.User;
import com.squarecross.photoalbum.dto.RegisterDto;

public class UserMapper {
    public static User convertToModel(RegisterDto registerDto, String salt, String encryptedPassword) {
        User user = new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setSalt(salt);
        user.setPassword(encryptedPassword);

        return user;
    }
}
