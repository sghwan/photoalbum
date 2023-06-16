package com.squarecross.photoalbum.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {

    private String name;
    private String email;
    private String password;
    private String passwordCheck;
}
