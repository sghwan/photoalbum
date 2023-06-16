package com.squarecross.photoalbum.ssr.service;

import com.squarecross.photoalbum.domain.User;
import com.squarecross.photoalbum.dto.EmailDto;
import com.squarecross.photoalbum.dto.LoginDto;
import com.squarecross.photoalbum.dto.RegisterDto;
import com.squarecross.photoalbum.repository.UserRepository;
import com.squarecross.photoalbum.util.Encrypt;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    void login() {
        //given
        User user = new User();
        user.setName("seo");
        user.setEmail("anan123456@naver.com");
        user.setPassword("CE370EFA9D66BD305E9DC65A472F6CC77D2266DD7DE93782BD2D63D307C82DBB");
        user.setSalt("abcde");
        userRepository.save(user);

        //when
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("anan123456@naver.com");
        loginDto.setPassword("1234");
        User loginUser = userService.login(loginDto);

        //then
        assertThat(loginUser).isEqualTo(user);
    }

    @Test
    void register() {
        //given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("anan123456@naver.com");
        registerDto.setName("seo");
        registerDto.setPassword("1234");
        registerDto.setPasswordCheck("1234");

        //when
        User register = userService.register(registerDto);
        String encrypt = new Encrypt().getEncrypt(registerDto.getPassword(), register.getSalt());

        //then
        assertThat(register.getEmail()).isEqualTo("anan123456@naver.com");
        assertThat(encrypt).isEqualTo(register.getPassword());
    }
}
